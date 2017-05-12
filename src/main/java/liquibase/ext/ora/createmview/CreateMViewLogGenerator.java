package liquibase.ext.ora.createmview;

import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.ext.ora.creatematerializedview.CreateMaterializedViewStatement;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;

public class CreateMViewLogGenerator extends AbstractSqlGenerator<CreateMViewLogStatement> {

    public boolean supports(CreateMViewLogStatement createMViewStatement, Database database) {
        return database instanceof OracleDatabase;
    }

    public ValidationErrors validate(CreateMViewLogStatement createMViewStatement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.checkRequiredField("viewName", createMViewStatement.getViewName());
        return validationErrors;
    }

    public Sql[] generateSql(CreateMViewLogStatement statment, Database database, SqlGeneratorChain sqlGeneratorChain) {
        StringBuilder sql = new StringBuilder();

        sql.append("CREATE MATERIALIZED VIEW LOG ON ");
        if (statment.getSchemaName() != null) {
            sql.append(statment.getSchemaName()).append(".");
        }
        sql.append(statment.getViewName()).append(" ");
        if (statment.isHasPK() || statment.isHasRowId() || statment.isHasSequence()) {
            sql.append("WITH ");
            if(statment.isHasRowId()) {
                sql.append("ROWID ");
                sql.append(",");
            }
            if(statment.isHasPK()) {
                sql.append("PRIMARY KEY ");
                sql.append(",");
            }
            if(statment.isHasSequence()) {
                sql.append("SEQUENCE ");
                sql.append(",");
            }
            sql.delete(sql.length()-1, sql.length());
        }
        return new Sql[]{new UnparsedSql(sql.toString())};
    }
}
