package liquibase.ext.ora.dblink;

import liquibase.statement.AbstractSqlStatement;

public class DbLinkState extends AbstractSqlStatement {
    public static final String TYPE_SHARED = "SHARED";
    public static final String TYPE_PUBLIC = "PUBLIC";
    public static final String TYPE_PRIVATE = "PRIVATE";

    private String type;     // SHARED, PUBLIC
    private String dblinkName;
    private String user;
    private String password;
    private String authUser;
    private String authPassword;
    private String using;

    public boolean isPrivateType () {
        return TYPE_PRIVATE.equalsIgnoreCase(this.type);
    }
    public boolean isSharedType () {
        return TYPE_SHARED.equalsIgnoreCase(this.type);
    }
    public boolean isPublicType() {return TYPE_PUBLIC.equalsIgnoreCase(this.type);}

    public void setType(String tableName) {
        this.type = tableName;
    }

    public String getType() {
        return type;
    }

    public void setDblinkName(String dblinkName) {
        this.dblinkName = dblinkName;
    }

    public String getDblinkName() {
        return dblinkName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthUser() {
        return authUser;
    }

    public void setAuthUser(String authUser) {
        this.authUser = authUser;
    }

    public String getAuthPassword() {
        return authPassword;
    }

    public void setAuthPassword(String authPassword) {
        this.authPassword = authPassword;
    }

    public String getUsing() {
        return using;
    }

    public void setUsing(String use) {
        this.using = use;
    }

}
