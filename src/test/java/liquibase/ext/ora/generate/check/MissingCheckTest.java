package liquibase.ext.ora.generate.check;

import liquibase.change.Change;
import liquibase.database.core.OracleDatabase;
import liquibase.ext.ora.addcheck.AddCheckChange;
import liquibase.ext.ora.output.changelog.MissingCheckChangeGenerator;
import liquibase.ext.ora.structure.Check;
import liquibase.ext.ora.structure.Synonym;
import liquibase.ext.ora.testing.BaseTestCase;
import liquibase.structure.core.Schema;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MissingCheckTest extends BaseTestCase {
    private MissingCheckChangeGenerator generator;
    private Schema schema = new Schema("TEST_SCM", "TEST_SCM");

    int PRIORITY_NONE = -1;
    int PRIORITY_DEFAULT = 1;
    int PRIORITY_DATABASE = 5;
    int PRIORITY_ADDITIONAL = 50;

    @Before
    public void setUp() throws Exception {
        generator = new MissingCheckChangeGenerator();
     }

    @Test
    public void getPriorityOtherDBObject() {
        Synonym target = new Synonym();
        int actual = generator.getPriority(target.getClass(), new OracleDatabase());
        assertEquals(PRIORITY_NONE, actual);
    }
    @Test
    public void getPrioritySameDBObject() {
        Check target = new Check();
        int actual = generator.getPriority(target.getClass(), new OracleDatabase());
        assertEquals(PRIORITY_DEFAULT, actual);
    }
    @Test
    public void fixChanged() {
        Check input = new Check();
        input.setSchema(schema);
        input.setConstraintName("CHK_NAME");
        input.setTableName("TBL_NAME");
        input.setCondition("CONDITION");

        Change[] changes = generator.fixMissing(input, null, null, null, null);

        assertEquals("TEST_SCM", ((AddCheckChange)changes[0]).getSchemaName());
        assertEquals("CHK_NAME", ((AddCheckChange)changes[0]).getConstraintName());
        assertEquals("TBL_NAME", ((AddCheckChange)changes[0]).getTableName());
        assertEquals("CONDITION", ((AddCheckChange)changes[0]).getCondition());
    }

}
