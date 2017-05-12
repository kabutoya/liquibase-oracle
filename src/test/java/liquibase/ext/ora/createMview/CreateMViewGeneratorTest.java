package liquibase.ext.ora.createMview;

import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.ext.ora.createmview.CreateMViewChange;
import liquibase.ext.ora.createmview.CreateMViewGenerator;
import liquibase.ext.ora.createmview.CreateMViewStatement;
import liquibase.ext.ora.dropdblink.DropDBLinkChange;
import liquibase.ext.ora.dropdblink.DropDBLinkGenerator;
import liquibase.ext.ora.dropdblink.DropDBLinkStatement;
import liquibase.ext.ora.testing.BaseTestCase;
import liquibase.sql.Sql;
import liquibase.statement.SqlStatement;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CreateMViewGeneratorTest extends BaseTestCase {
    private CreateMViewChange change;
    private Database database = new OracleDatabase();
    @Before
    public void setUp() throws Exception {
        change = new CreateMViewChange();
        change.setViewName("ViewName");
        change.setQuery("Query");
    }

    @Test
    public void createMViewMinimam() {
        SqlStatement[] statements = change.generateStatements(database);
        CreateMViewStatement statement = (CreateMViewStatement)statements[0];
        CreateMViewGenerator generator = new CreateMViewGenerator();
        Sql[] sqls = generator.generateSql(statement, database, null);

        assertEquals("CREATE MATERIALIZED VIEW ViewName AS Query", sqls[0].toSql());
    }
    @Test
    public void createMViewAddSchema() {
        change.setSchemaName("ScmName");
        SqlStatement[] statements = change.generateStatements(database);
        CreateMViewStatement statement = (CreateMViewStatement)statements[0];
        CreateMViewGenerator generator = new CreateMViewGenerator();
        Sql[] sqls = generator.generateSql(statement, database, null);

        assertEquals("CREATE MATERIALIZED VIEW ScmName.ViewName AS Query", sqls[0].toSql());
    }
    @Test
    public void createMViewAddWith() {
        change.setWith("With");
        SqlStatement[] statements = change.generateStatements(database);
        CreateMViewStatement statement = (CreateMViewStatement)statements[0];
        CreateMViewGenerator generator = new CreateMViewGenerator();
        Sql[] sqls = generator.generateSql(statement, database, null);

        assertEquals("CREATE MATERIALIZED VIEW ViewName WITH With AS Query", sqls[0].toSql());
    }
    @Test
    public void createMViewAddModeAndMode() {
        change.setRefreshMethod("Method");
        change.setRefreshMode("Mode");
        SqlStatement[] statements = change.generateStatements(database);
        CreateMViewStatement statement = (CreateMViewStatement)statements[0];
        CreateMViewGenerator generator = new CreateMViewGenerator();
        Sql[] sqls = generator.generateSql(statement, database, null);

        assertEquals("CREATE MATERIALIZED VIEW ViewName REFRESH Method ON Mode AS Query", sqls[0].toSql());
    }
    @Test
    public void validateMinimam() {
        change = new CreateMViewChange();
        SqlStatement[] statements = change.generateStatements(database);
        CreateMViewStatement statement = (CreateMViewStatement)statements[0];
        CreateMViewGenerator generator = new CreateMViewGenerator();

        ValidationErrors errs = generator.validate(statement, database, null);
        List<String> msgs = errs.getErrorMessages();
        assertEquals(2, msgs.size());
        assertEquals("viewName is required", msgs.get(0));
        assertEquals("query is required", msgs.get(1));
    }
    @Test
    public void validateMode() {
        change.setRefreshMethod("Method");
        SqlStatement[] statements = change.generateStatements(database);
        CreateMViewStatement statement = (CreateMViewStatement)statements[0];
        CreateMViewGenerator generator = new CreateMViewGenerator();

        ValidationErrors errs = generator.validate(statement, database, null);
        List<String> msgs = errs.getErrorMessages();
        assertEquals(1, msgs.size());
        assertEquals("refreshMode is required", msgs.get(0));
    }
    @Test
    public void validateMethod() {
        change.setRefreshMode("Mode");
        SqlStatement[] statements = change.generateStatements(database);
        CreateMViewStatement statement = (CreateMViewStatement)statements[0];
        CreateMViewGenerator generator = new CreateMViewGenerator();

        ValidationErrors errs = generator.validate(statement, database, null);
        List<String> msgs = errs.getErrorMessages();
        assertEquals(1, msgs.size());
        assertEquals("refreshMethod is required", msgs.get(0));
    }


}