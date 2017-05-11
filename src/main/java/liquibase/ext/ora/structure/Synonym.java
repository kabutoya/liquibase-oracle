package liquibase.ext.ora.structure;

import liquibase.structure.AbstractDatabaseObject;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Schema;

public class Synonym extends AbstractDatabaseObject {

    private boolean isPublic  = false;
    /*
     * シノニムを定義する先のスキーマ
     */
    public Synonym setSchema(Schema schema) {
        this.setAttribute("schema", schema);
        return this;
    }
    @Override
    public Schema getSchema() {
        return this.getAttribute("schema", Schema.class);
    }
    /*
     * シノニム自身の名前
     */
    public Synonym setName(String name){
        this.setAttribute("name", name);
        return this;
    }
    @Override
    public String getName() {
        // 注意：このメソッドはLiqubaseで「取得したSnapshotが他のSnapshotと同一か」をチェックするためにも使っている。
        // 重複する場合には同じ値とみなされ、genarateやdiffで出力されなくなるため、SynonymがSynonm名のみで同定されることが保証されていなければならない。
        // Publicシノニムに対応する場合、PublicとPrivateで同名のシノニムを定義できるため、いずれか一方が出力されなくなる可能性がある。
        return this.getAttribute("name", String.class);
    }
    /*
     * シノニム元のスキーマ名
     */
    public Synonym setSrcSchemaName(String schema) {
        this.setAttribute("src_schema", schema);
        return this;
    }
    public String getSrcSchemaName() {
        return this.getAttribute("src_schema", String.class);
    }
    /*
     * シノニム元のオブジェクト名
     */
    public Synonym setSrcName(String name){
        this.setAttribute("src_name", name);
        return this;
    }
    public String getSrcName() {
        return this.getAttribute("src_name", String.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Synonym that = (Synonym) o;
        return (this.hashCode() == that.hashCode());
    }

    @Override
    public int hashCode() {
        return toString().toUpperCase().hashCode();
    }

    @Override
    public String toString() {
        return getSchema().getName()+getName()+getSrcName()+ getSrcSchemaName();
    }

    @Override
    public DatabaseObject[] getContainingObjects() {
        return null;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

}
