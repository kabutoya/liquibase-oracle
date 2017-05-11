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
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Index;
import liquibase.structure.core.Table;

public class MissingTablespaceChangeGenerator extends AbstractChangeGenerator implements MissingObjectChangeGenerator {
    @Override
    public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
        if (Tablespace.class.isAssignableFrom(objectType)) {
            return PRIORITY_DEFAULT;
        }
        return PRIORITY_NONE;
    }

    @Override
    public Class<? extends DatabaseObject>[] runAfterTypes()  {
        return new Class[] {Table.class, Index.class, MaterializedView.class};
    }

    @Override
    public Class<? extends DatabaseObject>[] runBeforeTypes() {
        return null;
    }

    @Override
    public Change[] fixMissing(DatabaseObject missingObject, DiffOutputControl control, Database referenceDatabase, final Database comparisonDatabase, ChangeGeneratorChain chain) {
        Tablespace space = (Tablespace) missingObject;

        AlterTablespaceChange change = new AlterTablespaceChange();
        change.setObjectSchemaName(space.getSchema().getName());
        change.setObjectName(space.getObjectName());
        change.setType(space.getType());
        change.setTablespaceName(space.getTableSpaceName());

        return new Change[] { change };
    }

}
