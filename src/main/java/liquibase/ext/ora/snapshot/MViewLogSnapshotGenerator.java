package liquibase.ext.ora.snapshot;

import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.DatabaseException;
import liquibase.executor.ExecutorService;
import liquibase.ext.ora.structure.MView;
import liquibase.ext.ora.structure.MViewLog;
import liquibase.ext.ora.structure.Tablespace;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.InvalidExampleException;
import liquibase.snapshot.jvm.JdbcSnapshotGenerator;
import liquibase.statement.core.RawSqlStatement;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Schema;

import java.util.List;
import java.util.Map;

public class MViewLogSnapshotGenerator extends JdbcSnapshotGenerator {
    public MViewLogSnapshotGenerator() {
        super(MViewLog.class, new Class[] { Schema.class });
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
        if (!snapshot.getSnapshotControl().shouldInclude(MViewLog.class)) {
            return;
        }

        if (foundObject instanceof Schema) {
            // indexの表領域を取得
            String query = "select MASTER as MASTER, LOG_TABLE as LOG_TBL, ROWIDS as ISROWID, PRIMARY_KEY as ISKEY, SEQUENCE as ISSEQ from ALL_MVIEW_LOGS order by MASTER, LOG_TBL asc";
            Schema schema = (Schema) foundObject;
            Database database = snapshot.getDatabase();
            List<Map<String, ?>> views = ExecutorService.getInstance().getExecutor(database).queryForList(new RawSqlStatement(query));
            for (Map<String, ?> row : views) {
                schema.addDatabaseObject(constractTablespace(row, schema));
            }
        }
    }

    private MViewLog constractTablespace(Map<String, ?> row, Schema schema) {
        MViewLog mlog = new MViewLog();
        mlog.setSchema(schema);
        mlog.setName((String) row.get("LOG_TBL"));
        String YES = "YES";
        mlog.setHasRowId(YES.equalsIgnoreCase((String) row.get("ISROWID")));
        mlog.setHasPK(YES.equalsIgnoreCase((String) row.get("ISKEY")));
        mlog.setHasSequence(YES.equalsIgnoreCase((String) row.get("ISSEQ")));
        return mlog;
    }

}
