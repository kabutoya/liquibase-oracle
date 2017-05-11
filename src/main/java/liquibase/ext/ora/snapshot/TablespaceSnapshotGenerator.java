package liquibase.ext.ora.snapshot;

import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.DatabaseException;
import liquibase.executor.ExecutorService;
import liquibase.ext.ora.structure.Tablespace;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.InvalidExampleException;
import liquibase.snapshot.jvm.JdbcSnapshotGenerator;
import liquibase.statement.core.RawSqlStatement;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Schema;

import java.util.List;
import java.util.Map;

public class TablespaceSnapshotGenerator extends JdbcSnapshotGenerator {

    public TablespaceSnapshotGenerator() {
        super(Tablespace.class, new Class[] { Schema.class });
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
        if (!snapshot.getSnapshotControl().shouldInclude(Tablespace.class)) {
            return;
        }

        if (foundObject instanceof Schema) {
            // indexの表領域を取得
            String indexQuery = "select INDEX_NAME as NAME,TABLESPACE_NAME as TABLESPACE_NAME from USER_INDEXES order by TABLESPACE_NAME, NAME asc";
            Schema schema = (Schema) foundObject;
            Database database = snapshot.getDatabase();
            List<Map<String, ?>> indexSpaces = ExecutorService.getInstance().getExecutor(database).queryForList(new RawSqlStatement(indexQuery));
            for (Map<String, ?> indexSpaceRow : indexSpaces) {
                schema.addDatabaseObject(constractTablespace(indexSpaceRow, schema, Tablespace.TYPE_INDEX));
            }
            // tableとMVIEWの表領域を取得(同じUSER_TABLESに情報があるためMVIEWかどうかの判定を行っている)
            String tableQuery = "select UT.TABLE_NAME as NAME, UT.TABLESPACE_NAME as TABLESPACE_NAME, UM.MVIEW_NAME as IS_NULL from USER_TABLES UT, USER_MVIEWS UM where UT.TABLE_NAME = UM.MVIEW_NAME(+) order by TABLESPACE_NAME, NAME asc";
            List<Map<String, ?>> tableSpaces = ExecutorService.getInstance().getExecutor(database).queryForList(new RawSqlStatement(tableQuery));
            for (Map<String, ?> tableSpaceRow : tableSpaces) {
                String isMviewData = (String) tableSpaceRow.get("IS_NULL");
                if(isMviewData == null || isMviewData.isEmpty()) {
                    schema.addDatabaseObject(constractTablespace(tableSpaceRow, schema, Tablespace.TYPE_TABLE));
                } else {
                    schema.addDatabaseObject(constractTablespace(tableSpaceRow, schema, Tablespace.TYPE_MVIEW));
                }
            }
        }
    }

    private Tablespace constractTablespace(Map<String, ?> indexSpaceRow, Schema schema, String type) {
        Tablespace tblSpace = new Tablespace();
        tblSpace.setSchema(schema);
        tblSpace.setType(type);
        tblSpace.setTableSpaceName((String) indexSpaceRow.get("TABLESPACE_NAME"));
        tblSpace.setObjectName((String) indexSpaceRow.get("NAME"));
        return tblSpace;
    }

}
