package liquibase.ext.ora.alterdblink;

import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;

import static liquibase.ext.ora.dblink.DBLinkGeneratorUtil.*;

public class AlterDBLinkGenerator  extends AbstractSqlGenerator<AlterDBLinkStatement> {
    public Sql[] generateSql(AlterDBLinkStatement statement, Database database,
                             SqlGeneratorChain sqlGeneratorChain) {
        StringBuilder sql = new StringBuilder();
        sql.append(makeCommonParts(statement, OperationType.ALTER))
                .append(makeConnectParts(statement))
                .append(makeAuthParts(statement));

        return new Sql[]{new UnparsedSql(sql.toString())};
    }

    public boolean supports(AlterDBLinkStatement statement, Database database) {
        return database instanceof OracleDatabase;
    }

    public ValidationErrors validate(AlterDBLinkStatement statement,
                                     Database database, SqlGeneratorChain sqlGeneratorChain) {
        return makeValidate(statement, null);
    }}
