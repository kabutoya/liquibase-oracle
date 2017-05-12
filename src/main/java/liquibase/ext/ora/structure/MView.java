package liquibase.ext.ora.structure;

import liquibase.structure.AbstractDatabaseObject;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Schema;

public class MView extends AbstractDatabaseObject {
    /*
     * スキーマ
     */
    public MView setSchema(Schema schema) {
        this.setAttribute("schema", schema);
        return this;
    }
    @Override
    public Schema getSchema() {
        return this.getAttribute("schema", Schema.class);
    }
    @Override
    public MView setName(String name) {
        this.setAttribute("name", name);
        return this;
    }
    @Override
    public String getName() {
        // 注意：このメソッドはLiqubaseで「取得したSnapshotが他のSnapshotと同一か」をチェックするためにも使っている。
        // 重複する場合には同じ値とみなされ、genarateやdiffで出力されなくなるため、DabaseObjectをこのクラスからコピペするときには注意。
        return this.getAttribute("name", String.class);
    }
    /*
     * COMPLETE or FORCE or FAST
     */
    public MView setRefreshMethod(String method) {
        this.setAttribute("method", method);
        return this;
    }
    public String getRefreshMethod() {
        return this.getAttribute("method", String.class);
    }
    /*
     * DEMAND or COMMIT
     */
    public MView setMode(String mode) {
        this.setAttribute("mode", mode);
        return this;
    }
    public String getMode() {
        return this.getAttribute("mode", String.class);
    }
    /*
     * ROWID or PRIMARY KEY
     */
    public MView setWith(String with) {
        this.setAttribute("with", with);
        return this;
    }
    public String getWith() {
        return this.getAttribute("with", String.class);
    }
    /*
     * MView Body
     */
    public MView setQuery(String query) {
        this.setAttribute("query", query);
        return this;
    }
    public String getQuery() {
        return this.getAttribute("query", String.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MView that = (MView) o;
        return (this.hashCode() == that.hashCode());
    }

    @Override
    public int hashCode() {
        return toString().toUpperCase().hashCode();
    }

    @Override
    public String toString() {
        return getName()+getQuery();
    }

    @Override
    public DatabaseObject[] getContainingObjects() {
        return null;
    }

}
