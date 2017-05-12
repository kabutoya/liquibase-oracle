package liquibase.ext.ora.createmview;

import liquibase.change.AbstractChange;
import liquibase.change.Change;
import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.ext.ora.dropmaterializedview.DropMaterializedViewChange;
import liquibase.statement.SqlStatement;

@DatabaseChange(name="createMView", description = "Create materialized view", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class CreateMViewChange extends AbstractChange {
    private String schemaName;
    private String viewName;
    private String refreshMode;
    private String refreshMethod;
    private String with;
    private String query;

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

    // DEMAND or COMMIT
    public String getRefreshMode() {return refreshMode;}
    public void setRefreshMode(String refreshMode) {this.refreshMode = refreshMode; }

    // COMPLETE or FORCE or FAST
    public String getRefreshMethod() {return refreshMethod;}
    public void setRefreshMethod(String refreshMethod) {this.refreshMethod = refreshMethod;}

    public String getWith() { return with;}
    public void setWith(String with) {this.with = with; }

    public String getQuery() {return query;}
    public void setQuery(String query) { this.query = query;}

    public String getConfirmationMessage() {
        return "Materialized view " + getViewName() + " has been created";
    }

    protected Change[] createInverses() {
        DropMaterializedViewChange inverse = new DropMaterializedViewChange();
        inverse.setSchemaName(getSchemaName());
        inverse.setViewName(getViewName());
        return new Change[]{inverse};
    }

    public SqlStatement[] generateStatements(Database database) {
        CreateMViewStatement statement = new CreateMViewStatement(
                getSchemaName()
                , getViewName()
                , getRefreshMode()
                , getRefreshMethod()
                , getWith()
                , getQuery()
        );
        return new SqlStatement[]{statement};
    }

}
