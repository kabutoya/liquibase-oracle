package liquibase.ext.ora.alterdblink;

import liquibase.database.Database;
import liquibase.ext.ora.dblink.DBLinkGenerator;
import liquibase.ext.ora.dblink.DbLinkState;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;

public class AlterDBLinkGenerator extends DBLinkGenerator {
    public Sql[] generateSql(DbLinkState statement, Database database,
                             SqlGeneratorChain sqlGeneratorChain) {
        StringBuilder sql = new StringBuilder();
        sql.append(makeCommonParts(statement, OperationType.ALTER))
           .append(makeConnectParts(statement))
           .append(makeAuthParts(statement));

        return new Sql[]{new UnparsedSql(sql.toString())};
    }
}
