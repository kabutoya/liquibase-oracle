package liquibase.ext.ora.snapshot;

import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.DatabaseException;
import liquibase.executor.ExecutorService;
import liquibase.ext.ora.structure.Synonym;
import liquibase.ext.ora.structure.Trigger;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.InvalidExampleException;
import liquibase.snapshot.jvm.JdbcSnapshotGenerator;
import liquibase.statement.core.RawSqlStatement;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Schema;

import javax.print.attribute.standard.SheetCollate;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class TriggerSnapshotGenerator extends JdbcSnapshotGenerator {
    Pattern lineSparatorPattern =  Pattern.compile("\r\n|[\n\r\u2028\u2029\u0085]");

    public TriggerSnapshotGenerator() {
        super(Trigger.class, new Class[] { Schema.class });
    }
    @Override
    public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
        if(!(database instanceof OracleDatabase)) {
            return PRIORITY_NONE;
        }
        return super.getPriority(objectType, database);
    }

    @Override
    protected DatabaseObject snapshotObject(DatabaseObject example, DatabaseSnapshot snapshot) throws DatabaseException {
        return example;
    }

    @Override
    protected void addTo(DatabaseObject foundObject, DatabaseSnapshot snapshot) throws DatabaseException, InvalidExampleException {
        if (!snapshot.getSnapshotControl().shouldInclude(Synonym.class)) {
            return;
        }

        if (foundObject instanceof Schema) {
            String query = "select US.LINE as LINE, US.NAME as NAME, US.TEXT as TEXT from USER_SOURCE US, USER_TRIGGERS UT where US.NAME = UT.TRIGGER_NAME order by US.NAME, US.LINE asc";

            Schema schema = (Schema) foundObject;
            Database database = snapshot.getDatabase();
            List<Map<String, ?>> triggerLines = ExecutorService.getInstance().getExecutor(database).queryForList(new RawSqlStatement(query));
            StringBuilder sb = new StringBuilder();
            String beforeName = "";
            for (Map<String, ?> triggerLine : triggerLines) {
                String name = (String) triggerLine.get("NAME");
                if(beforeName.equals(name)) {
                    sb.append((String) triggerLine.get("TEXT")).append(" ");
                } else { // nameが変わるのは次のTriggerに移った時になる。そのため溜め込んだ値を書き込む。
                    setNewTriggerTo(schema, sb, beforeName);
                    beforeName = name;
                    sb.append((String) triggerLine.get("TEXT")).append(" ");
                }
            }
            setNewTriggerTo(schema, sb, beforeName); // 最後のTriggerはループ中で書き込まれないためここで書き込み。
        }
    }

    private void setNewTriggerTo(Schema schema, StringBuilder sb, String name) {
        if(name.isEmpty()) return;

        Trigger trigger = new Trigger();
        trigger.setName(name);
        trigger.setSchema(schema);
        String sql = sb.toString();
        trigger.setTriggerSql(lineSparatorPattern.matcher(sql).replaceAll(""));
        schema.addDatabaseObject(trigger);
        sb.delete(0, sb.length()); // このタイミングでバッファをクリア。
    }

}
