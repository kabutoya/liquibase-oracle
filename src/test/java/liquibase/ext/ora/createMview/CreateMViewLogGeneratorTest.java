package liquibase.ext.ora.createMview;

import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.ext.ora.createmview.CreateMViewLogChange;
import liquibase.ext.ora.createmview.CreateMViewLogGenerator;
import liquibase.ext.ora.createmview.CreateMViewLogStatement;
import liquibase.ext.ora.testing.BaseTestCase;
import liquibase.sql.Sql;
import liquibase.statement.SqlStatement;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CreateMViewLogGeneratorTest extends BaseTestCase {
    private CreateMViewLogChange change;
    private Database database = new OracleDatabase();
    @Before
    public void setUp() throws Exception {
        change = new CreateMViewLogChange();
        change.setViewName("ViewName");
    }

    @Test
    public void createMViewMinimam() {
        SqlStatement[] statements = change.generateStatements(database);
        CreateMViewLogStatement statement = (CreateMViewLogStatement)statements[0];
        CreateMViewLogGenerator generator = new CreateMViewLogGenerator();
        Sql[] sqls = generator.generateSql(statement, database, null);

        assertEquals("CREATE MATERIALIZED VIEW LOG ON ViewName", sqls[0].toSql());
    }
    @Test
    public void createMViewAddSchema() {
        change.setSchemaName("ScmName");
        SqlStatement[] statements = change.generateStatements(database);
        CreateMViewLogStatement statement = (CreateMViewLogStatement)statements[0];
        CreateMViewLogGenerator generator = new CreateMViewLogGenerator();
        Sql[] sqls = generator.generateSql(statement, database, null);

        assertEquals("CREATE MATERIALIZED VIEW LOG ON ScmName.ViewName", sqls[0].toSql());
    }
    @Test
    public void createMViewAddWithOne() {
        change.setHasRowId("true");
        SqlStatement[] statements = change.generateStatements(database);
        CreateMViewLogStatement statement = (CreateMViewLogStatement)statements[0];
        CreateMViewLogGenerator generator = new CreateMViewLogGenerator();
        Sql[] sqls = generator.generateSql(statement, database, null);

        assertEquals("CREATE MATERIALIZED VIEW LOG ON ViewName WITH ROWID", sqls[0].toSql());
    }
    @Test
    public void createMViewAddWithTwo1() {
        change.setHasPK("true");
        change.setHasSequence("true");
        SqlStatement[] statements = change.generateStatements(database);
        CreateMViewLogStatement statement = (CreateMViewLogStatement)statements[0];
        CreateMViewLogGenerator generator = new CreateMViewLogGenerator();
        Sql[] sqls = generator.generateSql(statement, database, null);

        assertEquals("CREATE MATERIALIZED VIEW LOG ON ViewName WITH PRIMARY KEY ,SEQUENCE", sqls[0].toSql());
    }
    @Test
    public void createMViewAddWithTwo2() {
        change.setHasRowId("true");
        change.setHasSequence("true");
        SqlStatement[] statements = change.generateStatements(database);
        CreateMViewLogStatement statement = (CreateMViewLogStatement)statements[0];
        CreateMViewLogGenerator generator = new CreateMViewLogGenerator();
        Sql[] sqls = generator.generateSql(statement, database, null);

        assertEquals("CREATE MATERIALIZED VIEW LOG ON ViewName WITH ROWID ,SEQUENCE", sqls[0].toSql());
    }
    @Test
    public void createMViewAddWithTwo3() {
        change.setHasRowId("true");
        change.setHasPK("true");
        SqlStatement[] statements = change.generateStatements(database);
        CreateMViewLogStatement statement = (CreateMViewLogStatement)statements[0];
        CreateMViewLogGenerator generator = new CreateMViewLogGenerator();
        Sql[] sqls = generator.generateSql(statement, database, null);

        assertEquals("CREATE MATERIALIZED VIEW LOG ON ViewName WITH ROWID ,PRIMARY KEY", sqls[0].toSql());
    }
    @Test
    public void createMViewAddWithThree() {
        change.setHasRowId("true");
        change.setHasSequence("true");
        change.setHasPK("true");
        SqlStatement[] statements = change.generateStatements(database);
        CreateMViewLogStatement statement = (CreateMViewLogStatement)statements[0];
        CreateMViewLogGenerator generator = new CreateMViewLogGenerator();
        Sql[] sqls = generator.generateSql(statement, database, null);

        assertEquals("CREATE MATERIALIZED VIEW LOG ON ViewName WITH ROWID ,PRIMARY KEY ,SEQUENCE", sqls[0].toSql());
    }

    @Test
    public void validateMethod() {
        change = new CreateMViewLogChange();
        SqlStatement[] statements = change.generateStatements(database);
        CreateMViewLogStatement statement = (CreateMViewLogStatement)statements[0];
        CreateMViewLogGenerator generator = new CreateMViewLogGenerator();

        ValidationErrors errs = generator.validate(statement, database, null);
        List<String> msgs = errs.getErrorMessages();
        assertEquals(1, msgs.size());
        assertEquals("viewName is required", msgs.get(0));
    }


}