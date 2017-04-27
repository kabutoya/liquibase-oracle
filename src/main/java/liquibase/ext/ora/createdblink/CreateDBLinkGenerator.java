package liquibase.ext.ora.createdblink;

import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.ValidationErrors;
import static liquibase.ext.ora.dblink.DBLinkGeneratorUtil.*;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;

public class CreateDBLinkGenerator extends AbstractSqlGenerator<CreateDBLinkStatement> {
    public Sql[] generateSql(CreateDBLinkStatement statement, Database database,
                             SqlGeneratorChain sqlGeneratorChain) {
        StringBuilder sql = new StringBuilder();
        sql.append(makeCommonParts(statement, OperationType.CREATE))
           .append(makeConnectParts(statement))
           .append(makeAuthParts(statement))
           .append(addIfNotNull(String.format(" USING '%s'", statement.getUsing()), !isEmpty(statement.getUsing())));

        return new Sql[]{new UnparsedSql(sql.toString())};
    }

    public boolean supports(CreateDBLinkStatement statement, Database database) {
        return database instanceof OracleDatabase;
    }

    public ValidationErrors validate(CreateDBLinkStatement statement,
                                     Database database, SqlGeneratorChain sqlGeneratorChain) {
        return makeValidate(statement, null);
    }

}
