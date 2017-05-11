package liquibase.ext.ora.output.changelog;

import liquibase.change.Change;
import liquibase.database.Database;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.AbstractChangeGenerator;
import liquibase.diff.output.changelog.ChangeGeneratorChain;
import liquibase.diff.output.changelog.MissingObjectChangeGenerator;
import liquibase.ext.ora.altertablespace.AlterTablespaceChange;
import liquibase.ext.ora.structure.MaterializedView;
import liquibase.ext.ora.structure.Tablespace;
import liquibase.ext.ora.structure.Trigger;
import liquibase.ext.ora.trriger.TriggerChange;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Index;
import liquibase.structure.core.Schema;
import liquibase.structure.core.Table;
import liquibase.structure.core.View;

public class MissingTriggerChangeGenerator extends AbstractChangeGenerator implements MissingObjectChangeGenerator {
    @Override
    public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
        if (Trigger.class.isAssignableFrom(objectType)) {
            return PRIORITY_DEFAULT;
        }
        return PRIORITY_NONE;
    }

    @Override
    public Class<? extends DatabaseObject>[] runAfterTypes()  {
        return new Class[] {Table.class, View.class, Index.class, MaterializedView.class};
    }
    @Override
    public Class<? extends DatabaseObject>[] runBeforeTypes() {
        return new Class[] { Tablespace.class };
    }

    @Override
    public Change[] fixMissing(DatabaseObject missingObject, DiffOutputControl control, Database referenceDatabase, final Database comparisonDatabase, ChangeGeneratorChain chain) {
        Trigger trigger = (Trigger) missingObject;

        TriggerChange change = new TriggerChange();
        change.setSchemaName(trigger.getSchema().getName());
        change.setTriggerName(trigger.getName());
        change.setTriggerSql(trigger.getTriggerSql());

        return new Change[] { change };
    }

}
