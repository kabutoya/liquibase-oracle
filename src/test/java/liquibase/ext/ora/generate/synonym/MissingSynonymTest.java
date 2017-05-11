package liquibase.ext.ora.generate.synonym;

import liquibase.change.Change;
import liquibase.database.core.OracleDatabase;
import liquibase.ext.ora.createSynonym.CreateSynonymChange;
import liquibase.ext.ora.output.changelog.ChangedSynonymChangeGenerator;
import liquibase.ext.ora.output.changelog.MissingSynonymChangeGenerator;
import liquibase.ext.ora.structure.Synonym;
import liquibase.ext.ora.structure.Trigger;
import liquibase.ext.ora.testing.BaseTestCase;
import liquibase.structure.core.Schema;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MissingSynonymTest extends BaseTestCase {
    private MissingSynonymChangeGenerator generator;
    private Schema schema = new Schema("TEST_SCM", "TEST_SCM");

    int PRIORITY_NONE = -1;
    int PRIORITY_DEFAULT = 1;
    int PRIORITY_DATABASE = 5;
    int PRIORITY_ADDITIONAL = 50;

    @Before
    public void setUp() throws Exception {
        generator = new MissingSynonymChangeGenerator();
     }

    @Test
    public void getPriorityOtherDBObject() {
        Trigger target = new Trigger();
        int actual = generator.getPriority(target.getClass(), new OracleDatabase());
        assertEquals(PRIORITY_NONE, actual);
    }
    @Test
    public void getPrioritySameDBObject() {
        Synonym target = new Synonym();
        int actual = generator.getPriority(target.getClass(), new OracleDatabase());
        assertEquals(PRIORITY_DEFAULT, actual);
    }
    @Test
    public void fixChanged() {
        Synonym input = new Synonym();
        input.setSchema(schema);
        input.setName("SYN_NAME");
        input.setSrcName("SRC_NAME");
        input.setSrcSchemaName("SRC_SCM_NAME");

        Change[] changes = generator.fixMissing(input, null, null, null, null);

        assertEquals("SRC_NAME", ((CreateSynonymChange)changes[0]).getObjectName());
        assertEquals("SRC_SCM_NAME", ((CreateSynonymChange)changes[0]).getObjectSchemaName());
        assertEquals("SYN_NAME", ((CreateSynonymChange)changes[0]).getSynonymName());
        assertEquals("TEST_SCM", ((CreateSynonymChange)changes[0]).getSynonymSchemaName());
    }

}
