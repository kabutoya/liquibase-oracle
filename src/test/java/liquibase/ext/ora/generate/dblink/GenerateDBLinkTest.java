package liquibase.ext.ora.generate.dblink;

import liquibase.database.Database;
import liquibase.database.core.MySQLDatabase;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.DatabaseException;
import liquibase.executor.ExecutorService;
import liquibase.ext.ora.generate.TestDatabaseSnapshot;
import liquibase.ext.ora.generate.TestExecuter;
import liquibase.ext.ora.snapshot.DBLinkSnapshotGenerator;
import liquibase.ext.ora.structure.Check;
import liquibase.ext.ora.structure.DBLink;
import liquibase.ext.ora.structure.Tablespace;
import liquibase.ext.ora.testing.BaseTestCase;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.InvalidExampleException;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Schema;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;

public class GenerateDBLinkTest extends BaseTestCase {
    private GeneratorWrapper generator;
    private Database database;
    private TestDatabaseSnapshot snapshot;
    private TestExecuter executer;
    int PRIORITY_NONE = -1;
    int PRIORITY_DEFAULT = 1;
    int PRIORITY_DATABASE = 5;
    int PRIORITY_ADDITIONAL = 50;

    @Before
    public void setUp() throws Exception {
        generator = new GeneratorWrapper();
        database = new OracleDatabase();
        snapshot = new TestDatabaseSnapshot(null, database);
        executer = new TestExecuter();
        ExecutorService.getInstance().setExecutor(database, executer);
     }

    @Test
    public void getPriorityOtherDB() {
        DBLink target = new DBLink();
        int actual = generator.getPriority(target.getClass(), new MySQLDatabase());
        assertEquals(PRIORITY_NONE, actual);
    }
    @Test
    public void getPriorityOtherDBObject() {
        Tablespace target = new Tablespace();
        int actual = generator.getPriority(target.getClass(), new OracleDatabase());
        assertEquals(PRIORITY_NONE, actual);
    }
    @Test
    public void getPrioritySameDBObject() {
        DBLink target = new DBLink();
        int actual = generator.getPriority(target.getClass(), new OracleDatabase());
        assertEquals(PRIORITY_DEFAULT, actual);
    }
    @Test
    public void snapshotObject() {
        try {
            DBLink target1 = new DBLink();
            DBLink target2 = new DBLink();
            DatabaseObject ret = generator.wrapSnapshotObject(target1, snapshot);
            assertTrue(target1 == ret);
            assertFalse(target2 == ret);
        } catch (Throwable th) {
            System.out.println(th.getStackTrace());
            fail();
        }
    }
    @Test
    public void addToZero() {
        executer.setLoopCount(0);
        Schema obj = new Schema();
        try {
            generator.wrapaddTo(obj, snapshot);
        } catch (Throwable th) {
            System.out.println(th.getStackTrace());
            fail();
        }
        Map map = obj.getAttribute("objects", HashMap.class);
        assertEquals(0, map.size());
    }
    @Test
    public void addToOne() {
        executer.setLoopCount(1);
        Schema obj = new Schema();
        try {
            generator.wrapaddTo(obj, snapshot);
        } catch (Throwable th) {
            System.out.println(th.getStackTrace());
            fail();
        }

        Map map = obj.getAttribute("objects", HashMap.class);
        HashSet datas = (HashSet)map.get(DBLink.class);
        assertEquals(1, datas.size());

        Iterator ite = datas.iterator();
        DBLink data1 = (DBLink)ite.next();

        assertEquals("NAME0", data1.getDBLinkName());
        assertEquals("PRIVATE", data1.getType());
        assertEquals("${dblink.name0.username}", data1.getUser());
        assertEquals("${dblink.name0.password}", data1.getPassword());
        assertEquals("${dblink.name0.servicename}", data1.getUsing());

    }
    @Test
    public void addToThree() {
        executer.setLoopCount(3);
        Schema obj = new Schema();
        try {
            generator.wrapaddTo(obj, snapshot);
        } catch (Throwable th) {
            System.out.println(th.getStackTrace());
            fail();
        }

        Map map = obj.getAttribute("objects", HashMap.class);
        HashSet datas = (HashSet)map.get(DBLink.class);
        assertEquals(3, datas.size());

    }

    class GeneratorWrapper extends DBLinkSnapshotGenerator {
        public DatabaseObject wrapSnapshotObject(DatabaseObject example, DatabaseSnapshot snapshot) throws DatabaseException {
            return super.snapshotObject(example, snapshot);
        }
        public void wrapaddTo(DatabaseObject foundObject, DatabaseSnapshot snapshot) throws DatabaseException, InvalidExampleException {
            super.addTo(foundObject, snapshot);
        }

    }

}
