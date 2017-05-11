package liquibase.ext.ora.structure;

import liquibase.structure.AbstractDatabaseObject;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Schema;

public class Check extends AbstractDatabaseObject {
    private String compareName;

    /*
     * スキーマ
     */
    public Check setSchema(Schema schema) {
        this.setAttribute("schema", schema);
        return this;
    }
    @Override
    public Schema getSchema() {
        return this.getAttribute("schema", Schema.class);
    }
    @Override
    public Check setName(String name) {
        // Liqubaseがこのメソッドを予約しているため、利用できない。(TableSpaceの場合、nameを利用するとすべての値を重複とみなしてしまう。)
        this.compareName = name;
        return this;
    }
    @Override
    public String getName() {
        // Liqubaseがこのメソッドを予約しているため、利用できない。(TableSpaceの場合、nameを利用するとすべての値を重複とみなしてしまう。)
        return toString();
    }

    // Check制約をつける対象テーブル
    public String getTableName() {
        return this.getAttribute("tableName", String.class);
    }
    public Check setTableName(String tableName) {
        this.setAttribute("tableName", tableName);
        return this;
    }

    // Check制約名
    public String getConstraintName() {
        return this.getAttribute("constraintName", String.class);
    }
    public Check setConstraintName(String tableName) {
        this.setAttribute("constraintName", tableName);
        return this;
    }

    // Check制約の内容
    public String getCondition() {
        return this.getAttribute("condition", String.class);
    }
    public Check setCondition(String tableName) {
        this.setAttribute("condition", tableName);
        return this;
    }



    @Override
    public int hashCode() {
        return toString().toUpperCase().hashCode();
    }

    @Override
    public String toString() {
        return getTableName()+getConstraintName();
    }

    @Override
    public DatabaseObject[] getContainingObjects() {
        return null;
    }

}
