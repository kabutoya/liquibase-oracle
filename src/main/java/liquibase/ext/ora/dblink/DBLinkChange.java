package liquibase.ext.ora.dblink;

import liquibase.statement.SqlStatement;


public abstract class DBLinkChange extends DbLinkAttribute {

    protected <T extends DbLinkState>SqlStatement[] constnstructStatment(T statement) {
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