package liquibase.ext.ora.snapshot;

import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.DatabaseException;
import liquibase.executor.ExecutorService;
import liquibase.ext.ora.structure.Check;
import liquibase.ext.ora.structure.Synonym;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.InvalidExampleException;
import liquibase.snapshot.jvm.JdbcSnapshotGenerator;
import liquibase.statement.core.RawSqlStatement;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Schema;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class CheckSnapshotGenerator extends JdbcSnapshotGenerator {

    Pattern isNotNullConstraintPattern =  Pattern.compile(".+\"?\\s+IS\\s+NOT NULL");

    public CheckSnapshotGenerator() {
        super(Check.class, new Class[] { Schema.class });
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
            String query = "select SEARCH_CONDITION as CONDITION, CONSTRAINT_NAME as NAME, TABLE_NAME as TABLE_NAME from USER_CONSTRAINTS where SEARCH_CONDITION is not null";

            Schema schema = (Schema) foundObject;
            Database database = snapshot.getDatabase();
            List<Map<String, ?>> lines = ExecutorService.getInstance().getExecutor(database).queryForList(new RawSqlStatement(query));
            for (Map<String, ?> line : lines) {
                // USER_CONSTRAINTSにはNOT NULL制約が含まれるが、NOT NULL制約はLIQUIBASE本体側で実装済みのため除外する。
                if(!isNotNullConstraintPattern.matcher((String)line.get("CONDITION")).matches()){
                    Check check = new Check();
                    check.setSchema(schema);
                    check.setTableName((String)line.get("TABLE_NAME"));
                    check.setConstraintName((String)line.get("NAME"));
                    check.setCondition((String)line.get("CONDITION"));
                    schema.addDatabaseObject(check);
                }
            }
        }
    }

}
