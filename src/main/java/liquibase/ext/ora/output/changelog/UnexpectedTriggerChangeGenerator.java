package liquibase.ext.ora.output.changelog;

import liquibase.change.Change;
import liquibase.database.Database;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.AbstractChangeGenerator;
import liquibase.diff.output.changelog.ChangeGeneratorChain;
import liquibase.diff.output.changelog.UnexpectedObjectChangeGenerator;
import liquibase.ext.ora.dropSynonym.DropSynonymChange;
import liquibase.ext.ora.droptrigger.DropTriggerChange;
import liquibase.ext.ora.structure.MaterializedView;
import liquibase.ext.ora.structure.Synonym;
import liquibase.ext.ora.structure.Tablespace;
import liquibase.ext.ora.structure.Trigger;
import liquibase.ext.ora.trriger.TriggerChange;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Index;
import liquibase.structure.core.Table;
import liquibase.structure.core.View;

public class UnexpectedTriggerChangeGenerator extends AbstractChangeGenerator implements UnexpectedObjectChangeGenerator {
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
    public Change[] fixUnexpected(DatabaseObject unexpectedObject, DiffOutputControl control, Database referenceDatabase, Database comparisonDatabase, ChangeGeneratorChain chain) {
        Trigger trigger = (Trigger) unexpectedObject;
        DropTriggerChange change = new DropTriggerChange();
        change.setSchemaName(trigger.getSchema().getName());
        change.setTriggerName(trigger.getName());
        return new Change[] { change };
    }
}
