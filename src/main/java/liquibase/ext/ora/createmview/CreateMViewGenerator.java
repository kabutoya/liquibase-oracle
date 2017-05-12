package liquibase.ext.ora.createmview;

import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.ext.ora.creatematerializedview.CreateMaterializedViewStatement;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;

public class CreateMViewGenerator extends AbstractSqlGenerator<CreateMViewStatement> {

    public boolean supports(CreateMaterializedViewStatement CreateMViewStatement, Database database) {
        return database instanceof OracleDatabase;
    }

    public ValidationErrors validate(CreateMViewStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.checkRequiredField("viewName", statement.getViewName());
        validationErrors.checkRequiredField("query", statement.getQuery());
        if (statement.getRefreshMode() != null) {
            validationErrors.checkRequiredField("refreshMethod", statement.getRefreshMethod());
        }
        if (statement.getRefreshMethod() != null) {
            validationErrors.checkRequiredField("refreshMode", statement.getRefreshMode());
        }
        return validationErrors;
    }

    public Sql[] generateSql(CreateMViewStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        StringBuilder sql = new StringBuilder();

        sql.append("CREATE MATERIALIZED VIEW ");
        if (statement.getSchemaName() != null) {
            sql.append(statement.getSchemaName()).append(".");
        }
        sql.append(statement.getViewName()).append(" ");
        if (statement.getRefreshMode() != null && statement.getRefreshMethod() != null) {
            sql.append("REFRESH ")
                    .append(statement.getRefreshMethod()).append(" ")
                    .append("ON ")
                    .append(statement.getRefreshMode()).append(" ");
        }
        if (statement.getWith() != null) {
            sql.append("WITH ").append(statement.getWith()).append(" ");
        }
        sql.append("AS ").append(statement.getQuery());
        return new Sql[]{new UnparsedSql(sql.toString())};
    }
}
