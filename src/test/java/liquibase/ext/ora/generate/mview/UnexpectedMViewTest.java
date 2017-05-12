package liquibase.ext.ora.generate.mview;

import liquibase.change.Change;
import liquibase.database.core.OracleDatabase;
import liquibase.ext.ora.dropSynonym.DropSynonymChange;
import liquibase.ext.ora.dropmaterializedview.DropMaterializedViewChange;
import liquibase.ext.ora.output.changelog.UnexpectedMViewChangeGenerator;
import liquibase.ext.ora.output.changelog.UnexpectedSynonymChangeGenerator;
import liquibase.ext.ora.structure.MView;
import liquibase.ext.ora.structure.Synonym;
import liquibase.ext.ora.structure.Trigger;
import liquibase.ext.ora.testing.BaseTestCase;
import liquibase.structure.core.Schema;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnexpectedMViewTest extends BaseTestCase {
    private UnexpectedMViewChangeGenerator generator;
    private Schema schema = new Schema("TEST_SCM", "TEST_SCM");

    int PRIORITY_NONE = -1;
    int PRIORITY_DEFAULT = 1;
    int PRIORITY_DATABASE = 5;
    int PRIORITY_ADDITIONAL = 50;

    @Before
    public void setUp() throws Exception {
        generator = new UnexpectedMViewChangeGenerator();
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

        Change[] changes = generator.fixUnexpected(input, null, null, null, null);

        assertEquals("TEST_SCM", ((DropMaterializedViewChange)changes[0]).getSchemaName());
        assertEquals("SYN_NAME", ((DropMaterializedViewChange)changes[0]).getViewName());
    }

}
