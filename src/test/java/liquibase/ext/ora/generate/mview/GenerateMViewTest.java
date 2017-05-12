package liquibase.ext.ora.generate.mview;

import liquibase.database.Database;
import liquibase.database.core.MySQLDatabase;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.DatabaseException;
import liquibase.executor.ExecutorService;
import liquibase.ext.ora.generate.TestDatabaseSnapshot;
import liquibase.ext.ora.generate.TestExecuter;
import liquibase.ext.ora.snapshot.MViewSnapshotGenerator;
import liquibase.ext.ora.snapshot.SynonymSnapshotGenerator;
import liquibase.ext.ora.structure.MView;
import liquibase.ext.ora.structure.Synonym;
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

public class GenerateMViewTest extends BaseTestCase {
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
        MView target = new MView();
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
        MView target = new MView();
        int actual = generator.getPriority(target.getClass(), new OracleDatabase());
        assertEquals(PRIORITY_DEFAULT, actual);
    }
    @Test
    public void snapshotObject() {
        try {
            MView target1 = new MView();
            MView target2 = new MView();
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
        HashSet datas = (HashSet)map.get(MView.class);
        assertEquals(1, datas.size());

        Iterator ite = datas.iterator();
        MView data1 = (MView)ite.next();
        MView expected1 = new MView();
        expected1.setSchema(obj);
        expected1.setName("NAME0");
        expected1.setRefreshMethod("R_MODE0");
        expected1.setRefreshMethod("METHOD");
        expected1.setQuery("QUERY0");
        assertEquals(expected1.toString(), data1.toString());

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
        HashSet datas = (HashSet)map.get(MView.class);
        assertEquals(3, datas.size());

    }

    class GeneratorWrapper extends MViewSnapshotGenerator {
        public DatabaseObject wrapSnapshotObject(DatabaseObject example, DatabaseSnapshot snapshot) throws DatabaseException {
            return super.snapshotObject(example, snapshot);
        }
        public void wrapaddTo(DatabaseObject foundObject, DatabaseSnapshot snapshot) throws DatabaseException, InvalidExampleException {
            super.addTo(foundObject, snapshot);
        }

    }

}
