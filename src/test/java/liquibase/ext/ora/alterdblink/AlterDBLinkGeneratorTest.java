package liquibase.ext.ora.alterdblink;

import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.ext.ora.dblink.DbLinkState;
import liquibase.ext.ora.testing.BaseTestCase;
import liquibase.sql.Sql;
import liquibase.statement.SqlStatement;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by 01008716 on 2017/04/26.
 */
public class AlterDBLinkGeneratorTest extends BaseTestCase {
    private AlterDBLinkChange change;
    private Database database = new OracleDatabase();
    @Before
    public void setUp() throws Exception {
        changeLogFile = "liquibase/ext/ora/addcheck/changelog.test.xml";
        connectToDB();
        cleanDB();

        change = new AlterDBLinkChange();
        //change.setType(null);
        change.setDblinkName("MY_LINK1");
        change.setUser("USER1");
        change.setPassword("PWD1");
        change.setUsing("USING1");
    }

    @Test
    public void checkLocalDBLinkSQL() {
        SqlStatement[] statements = change.generateStatements(database);
        DbLinkState statement = (DbLinkState)statements[0];
        AlterDBLinkGenerator generator = new AlterDBLinkGenerator();
        Sql[] sqls = generator.generateSql(statement, database, null);

        assertEquals("ALTER  DATABASE LINK MY_LINK1 CONNECT TO USER1 IDENTIFIED BY PWD1", sqls[0].toSql());
    }
    @Test
    public void checkLocalDBLinkSQLRequiredERR() {
        change.setDblinkName(null);
        change.setUser(null);
        change.setPassword(null);
        change.setUsing(null);  //USING allow NULL
        SqlStatement[] statements = change.generateStatements(database);
        DbLinkState statement = (DbLinkState)statements[0];
        AlterDBLinkGenerator generator = new AlterDBLinkGenerator();

        ValidationErrors errs = generator.validate(statement, database, null);
        List<String> msgs = errs.getRequiredErrorMessages();
        assertEquals(3, msgs.size());
    }
    @Test
    public void checkPublicDBLinkSQL() {
        change.setType("PUBLIC");
        SqlStatement[] statements = change.generateStatements(database);
        DbLinkState statement = (DbLinkState)statements[0];
        AlterDBLinkGenerator generator = new AlterDBLinkGenerator();
        Sql[] sqls = generator.generateSql(statement, database, null);

        assertEquals("ALTER PUBLIC DATABASE LINK MY_LINK1 CONNECT TO USER1 IDENTIFIED BY PWD1", sqls[0].toSql());

//        assertEquals("CREATE  DATABASE LINK MY_LINK1 CONNECT TO USER1 IDENTIFIED BY PWD1 AUTHENTICATED BY test IDENTIFIED BY test  USING 'xe';", sqls[1].toSql());
    }
    @Test
    public void checkSharedDBLinkSQL() {
        change.setType("SHARED");
        change.setAuthUser("AUSER1");
        change.setAuthPassword("APWD1");
        SqlStatement[] statements = change.generateStatements(database);
        DbLinkState statement = (DbLinkState)statements[0];
        AlterDBLinkGenerator generator = new AlterDBLinkGenerator();
        Sql[] sqls = generator.generateSql(statement, database, null);

        assertEquals("ALTER SHARED DATABASE LINK MY_LINK1 CONNECT TO USER1 IDENTIFIED BY PWD1 AUTHENTICATED BY AUSER1 IDENTIFIED BY APWD1", sqls[0].toSql());
    }
    @Test
    public void checkSharedDBLinkSQLRequiredERR() {
        change.setType("SHARED");
        change.setAuthUser(null);
        change.setAuthPassword(null);
        SqlStatement[] statements = change.generateStatements(database);
        DbLinkState statement = (DbLinkState)statements[0];
        AlterDBLinkGenerator generator = new AlterDBLinkGenerator();
        Sql[] sqls = generator.generateSql(statement, database, null);

        ValidationErrors errs = generator.validate(statement, database, null);
        List<String> msgs = errs.getRequiredErrorMessages();
        assertEquals(2, msgs.size());
    }
    @Test
    public void checkSharedDBLinkSQLNonSupportedTypeERR() {
        change.setType("HOGE");
        SqlStatement[] statements = change.generateStatements(database);
        DbLinkState statement = (DbLinkState)statements[0];
        AlterDBLinkGenerator generator = new AlterDBLinkGenerator();
        Sql[] sqls = generator.generateSql(statement, database, null);

        ValidationErrors errs = generator.validate(statement, database, null);
        List<String> msgs = errs.getErrorMessages();
        assertEquals("type must be \"SHARED\" or \"PUBLIC\" or empty", msgs.get(0));
    }


}