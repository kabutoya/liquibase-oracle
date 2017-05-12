package liquibase.ext.ora.generate.mview;

import liquibase.change.Change;
import liquibase.database.core.OracleDatabase;
import liquibase.ext.ora.createmview.CreateMViewChange;
import liquibase.ext.ora.output.changelog.MissingMViewChangeGenerator;
import liquibase.ext.ora.structure.MView;
import liquibase.ext.ora.structure.Trigger;
import liquibase.ext.ora.testing.BaseTestCase;
import liquibase.structure.core.Schema;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MissingMViewTest extends BaseTestCase {
    private MissingMViewChangeGenerator generator;
    private Schema schema = new Schema("TEST_SCM", "TEST_SCM");

    int PRIORITY_NONE = -1;
    int PRIORITY_DEFAULT = 1;
    int PRIORITY_DATABASE = 5;
    int PRIORITY_ADDITIONAL = 50;

    @Before
    public void setUp() throws Exception {
        generator = new MissingMViewChangeGenerator();
     }

    @Test
    public void getPriorityOtherDBObject() {
        Trigger target = new Trigger();
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
    public void fixChanged() {
        MView input = new MView();
        input.setSchema(schema);
        input.setName("SYN_NAME");
        input.setRefreshMethod("METH");
        input.setMode("MODE");
        input.setQuery("QUERY");
        input.setWith("WITH");

        Change[] changes = generator.fixMissing(input, null, null, null, null);

        assertEquals("TEST_SCM", ((CreateMViewChange)changes[0]).getSchemaName());
        assertEquals("SYN_NAME", ((CreateMViewChange)changes[0]).getViewName());
        assertEquals("METH", ((CreateMViewChange)changes[0]).getRefreshMethod());
        assertEquals("MODE", ((CreateMViewChange)changes[0]).getRefreshMode());
        assertEquals("QUERY", ((CreateMViewChange)changes[0]).getQuery());
        assertEquals("WITH", ((CreateMViewChange)changes[0]).getWith());
    }

}
