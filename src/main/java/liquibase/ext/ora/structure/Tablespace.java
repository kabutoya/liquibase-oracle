package liquibase.ext.ora.structure;

import liquibase.structure.AbstractDatabaseObject;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Schema;

public class Tablespace extends AbstractDatabaseObject {
    public static String TYPE_INDEX = "INDEX";
    public static String TYPE_TABLE = "TABLE";
    public static String TYPE_MVIEW = "MVIEW";

    private String compareName;

    /*
     * シノニムを定義する先のスキーマ
     */
    public Tablespace setSchema(Schema schema) {
        this.setAttribute("schema", schema);
        return this;
    }
    @Override
    public Schema getSchema() {
        return this.getAttribute("schema", Schema.class);
    }
    /*
     * Tablespaceの名前
     */
    public Tablespace setTableSpaceName(String name){
        this.setAttribute("name", name);
        return this;
    }
    public String getTableSpaceName() {
        return this.getAttribute("name", String.class);
    }
    @Override
    public Tablespace setName(String name) {
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
     * 対象オブジェクト名
     */
    public Tablespace setObjectName(String objectname) {
        this.setAttribute("objectname", objectname);
        return this;
    }
    public String getObjectName() {
        return this.getAttribute("objectname", String.class);
    }
    /*
     * シノニム元のオブジェクト名
     */
    public Tablespace setType(String name){
        this.setAttribute("type", name);
        return this;
    }
    public String getType() {
        return this.getAttribute("type", String.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tablespace that = (Tablespace) o;
        return (this.hashCode() == that.hashCode());
    }

    @Override
    public int hashCode() {
        return toString().toUpperCase().hashCode();
    }

    @Override
    public String toString() {
        return getSchema().getName()+getObjectName()+getTableSpaceName();
    }

    @Override
    public DatabaseObject[] getContainingObjects() {
        return null;
    }

}
