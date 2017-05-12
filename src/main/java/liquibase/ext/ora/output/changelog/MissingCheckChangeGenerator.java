package liquibase.ext.ora.output.changelog;

import liquibase.change.Change;
import liquibase.database.Database;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.AbstractChangeGenerator;
import liquibase.diff.output.changelog.ChangeGeneratorChain;
import liquibase.diff.output.changelog.MissingObjectChangeGenerator;
import liquibase.ext.ora.addcheck.AddCheckChange;
import liquibase.ext.ora.structure.Check;
import liquibase.ext.ora.structure.MView;
import liquibase.ext.ora.structure.Tablespace;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Index;
import liquibase.structure.core.Table;
import liquibase.structure.core.View;

public class MissingCheckChangeGenerator extends AbstractChangeGenerator implements MissingObjectChangeGenerator {
    @Override
    public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
        if (Check.class.isAssignableFrom(objectType)) {
            return PRIORITY_DEFAULT;
        }
        return PRIORITY_NONE;
    }

    @Override
    public Class<? extends DatabaseObject>[] runAfterTypes()  {
        return new Class[] {Table.class, View.class, Index.class, MView.class};
    }
    @Override
    public Class<? extends DatabaseObject>[] runBeforeTypes() {
        return new Class[] { Tablespace.class };
    }

    @Override
    public Change[] fixMissing(DatabaseObject missingObject, DiffOutputControl control, Database referenceDatabase, final Database comparisonDatabase, ChangeGeneratorChain chain) {
        Check check = (Check) missingObject;

        AddCheckChange change = new AddCheckChange();
        change.setSchemaName(check.getSchema().getName());
        change.setTableName(check.getTableName());
        change.setConstraintName(check.getConstraintName());
        change.setCondition(check.getCondition());

        return new Change[] { change };
    }

}
