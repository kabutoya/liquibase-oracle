package liquibase.ext.ora.dropdblink;

import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.ext.ora.testing.BaseTestCase;
import liquibase.sql.Sql;
import liquibase.statement.SqlStatement;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class DropDBLinkGeneratorTest extends BaseTestCase {
    private DropDBLinkChange change;
    private Database database = new OracleDatabase();
    @Before
    public void setUp() throws Exception {
        changeLogFile = "liquibase/ext/ora/addcheck/changelog.test.xml";
        connectToDB();
        cleanDB();

        change = new DropDBLinkChange();
        //change.setType(null);
        change.setDblinkName("MY_LINK1");
        change.setUser("USER1");
        change.setPassword("PWD1");
        change.setUsing("USING1");
    }

    @Test
    public void checkLocalDBLinkSQL() {
        SqlStatement[] statements = change.generateStatements(database);
        DropDBLinkStatement statement = (DropDBLinkStatement)statements[0];
        DropDBLinkGenerator generator = new DropDBLinkGenerator();
        Sql[] sqls = generator.generateSql(statement, database, null);

        assertEquals("DROP  DATABASE LINK MY_LINK1", sqls[0].toSql());
    }
    @Test
    public void checkLocalDBLinkSQLRequiredERR() {
        change.setDblinkName(null);
        change.setUser(null);  //USER allow NULL
        change.setPassword(null);  //PASSWORD allow NULL
        change.setUsing(null);  //USING allow NULL
        SqlStatement[] statements = change.generateStatements(database);
        DropDBLinkStatement statement = (DropDBLinkStatement)statements[0];
        DropDBLinkGenerator generator = new DropDBLinkGenerator();

        ValidationErrors errs = generator.validate(statement, database, null);
        List<String> msgs = errs.getRequiredErrorMessages();
        assertEquals(1, msgs.size());
    }
    @Test
    public void checkPublicDBLinkSQL() {
        change.setType("PUBLIC");
        SqlStatement[] statements = change.generateStatements(database);
        DropDBLinkStatement statement = (DropDBLinkStatement)statements[0];
        DropDBLinkGenerator generator = new DropDBLinkGenerator();
        Sql[] sqls = generator.generateSql(statement, database, null);

        assertEquals("DROP PUBLIC DATABASE LINK MY_LINK1", sqls[0].toSql());
    }
    @Test
    public void checkSharedDBLinkSQLNonSupportedTypeERR() {
        change.setType("HOGE");
        SqlStatement[] statements = change.generateStatements(database);
        DropDBLinkStatement statement = (DropDBLinkStatement)statements[0];
        DropDBLinkGenerator generator = new DropDBLinkGenerator();

        ValidationErrors errs = generator.validate(statement, database, null);
        List<String> msgs = errs.getErrorMessages();
        assertEquals("type must be \"PUBLIC\" or empty", msgs.get(0));
    }


}