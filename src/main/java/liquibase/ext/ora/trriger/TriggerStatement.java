package liquibase.ext.ora.trriger;

import liquibase.statement.AbstractSqlStatement;


public class TriggerStatement extends AbstractSqlStatement {

    private String schemaName;
    private String triggerName;
    private String triggerSql;

    public TriggerStatement(String schemaName, String triggerName, String triggerSql) {
        this.schemaName = schemaName;
        this.triggerName = triggerName;
        this.triggerSql = triggerSql;
    }

    public String getTriggerName() {
        return this.triggerName;
    }
    public String getTriggerSql() {
        return this.triggerSql;
    }
    public String getSchemaName() {
        return this.schemaName;
    }


}
