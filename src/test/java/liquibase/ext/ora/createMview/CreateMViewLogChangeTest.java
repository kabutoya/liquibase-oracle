package liquibase.ext.ora.createMview;

import liquibase.database.core.OracleDatabase;
import liquibase.ext.ora.createmview.CreateMViewChange;
import liquibase.ext.ora.createmview.CreateMViewLogChange;
import liquibase.ext.ora.createmview.CreateMViewLogStatement;
import liquibase.ext.ora.createmview.CreateMViewStatement;
import liquibase.ext.ora.testing.BaseTestCase;
import liquibase.statement.SqlStatement;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CreateMViewLogChangeTest extends BaseTestCase {
    CreateMViewLogChange change;
    @Before
    public void setUp() {
        change = new CreateMViewLogChange();
        change.setSchemaName("ScmName");
        change.setViewName("ViewName");
        change.setHasRowId("true");
        change.setHasPK("True");
        change.setHasSequence("true");
    }
    @Test
    public void statementRoIdFalse() {
        change.setHasRowId(null);

        SqlStatement[] statements = change.generateStatements(new OracleDatabase());
        assertEquals(1, statements.length);
        CreateMViewLogStatement statement = (CreateMViewLogStatement)statements[0];
        assertEquals("ScmName", statement.getSchemaName());
        assertEquals("ViewName", statement.getViewName());
        assertFalse(statement.isHasRowId());
        assertTrue(statement.isHasPK());
        assertTrue(statement.isHasSequence());
    }
    @Test
    public void statementPKFalse() {
        change.setHasPK("No");

        SqlStatement[] statements = change.generateStatements(new OracleDatabase());
        CreateMViewLogStatement statement = (CreateMViewLogStatement)statements[0];
        assertTrue(statement.isHasRowId());
        assertFalse(statement.isHasPK());
        assertTrue(statement.isHasSequence());
    }
    @Test
    public void statementSequenceFalse() {
        change.setHasSequence("false");

        SqlStatement[] statements = change.generateStatements(new OracleDatabase());
        CreateMViewLogStatement statement = (CreateMViewLogStatement)statements[0];
        assertTrue(statement.isHasRowId());
        assertTrue(statement.isHasPK());
        assertFalse(statement.isHasSequence());
    }

    @Test
    public void getConfirmationMessage() {
        CreateMViewLogChange change = new CreateMViewLogChange();
        change.setSchemaName("ScmName");
        change.setViewName("ViewName");
        assertEquals( "Materialized view Log ViewName has been created", change.getConfirmationMessage());
    }

}
