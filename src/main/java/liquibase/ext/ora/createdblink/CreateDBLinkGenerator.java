package liquibase.ext.ora.createdblink;

import liquibase.database.Database;
import liquibase.ext.ora.dblink.DBLinkGenerator;
import liquibase.ext.ora.dblink.DbLinkState;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;

public class CreateDBLinkGenerator extends DBLinkGenerator {
    public Sql[] generateSql(DbLinkState statement, Database database,
                             SqlGeneratorChain sqlGeneratorChain) {
        StringBuilder sql = new StringBuilder();
        sql.append(makeCommonParts(statement, OperationType.CREATE))
           .append(makeConnectParts(statement))
           .append(makeAuthParts(statement))
           .append(addIfNotNull(String.format(" USING '%s'", statement.getUsing()), !isEmpty(statement.getUsing())));

        return new Sql[]{new UnparsedSql(sql.toString())};
    }
}
