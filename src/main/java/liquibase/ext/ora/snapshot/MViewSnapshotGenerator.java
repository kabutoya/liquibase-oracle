package liquibase.ext.ora.snapshot;

import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.DatabaseException;
import liquibase.executor.ExecutorService;
import liquibase.ext.ora.structure.MView;
import liquibase.ext.ora.structure.Tablespace;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.InvalidExampleException;
import liquibase.snapshot.jvm.JdbcSnapshotGenerator;
import liquibase.statement.core.RawSqlStatement;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Schema;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MViewSnapshotGenerator extends JdbcSnapshotGenerator {
    Pattern pattern =  Pattern.compile("(WITH|with)\\s+(PRIMARY KEY|ROWID)");

    public MViewSnapshotGenerator() {
        super(MView.class, new Class[] { Schema.class });
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
        if (!snapshot.getSnapshotControl().shouldInclude(MView.class)) {
            return;
        }

        if (foundObject instanceof Schema) {
            // indexの表領域を取得
            String query = "select MVIEW_NAME as NAME, QUERY as QUERY, REFRESH_MODE as R_MODE, REFRESH_METHOD as METHOD, DBMS_METADATA.GET_DDL('MATERIALIZED_VIEW',MVIEW_NAME) as DETAIL from USER_MVIEWS order by NAME asc";
            Schema schema = (Schema) foundObject;
            Database database = snapshot.getDatabase();
            List<Map<String, ?>> views = ExecutorService.getInstance().getExecutor(database).queryForList(new RawSqlStatement(query));
            for (Map<String, ?> row : views) {
                schema.addDatabaseObject(constractTablespace(row, schema));
            }
        }
    }

    private MView constractTablespace(Map<String, ?> row, Schema schema) {
        MView mview = new MView();
        mview.setSchema(schema);
        mview.setName((String) row.get("NAME"));
        mview.setRefreshMethod((String) row.get("METHOD"));
        mview.setMode((String) row.get("R_MODE"));
        String with = (String) row.get("DETAIL");
        Matcher m = pattern.matcher(with);
        if(m.find()) {
            mview.setWith(m.group(2));
        }
        mview.setQuery((String) row.get("QUERY"));
        return mview;
    }

}
