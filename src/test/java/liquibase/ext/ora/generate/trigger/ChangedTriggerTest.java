package liquibase.ext.ora.generate.trigger;

import liquibase.change.Change;
import liquibase.database.core.OracleDatabase;
import liquibase.ext.ora.output.changelog.ChangedTriggerChangeGenerator;
import liquibase.ext.ora.structure.Synonym;
import liquibase.ext.ora.structure.Trigger;
import liquibase.ext.ora.testing.BaseTestCase;
import liquibase.ext.ora.trriger.TriggerChange;
import liquibase.structure.core.Schema;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChangedTriggerTest extends BaseTestCase {
    private ChangedTriggerChangeGenerator generator;
    private Schema schema = new Schema("TEST_SCM", "TEST_SCM");

    int PRIORITY_NONE = -1;
    int PRIORITY_DEFAULT = 1;
    int PRIORITY_DATABASE = 5;
    int PRIORITY_ADDITIONAL = 50;

    @Before
    public void setUp() throws Exception {
        generator = new ChangedTriggerChangeGenerator();
     }

    @Test
    public void getPriorityOtherDBObject() {
        Synonym trigger = new Synonym();
        int actual = generator.getPriority(trigger.getClass(), new OracleDatabase());
        assertEquals(PRIORITY_NONE, actual);
    }
    @Test
    public void getPrioritySameDBObject() {
        Trigger trigger = new Trigger();
        int actual = generator.getPriority(trigger.getClass(), new OracleDatabase());
        assertEquals(PRIORITY_DEFAULT, actual);
    }
    @Test
    public void fixChanged() {
        Trigger input = new Trigger();
        input.setSchema(schema);
        input.setName("NAME0");
        input.setTriggerSql("SQL0");

        Change[] changes = generator.fixChanged(input, null, null, null, null, null);

        assertEquals("TEST_SCM", ((TriggerChange)changes[0]).getSchemaName());
        assertEquals("NAME0", ((TriggerChange)changes[0]).getTriggerName());
        assertEquals("SQL0", ((TriggerChange)changes[0]).getTriggerSql());
    }

}
