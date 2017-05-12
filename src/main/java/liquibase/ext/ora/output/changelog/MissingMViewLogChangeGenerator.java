package liquibase.ext.ora.output.changelog;

import liquibase.change.Change;
import liquibase.database.Database;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.AbstractChangeGenerator;
import liquibase.diff.output.changelog.ChangeGeneratorChain;
import liquibase.diff.output.changelog.MissingObjectChangeGenerator;
import liquibase.ext.ora.createmview.CreateMViewChange;
import liquibase.ext.ora.createmview.CreateMViewLogChange;
import liquibase.ext.ora.createmview.CreateMViewLogStatement;
import liquibase.ext.ora.structure.MView;
import liquibase.ext.ora.structure.MViewLog;
import liquibase.ext.ora.structure.Tablespace;
import liquibase.ext.ora.structure.Trigger;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Index;
import liquibase.structure.core.Table;
import liquibase.structure.core.View;

public class MissingMViewLogChangeGenerator extends AbstractChangeGenerator implements MissingObjectChangeGenerator {
    @Override
    public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
        if (MViewLog.class.isAssignableFrom(objectType)) {
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
        MViewLog mlog = (MViewLog) missingObject;

        String pk = (mlog.getHasPK() == null) ? "false" : String.valueOf(mlog.getHasPK());
        String rowid = (mlog.getHasRowId() == null) ? "false" : String.valueOf(mlog.getHasRowId());
        String sq = (mlog.getHasSequence() == null) ? "false" : String.valueOf(mlog.getHasSequence());

        CreateMViewLogChange change = new CreateMViewLogChange();
        change.setSchemaName(mlog.getSchema().getName());
        change.setViewName(mlog.getName());
        change.setHasPK(pk);
        change.setHasRowId(rowid);
        change.setHasSequence(sq);

        return new Change[] { change };
    }

}
