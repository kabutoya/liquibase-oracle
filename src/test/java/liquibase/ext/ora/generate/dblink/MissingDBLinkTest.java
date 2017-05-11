package liquibase.ext.ora.generate.dblink;

import liquibase.change.Change;
import liquibase.database.core.OracleDatabase;
import liquibase.ext.ora.addcheck.AddCheckChange;
import liquibase.ext.ora.createdblink.CreateDBLinkChange;
import liquibase.ext.ora.output.changelog.MissingCheckChangeGenerator;
import liquibase.ext.ora.output.changelog.MissingDBLinkChangeGenerator;
import liquibase.ext.ora.structure.Check;
import liquibase.ext.ora.structure.DBLink;
import liquibase.ext.ora.structure.Synonym;
import liquibase.ext.ora.testing.BaseTestCase;
import liquibase.structure.core.Schema;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MissingDBLinkTest extends BaseTestCase {
    private MissingDBLinkChangeGenerator generator;
    private Schema schema = new Schema("TEST_SCM", "TEST_SCM");

    int PRIORITY_NONE = -1;
    int PRIORITY_DEFAULT = 1;
    int PRIORITY_DATABASE = 5;
    int PRIORITY_ADDITIONAL = 50;

    @Before
    public void setUp() throws Exception {
        generator = new MissingDBLinkChangeGenerator();
     }

    @Test
    public void getPriorityOtherDBObject() {
        Synonym target = new Synonym();
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
    public void fixChanged() {
        DBLink input = new DBLink();
        input.setSchema(schema);
        input.setType("TYPE");
        input.setDBLinkName("LINK_NAKE");
        input.setUser("USER");
        input.setPassword("PWD");
        input.setUsing("USING");

        Change[] changes = generator.fixMissing(input, null, null, null, null);

        assertEquals("TYPE", ((CreateDBLinkChange)changes[0]).getType());
        assertEquals("LINK_NAKE", ((CreateDBLinkChange)changes[0]).getDblinkName());
        assertEquals("USER", ((CreateDBLinkChange)changes[0]).getUser());
        assertEquals("PWD", ((CreateDBLinkChange)changes[0]).getPassword());
        assertEquals("USING", ((CreateDBLinkChange)changes[0]).getUsing());
    }

}
