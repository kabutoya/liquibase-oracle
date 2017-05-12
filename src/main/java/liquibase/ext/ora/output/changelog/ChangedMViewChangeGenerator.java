package liquibase.ext.ora.output.changelog;

import liquibase.change.Change;
import liquibase.database.Database;
import liquibase.diff.ObjectDifferences;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.AbstractChangeGenerator;
import liquibase.diff.output.changelog.ChangeGeneratorChain;
import liquibase.diff.output.changelog.ChangedObjectChangeGenerator;
import liquibase.ext.ora.createmview.CreateMViewChange;
import liquibase.ext.ora.structure.MView;
import liquibase.structure.DatabaseObject;

public class ChangedMViewChangeGenerator extends AbstractChangeGenerator implements ChangedObjectChangeGenerator {
    @Override
    public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
        if (MView.class.isAssignableFrom(objectType)) {
            return PRIORITY_DEFAULT;
        }
        return PRIORITY_NONE;
    }

    @Override
    public Class<? extends DatabaseObject>[] runAfterTypes()  {
        return null;
    }

    @Override
    public Class<? extends DatabaseObject>[] runBeforeTypes() {
        return null;
    }

    @Override
    public Change[] fixChanged(DatabaseObject changedObject, ObjectDifferences differences, DiffOutputControl control, Database referenceDatabase, final Database comparisonDatabase, ChangeGeneratorChain chain) {
        MView mview = (MView) changedObject;

        CreateMViewChange change = new CreateMViewChange();
        change.setSchemaName(mview.getSchema().getName());
        change.setViewName(mview.getName());
        change.setRefreshMethod(mview.getRefreshMethod());
        change.setRefreshMode(mview.getMode());
        change.setWith(mview.getWith());
        change.setQuery(mview.getQuery());
        return new Change[] { change };
    }
}
