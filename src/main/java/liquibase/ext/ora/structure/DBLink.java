package liquibase.ext.ora.structure;

import liquibase.structure.AbstractDatabaseObject;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Schema;

public class DBLink extends AbstractDatabaseObject {
    private String compareName;
    /*
     * スキーマ
     */
    public DBLink setSchema(Schema schema) {
        this.setAttribute("schema", schema);
        return this;
    }
    @Override
    public Schema getSchema() {
        return this.getAttribute("schema", Schema.class);
    }
    @Override
    public DBLink setName(String name) {
        // Liqubaseがこのメソッドを予約しているため、利用できない。(TableSpaceの場合、nameを利用するとすべての値を重複とみなしてしまう。)
        this.compareName = name;
        return this;
    }
    @Override
    public String getName() {
        // Liqubaseがこのメソッドを予約しているため、利用できない。(TableSpaceの場合、nameを利用するとすべての値を重複とみなしてしまう。)
        return toString();
    }
    /*
     * Public or Private
     */
    public DBLink setType(String type) {
        this.setAttribute("type", type);
        return this;
    }
    public String getType() {
        return this.getAttribute("type", String.class);
    }
    /*
     * DB-Link名
     */
    public DBLink setDBLinkName(String name) {
        this.setAttribute("name", name);
        return this;
    }
    public String getDBLinkName() {
        return this.getAttribute("name", String.class);
    }
    /*
     * 接続先ID/PWD
     */
    public DBLink setUser(String name) {
        this.setAttribute("user", name);
        return this;
    }
    public String getUser() {
        return this.getAttribute("user", String.class);
    }
    public DBLink setPassword(String name) {
        this.setAttribute("password", name);
        return this;
    }
    public String getPassword() {
        return this.getAttribute("password", String.class);
    }
    /*
     * 認証ID/PWD
     */
    public DBLink setAuthUser(String name) {
        this.setAttribute("authUser", name);
        return this;
    }
    public String getAuthUser() {
        return this.getAttribute("authUser", String.class);
    }
    public DBLink setAuthPassword(String name) {
        this.setAttribute("authPassword", name);
        return this;
    }
    public String getAuthPassword() {
        return this.getAttribute("authPassword", String.class);
    }
    /*
     * 接続サービス名
     */
    public DBLink setUsing(String name) {
        this.setAttribute("using", name);
        return this;
    }
    public String getUsing() {
        return this.getAttribute("using", String.class);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DBLink that = (DBLink) o;
        return (this.hashCode() == that.hashCode());
    }

    @Override
    public int hashCode() {
        return toString().toUpperCase().hashCode();
    }

    @Override
    public String toString() {
        return getDBLinkName();
    }

    @Override
    public DatabaseObject[] getContainingObjects() {
        return null;
    }

}
