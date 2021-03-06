package liquibase.ext.ora.alterdblink;

import liquibase.database.core.OracleDatabase;
import liquibase.ext.ora.dblink.DbLinkState;
import liquibase.ext.ora.testing.BaseTestCase;
import liquibase.statement.SqlStatement;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 01008716 on 2017/04/26.
 */
public class AlterDBLinkChangeTest extends BaseTestCase {
    @Before
    public void setUp() throws Exception {
        changeLogFile = "liquibase/ext/ora/addcheck/changelog.test.xml";
        connectToDB();
        cleanDB();
    }

    @Test
    public void statementWhenTypeNull() {



        AlterDBLinkChange change = new AlterDBLinkChange();
        //change.setType(null);
        change.setDblinkName("DB-LINK1");
        change.setUser("USER1");
        change.setPassword("PWD1");
        change.setAuthUser("AUSER1");
        change.setAuthPassword("APWD1");
        change.setUsing("USING1");

        SqlStatement[] statements = change.generateStatements(new OracleDatabase());
        assertEquals(1, statements.length);
        DbLinkState statement = (DbLinkState)statements[0];
        assertFalse("TYPE NOT PUBLIC", statement.isPublicType());
        assertFalse("TYPE NOT SHARED", statement.isSharedType());
        assertEquals("DB-LINK1", statement.getDblinkName());
        assertEquals("USER1", statement.getUser());
        assertEquals("PWD1", statement.getPassword());
        assertEquals("AUSER1", statement.getAuthUser());
        assertEquals("APWD1", statement.getAuthPassword());
        assertEquals("USING1", statement.getUsing());

    }

    @Test
    public void statementWhenTypePublic() {
        AlterDBLinkChange change = new AlterDBLinkChange();
        change.setType("PUBLIC");

        SqlStatement[] statements = change.generateStatements(new OracleDatabase());
        assertEquals(1, statements.length);
        DbLinkState statement = (DbLinkState)statements[0];
        assertTrue("TYPE PUBLIC", statement.isPublicType());
        assertFalse("TYPE NOT SHARED", statement.isSharedType());

    }

    @Test
    public void statementWhenTypeShared() {
        AlterDBLinkChange change = new AlterDBLinkChange();
        change.setType("SHARED");

        SqlStatement[] statements = change.generateStatements(new OracleDatabase());
        assertEquals(1, statements.length);
        DbLinkState statement = (DbLinkState)statements[0];
        assertFalse("TYPE NOT PUBLIC", statement.isPublicType());
        assertTrue("TYPE SHARED", statement.isSharedType());

    }

    @Test
    public void getConfirmationMessage() {
        AlterDBLinkChange change = new AlterDBLinkChange();

        change.setDblinkName("LINK_NAME");
        change.setUser("USER2");
        assertEquals( "Alter DB-Link LINK_NAME connect to USER2", change.getConfirmationMessage());
    }

}
