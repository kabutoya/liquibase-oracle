package liquibase.ext.ora.dropdblink;

import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;

import static liquibase.ext.ora.dblink.DBLinkGeneratorUtil.*;

public class DropDBLinkGenerator extends AbstractSqlGenerator<DropDBLinkStatement> {
    public Sql[] generateSql(DropDBLinkStatement statement, Database database,
                             SqlGeneratorChain sqlGeneratorChain) {
        StringBuilder sql = new StringBuilder();
        sql.append(makeCommonParts(statement, OperationType.DROP));

        return new Sql[]{new UnparsedSql(sql.toString())};
    }
    public boolean supports(DropDBLinkStatement statement, Database database) {
        return database instanceof OracleDatabase;
    }

    public ValidationErrors validate(DropDBLinkStatement statement,
                                     Database database, SqlGeneratorChain sqlGeneratorChain) {
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.checkRequiredField("dblinkName", statement.getDblinkName());
        if(!isEmpty(statement.getType()) && !statement.isPublicType()) {
            validationErrors.addError("type must be \"PUBLIC\" or empty");
        }
        return validationErrors;
    }
}
