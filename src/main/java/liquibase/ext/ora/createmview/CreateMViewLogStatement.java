package liquibase.ext.ora.createmview;

import liquibase.statement.AbstractSqlStatement;

public class CreateMViewLogStatement extends AbstractSqlStatement {
    private String schemaName;
    private String viewName;
    private boolean hasRowId;
    private boolean hasPK;
    private boolean hasSequence;

    public CreateMViewLogStatement(String schemaName, String viewName) {
        this.schemaName = schemaName;
        this.viewName = viewName;
    }
    public String getSchemaName() {
        return schemaName;
    }
    public String getViewName() {
        return viewName;
    }
    public boolean isHasRowId() {return hasRowId;}
    public void setHasRowId(boolean hasRowId) {this.hasRowId = hasRowId; }
    public boolean isHasPK() {return hasPK;}
    public void setHasPK(boolean hasPK) {this.hasPK = hasPK;}
    public boolean isHasSequence() {return hasSequence; }
    public void setHasSequence(boolean hasSequence) { this.hasSequence = hasSequence;}
}
