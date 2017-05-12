package liquibase.ext.ora.createMview;

import liquibase.change.Change;
import liquibase.database.core.OracleDatabase;
import liquibase.ext.ora.createmview.CreateMViewChange;
import liquibase.ext.ora.createmview.CreateMViewStatement;
import liquibase.ext.ora.dblink.DbLinkState;
import liquibase.ext.ora.dropdblink.DropDBLinkChange;
import liquibase.ext.ora.testing.BaseTestCase;
import liquibase.statement.SqlStatement;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreateMViewChangeTest extends BaseTestCase {
    @Test
    public void statementWhenTypeNull() {


        CreateMViewChange change = new CreateMViewChange();
        change.setSchemaName("ScmName");
        change.setViewName("ViewName");
        change.setRefreshMode("Mode");
        change.setRefreshMethod("Method");
        change.setWith("With");
        change.setQuery("Query");

        SqlStatement[] statements = change.generateStatements(new OracleDatabase());
        assertEquals(1, statements.length);
        CreateMViewStatement statement = (CreateMViewStatement)statements[0];
        assertEquals("ScmName", statement.getSchemaName());
        assertEquals("ViewName", statement.getViewName());
        assertEquals("Mode", statement.getRefreshMode());
        assertEquals("Method", statement.getRefreshMethod());
        assertEquals("With", statement.getWith());
        assertEquals("Query", statement.getQuery());

    }

    @Test
    public void getConfirmationMessage() {
        CreateMViewChange change = new CreateMViewChange();
        change.setSchemaName("ScmName");
        change.setViewName("ViewName");
        assertEquals( "Materialized view ViewName has been created", change.getConfirmationMessage());
    }

}
