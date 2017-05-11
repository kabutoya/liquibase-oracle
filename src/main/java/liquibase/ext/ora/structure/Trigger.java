package liquibase.ext.ora.structure;

import liquibase.structure.AbstractDatabaseObject;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Schema;

public class Trigger extends AbstractDatabaseObject {
    /*
     * スキーマ
     */
    public Trigger setSchema(Schema schema) {
        this.setAttribute("schema", schema);
        return this;
    }
    @Override
    public Schema getSchema() {
        return this.getAttribute("schema", Schema.class);
    }
    /*
     * Triggerの名前
     */
    public Trigger setName(String name){
        this.setAttribute("name", name);
        return this;
    }
    @Override
    public String getName() {
        // 注意：このメソッドはLiqubaseで「取得したSnapshotが他のSnapshotと同一か」をチェックするためにも使っている。
        // 重複する場合には同じ値とみなされ、genarateやdiffで出力されなくなるため、TriggerがTrigger名のみで同定されることが保証されていなければならない。
        return this.getAttribute("name", String.class);
    }
    /*
     * Triggerの実態
     */
    public Trigger setTriggerSql(String triggerSql) {
        this.setAttribute("triggerSql", triggerSql);
        return this;
    }
    public String getTriggerSql() {
        return this.getAttribute("triggerSql", String.class);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trigger that = (Trigger) o;
        return (this.hashCode() == that.hashCode());
    }

    @Override
    public int hashCode() {
        return toString().toUpperCase().hashCode();
    }

    @Override
    public String toString() {
        return getTriggerSql();
    }

    @Override
    public DatabaseObject[] getContainingObjects() {
        return null;
    }

}
