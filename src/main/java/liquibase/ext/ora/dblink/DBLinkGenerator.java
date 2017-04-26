package liquibase.ext.ora.dblink;

import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;

public abstract class DBLinkGenerator extends AbstractSqlGenerator<DbLinkState> {

    protected enum OperationType {
        CREATE,ALTER,DROP
    }

    public Sql[] generateSql(DbLinkState statement, Database database,
                             SqlGeneratorChain sqlGeneratorChain) {
        StringBuilder sql = new StringBuilder();
        sql
           .append(addIfNotNull(String.format(" USING '%s'", statement.getUsing()), !isEmpty(statement.getUsing())));

        return new Sql[]{new UnparsedSql(sql.toString())};
    }

    protected String makeCommonParts(DbLinkState statement, OperationType type) {
        StringBuilder sb = new StringBuilder();
        switch (type) {
            case CREATE:
                sb.append("CREATE ");
                break;
            case ALTER:
                sb.append("ALTER ");
                break;
            case DROP:
                sb.append("DROP ");
        }
        sb.append(addIfNotNull(DbLinkState.TYPE_SHARED, statement.isSharedType()))
           .append(addIfNotNull(DbLinkState.TYPE_PUBLIC, statement.isPublicType()))
           .append(" DATABASE LINK ")
           .append(statement.getDblinkName());
        return sb.toString();
    }
    protected String makeConnectParts(DbLinkState statement) {
        StringBuilder sb = new StringBuilder();
        sb.append(" CONNECT TO ")
          .append(statement.getUser())
          .append(" IDENTIFIED BY ")
          .append(statement.getPassword());

        return sb.toString();
    }
    protected String makeAuthParts(DbLinkState statement) {
        StringBuilder sb = new StringBuilder();
        sb.append(addIfNotNull(String.format(" AUTHENTICATED BY %s IDENTIFIED BY %s ", statement.getAuthUser(), statement.getAuthPassword())
                , statement.isSharedType()));
        return sb.toString();
    }

    protected String addIfNotNull(String data, boolean option) {
        if(option && !isEmpty(data)) {
            return data;
        }
        return "";
    }

    protected boolean isEmpty(String data) {
        return data == null || data.length() <= 0;
    }

    public boolean supports(DbLinkState statement, Database database) {
        return database instanceof OracleDatabase;
    }

    public ValidationErrors validate(DbLinkState statement,
                                     Database database, SqlGeneratorChain sqlGeneratorChain) {
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.checkRequiredField("dblinkName", statement.getDblinkName());
        validationErrors.checkRequiredField("user", statement.getUser());
        validationErrors.checkRequiredField("password", statement.getPassword());
        if(statement.isSharedType()) {
            validationErrors.checkRequiredField("authUser", statement.getAuthUser());
            validationErrors.checkRequiredField("authPassword", statement.getAuthPassword());
        }
        if(!isEmpty(statement.getType()) && !statement.isSharedType() && !statement.isPublicType()) {
            validationErrors.addError("type must be \"SHARED\" or \"PUBLIC\" or empty");
        }
        return validationErrors;
    }

}
