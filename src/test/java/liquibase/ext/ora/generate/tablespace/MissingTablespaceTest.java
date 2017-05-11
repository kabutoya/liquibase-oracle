package liquibase.ext.ora.generate.tablespace;

import liquibase.change.Change;
import liquibase.database.core.OracleDatabase;
import liquibase.ext.ora.altertablespace.AlterTablespaceChange;
import liquibase.ext.ora.output.changelog.MissingTablespaceChangeGenerator;
import liquibase.ext.ora.structure.Synonym;
import liquibase.ext.ora.structure.Tablespace;
import liquibase.ext.ora.testing.BaseTestCase;
import liquibase.structure.core.Schema;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MissingTablespaceTest extends BaseTestCase {
    private MissingTablespaceChangeGenerator generator;
    private Schema schema = new Schema("TEST_SCM", "TEST_SCM");

    int PRIORITY_NONE = -1;
    int PRIORITY_DEFAULT = 1;
    int PRIORITY_DATABASE = 5;
    int PRIORITY_ADDITIONAL = 50;

    @Before
    public void setUp() throws Exception {
        generator = new MissingTablespaceChangeGenerator();
     }

    @Test
    public void getPriorityOtherDBObject() {
        Synonym space = new Synonym();
        int actual = generator.getPriority(space.getClass(), new OracleDatabase());
        assertEquals(PRIORITY_NONE, actual);
    }
    @Test
    public void getPrioritySameDBObject() {
        Tablespace space = new Tablespace();
        int actual = generator.getPriority(space.getClass(), new OracleDatabase());
        assertEquals(PRIORITY_DEFAULT, actual);
    }
    @Test
    public void fixChanged() {
        Tablespace input = new Tablespace();
        input.setSchema(schema);
        input.setObjectName("OBJ_NAME");
        input.setType("TYPE");
        input.setTableSpaceName("SPACE_NAME");

        Change[] changes = generator.fixMissing(input, null, null, null, null);

        assertEquals("TEST_SCM", ((AlterTablespaceChange)changes[0]).getObjectSchemaName());
        assertEquals("OBJ_NAME", ((AlterTablespaceChange)changes[0]).getObjectName());
        assertEquals("TYPE", ((AlterTablespaceChange)changes[0]).getType());
        assertEquals("SPACE_NAME", ((AlterTablespaceChange)changes[0]).getTablespaceName());
    }

}
