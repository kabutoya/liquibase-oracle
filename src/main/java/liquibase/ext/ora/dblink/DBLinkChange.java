package liquibase.ext.ora.dblink;

import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.statement.SqlStatement;


public abstract class DBLinkChange extends DbLinkAttribute {

    public SqlStatement[] generateStatements(Database database) {
        DbLinkState statement = new DbLinkState();
        statement.setType(getType());
        statement.setDblinkName(getDblinkName());
        statement.setUser(getUser());
        statement.setPassword(getPassword());
        statement.setAuthUser(getAuthUser());
        statement.setAuthPassword(getAuthPassword());
        statement.setUsing(getUsing());

        return new SqlStatement[]{statement};
    }

}