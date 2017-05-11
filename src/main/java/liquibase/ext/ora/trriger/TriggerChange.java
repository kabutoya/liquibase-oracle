package liquibase.ext.ora.trriger;

import liquibase.change.AbstractChange;
import liquibase.change.Change;
import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.ext.ora.altertablespace.AlterTablespaceStatement;
import liquibase.ext.ora.createdblink.CreateDBLinkStatement;
import liquibase.ext.ora.droptrigger.DropTriggerChange;
import liquibase.statement.SqlStatement;

@DatabaseChange(name="trigger", description = "Create or Replace Trigger", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class TriggerChange extends AbstractChange {
    private String schemaName;
    private String triggerName;
    private String triggerSql;

    public String getSchemaName() {
        return this.schemaName;
    }
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTriggerName() {
        return this.triggerName;
    }
    public void setTriggerName(String triggerName) { this.triggerName = triggerName; }

    public String getTriggerSql() {
        return this.triggerSql;
    }
    public void setTriggerSql(String triggerSql) {
        this.triggerSql = triggerSql;
    }

    @Override
    public SqlStatement[] generateStatements(Database database) {
        return new SqlStatement[] { new TriggerStatement(getSchemaName(), getTriggerName(), getTriggerSql()) };
    }

    @Override
    protected Change[] createInverses() {
        DropTriggerChange inverse = new DropTriggerChange();
        inverse.setSchemaName(getSchemaName());
        inverse.setTriggerName(getTriggerName());
        return new Change[]{
                inverse,
        };
    }

    @Override
    public String getConfirmationMessage() {
        return "Create or Replace trigger " + getTriggerName();
    }
}
