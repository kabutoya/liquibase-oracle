package liquibase.ext.ora.structure;

import liquibase.structure.AbstractDatabaseObject;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Schema;

public class MViewLog extends AbstractDatabaseObject {
    /*
     * スキーマ
     */
    public MViewLog setSchema(Schema schema) {
        this.setAttribute("schema", schema);
        return this;
    }
    @Override
    public Schema getSchema() {
        return this.getAttribute("schema", Schema.class);
    }
    @Override
    public MViewLog setName(String name) {
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
     * WITHにROWIDを指定するか
     */
    public MViewLog setHasRowId(Boolean rowId) {
        this.setAttribute("rowId", rowId);
        return this;
    }
    public Boolean getHasRowId() {
        return this.getAttribute("rowId", Boolean.class);
    }
    /*
     * WITHにPKを指定するか
     */
    public MViewLog setHasPK(Boolean pk) {
        this.setAttribute("pk", pk);
        return this;
    }
    public Boolean getHasPK() {
        return this.getAttribute("pk", Boolean.class);
    }
    /*
     * WITHにsequenceを指定するか
     */
    public MViewLog setHasSequence(Boolean sequence) {
        this.setAttribute("sequence", sequence);
        return this;
    }
    public Boolean getHasSequence() {
        return this.getAttribute("sequence", Boolean.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MViewLog that = (MViewLog) o;
        return (this.hashCode() == that.hashCode());
    }

    @Override
    public int hashCode() {
        return toString().toUpperCase().hashCode();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public DatabaseObject[] getContainingObjects() {
        return null;
    }

}
