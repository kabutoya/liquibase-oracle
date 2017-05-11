package liquibase.ext.ora.snapshot;

import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.DatabaseException;
import liquibase.executor.ExecutorService;
import liquibase.ext.ora.structure.DBLink;
import liquibase.ext.ora.structure.Synonym;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.InvalidExampleException;
import liquibase.snapshot.jvm.JdbcSnapshotGenerator;
import liquibase.statement.core.RawSqlStatement;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Schema;

import java.util.List;
import java.util.Map;

public class DBLinkSnapshotGenerator extends JdbcSnapshotGenerator {

    public DBLinkSnapshotGenerator() {
        super(DBLink.class, new Class[] { Schema.class });
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
            String query = "select USERNAME as USERNAME, DB_LINK as NAME FROM user_db_links";

            Schema schema = (Schema) foundObject;
            Database database = snapshot.getDatabase();
            List<Map<String, ?>> lines = ExecutorService.getInstance().getExecutor(database).queryForList(new RawSqlStatement(query));
            StringBuilder sb = new StringBuilder();
            for (Map<String, ?> line : lines) {
                String linkName = (String) line.get("NAME");
                DBLink link = new DBLink();
                link.setSchema(schema);
                link.setType("PRIVATE"); // 本ツールではPUBLICについては対応しない。
                link.setDBLinkName(linkName);
                link.setUser("${dblink."+linkName.toLowerCase()+".username}"); //DBLinkのID・PWDは環境ごとに異なるため最初からプロパティを利用する想定で。
                link.setPassword("${dblink."+linkName.toLowerCase()+".password}"); //DBLinkのID・PWDは環境ごとに異なるため最初からプロパティを利用する想定で。
                link.setUsing("${dblink."+linkName.toLowerCase()+".servicename}"); //DBLinkのID・PWDは環境ごとに異なるため最初からプロパティを利用する想定で。
                // SHARED DB-Linkにも対応しないためAUTHENTICATEDも最初から作らない。
                schema.addDatabaseObject(link);
            }
        }
    }

}
