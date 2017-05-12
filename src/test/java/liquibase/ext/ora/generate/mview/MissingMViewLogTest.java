package liquibase.ext.ora.generate.mview;

import liquibase.change.Change;
import liquibase.database.core.OracleDatabase;
import liquibase.ext.ora.createmview.CreateMViewChange;
import liquibase.ext.ora.createmview.CreateMViewLogChange;
import liquibase.ext.ora.output.changelog.MissingMViewChangeGenerator;
import liquibase.ext.ora.output.changelog.MissingMViewLogChangeGenerator;
import liquibase.ext.ora.structure.MView;
import liquibase.ext.ora.structure.MViewLog;
import liquibase.ext.ora.structure.Trigger;
import liquibase.ext.ora.testing.BaseTestCase;
import liquibase.structure.core.Schema;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MissingMViewLogTest extends BaseTestCase {
    private MissingMViewLogChangeGenerator generator;
    private Schema schema = new Schema("TEST_SCM", "TEST_SCM");

    int PRIORITY_NONE = -1;
    int PRIORITY_DEFAULT = 1;
    int PRIORITY_DATABASE = 5;
    int PRIORITY_ADDITIONAL = 50;

    @Before
    public void setUp() throws Exception {
        generator = new MissingMViewLogChangeGenerator();
     }

    @Test
    public void getPriorityOtherDBObject() {
        Trigger target = new Trigger();
        int actual = generator.getPriority(target.getClass(), new OracleDatabase());
        assertEquals(PRIORITY_NONE, actual);
    }
    @Test
    public void getPrioritySameDBObject() {
        MViewLog target = new MViewLog();
        int actual = generator.getPriority(target.getClass(), new OracleDatabase());
        assertEquals(PRIORITY_DEFAULT, actual);
    }
    @Test
    public void fixChanged() {
        MViewLog input = new MViewLog();
        input.setSchema(schema);
        input.setName("SYN_NAME");
        input.setHasSequence(true);
//        input.setHasRowId(false); 初期値はfalseのはず。
        input.setHasPK(true);

        Change[] changes = generator.fixMissing(input, null, null, null, null);

        assertEquals("TEST_SCM", ((CreateMViewLogChange)changes[0]).getSchemaName());
        assertEquals("SYN_NAME", ((CreateMViewLogChange)changes[0]).getViewName());
        assertEquals("true", ((CreateMViewLogChange)changes[0]).getHasPK());
        assertEquals("false", ((CreateMViewLogChange)changes[0]).getHasRowId());
        assertEquals("true", ((CreateMViewLogChange)changes[0]).getHasSequence());
    }

}
