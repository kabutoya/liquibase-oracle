package liquibase.ext.ora.dropdblink;

import liquibase.database.Database;
import liquibase.exception.ValidationErrors;
import liquibase.ext.ora.dblink.DBLinkGenerator;
import liquibase.ext.ora.dblink.DbLinkState;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;

public class DropDBLinkGenerator extends DBLinkGenerator {
    public Sql[] generateSql(DbLinkState statement, Database database,
                             SqlGeneratorChain sqlGeneratorChain) {
        StringBuilder sql = new StringBuilder();
        sql.append(makeCommonParts(statement, OperationType.DROP));

        return new Sql[]{new UnparsedSql(sql.toString())};
    }
    public ValidationErrors validate(DbLinkState statement,
                                     Database database, SqlGeneratorChain sqlGeneratorChain) {
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.checkRequiredField("dblinkName", statement.getDblinkName());
        if(!isEmpty(statement.getType()) && !statement.isPublicType()) {
            validationErrors.addError("type must be \"PUBLIC\" or empty");
        }
        return validationErrors;
    }
}
