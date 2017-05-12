package liquibase.ext.ora.createmview;

import liquibase.change.AbstractChange;
import liquibase.change.Change;
import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.ext.ora.dropmaterializedview.DropMaterializedViewChange;
import liquibase.statement.SqlStatement;

@DatabaseChange(name="createMViewLog", description = "Create materialized view", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class CreateMViewLogChange extends AbstractChange {
    private String schemaName;
    private String viewName;
    private String hasRowId;
    private String hasPK;
    private String hasSequence;

    public String getSchemaName() {
        return schemaName;
    }
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getViewName() {
        return viewName;
    }
    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getHasRowId() {return hasRowId;}
    public void setHasRowId(String hasRowId) {this.hasRowId = hasRowId; }

    public String getHasPK() { return hasPK;}
    public void setHasPK(String hasPK) {this.hasPK = hasPK;}

    public String getHasSequence() {return hasSequence;}
    public void setHasSequence(String hasSequence) { this.hasSequence = hasSequence;}


    public String getConfirmationMessage() {
        return "Materialized view Log " + getViewName() + " has been created";
    }

    protected Change[] createInverses() {
        return new Change[]{};
    }

    public SqlStatement[] generateStatements(Database database) {
        CreateMViewLogStatement statement = new CreateMViewLogStatement(
                getSchemaName()
                , getViewName()
        );
        boolean pk = getHasPK()==null ? false : new Boolean(getHasPK());
        boolean rowId = getHasRowId()==null ? false : new Boolean(getHasRowId());
        boolean seq = getHasSequence()==null ? false : new Boolean(getHasSequence());
        statement.setHasPK(pk);
        statement.setHasRowId(rowId);
        statement.setHasSequence(seq);
        return new SqlStatement[]{statement};
    }

}
