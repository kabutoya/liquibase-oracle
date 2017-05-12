package liquibase.ext.ora.createmview;

import liquibase.statement.AbstractSqlStatement;

public class CreateMViewStatement extends AbstractSqlStatement {
    private String schemaName;
    private String viewName;
    private String refreshMode;
    private String refreshMethod;
    private String with;
    private String query;

    public CreateMViewStatement(String schemaName, String viewName, String mode, String method, String with, String query) {
        this.schemaName = schemaName;
        this.viewName = viewName;
        this.refreshMode = mode;
        this.refreshMethod = method;
        this.with = with;
        this.query = query;
    }
    public String getSchemaName() {
        return schemaName;
    }
    public String getViewName() {return viewName;}
    // DEMAND or COMMIT
    public String getRefreshMode() {
        return refreshMode;
    }
    // COMPLETE or FORCE or FAST
    public String getRefreshMethod() {
        return refreshMethod;
    }
    public String getWith() {
        return with;
    }
    public String getQuery() {
        return query;
    }
}
