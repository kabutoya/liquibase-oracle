package liquibase.ext.ora.generate;

import liquibase.change.Change;
import liquibase.database.Database;
import liquibase.exception.DatabaseException;
import liquibase.executor.Executor;
import liquibase.sql.visitor.SqlVisitor;
import liquibase.statement.SqlStatement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 01008716 on 2017/05/11.
 */
public class TestExecuter implements Executor {
    int count = 0;
    @Override
    public List<Map<String, ?>> queryForList(SqlStatement sqlStatement) throws DatabaseException {
        List<String> names = getColumNameList(sqlStatement.toString());
        List<Map<String, ?>> ret = new ArrayList<Map<String, ?>>();
        for(int i=0; i<this.count; i++) {
            Map map = new HashMap<String, String>();
            for(String name : names) {
                map.put(name, name + i);
            }
            ret.add(map);
        }
        return ret;
    }
    private List<String> getColumNameList(String sql){
        String selector = sql.split("(from|FROM)")[0];
        selector = selector.split("(select|SELECT)")[1];
        List<String> list = new ArrayList<String>();
        for(String data : selector.split(",")) {
            String target = data.split("(as|AS)")[1];
            list.add(target.replaceAll("\\s", ""));
        }
        return list;
    }
    public void setLoopCount(int count) {
        this.count = count;
    }

    @Override
    public void setDatabase(Database database) {

    }

    @Override
    public <T> T queryForObject(SqlStatement sqlStatement, Class<T> aClass) throws DatabaseException {
        return null;
    }

    @Override
    public <T> T queryForObject(SqlStatement sqlStatement, Class<T> aClass, List<SqlVisitor> list) throws DatabaseException {
        return null;
    }

    @Override
    public long queryForLong(SqlStatement sqlStatement) throws DatabaseException {
        return 0;
    }

    @Override
    public long queryForLong(SqlStatement sqlStatement, List<SqlVisitor> list) throws DatabaseException {
        return 0;
    }

    @Override
    public int queryForInt(SqlStatement sqlStatement) throws DatabaseException {
        return 0;
    }

    @Override
    public int queryForInt(SqlStatement sqlStatement, List<SqlVisitor> list) throws DatabaseException {
        return 0;
    }

    @Override
    public List queryForList(SqlStatement sqlStatement, Class aClass) throws DatabaseException {
        return null;
    }

    @Override
    public List queryForList(SqlStatement sqlStatement, Class aClass, List<SqlVisitor> list) throws DatabaseException {
        return null;
    }

    @Override
    public List<Map<String, ?>> queryForList(SqlStatement sqlStatement, List<SqlVisitor> list) throws DatabaseException {
        return null;
    }

    @Override
    public void execute(Change change) throws DatabaseException {

    }

    @Override
    public void execute(Change change, List<SqlVisitor> list) throws DatabaseException {

    }

    @Override
    public void execute(SqlStatement sqlStatement) throws DatabaseException {

    }

    @Override
    public void execute(SqlStatement sqlStatement, List<SqlVisitor> list) throws DatabaseException {

    }

    @Override
    public int update(SqlStatement sqlStatement) throws DatabaseException {
        return 0;
    }

    @Override
    public int update(SqlStatement sqlStatement, List<SqlVisitor> list) throws DatabaseException {
        return 0;
    }

    @Override
    public void comment(String s) throws DatabaseException {

    }

    @Override
    public boolean updatesDatabase() {
        return false;
    }
}
