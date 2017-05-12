package liquibase.ext.ora.output.changelog;

import liquibase.change.Change;
import liquibase.database.Database;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.AbstractChangeGenerator;
import liquibase.diff.output.changelog.ChangeGeneratorChain;
import liquibase.diff.output.changelog.UnexpectedObjectChangeGenerator;
import liquibase.ext.ora.dropdblink.DropDBLinkChange;
import liquibase.ext.ora.structure.DBLink;
import liquibase.ext.ora.structure.MView;
import liquibase.ext.ora.structure.Tablespace;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Index;
import liquibase.structure.core.Table;
import liquibase.structure.core.View;

public class UnexpectedDBLinkChangeGenerator extends AbstractChangeGenerator implements UnexpectedObjectChangeGenerator {
    @Override
    public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
        if (DBLink.class.isAssignableFrom(objectType)) {
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
    public Change[] fixUnexpected(DatabaseObject unexpectedObject, DiffOutputControl control, Database referenceDatabase, Database comparisonDatabase, ChangeGeneratorChain chain) {
        DBLink link = (DBLink) unexpectedObject;

        DropDBLinkChange change = new DropDBLinkChange();
        change.setType(link.getType());
        change.setDblinkName(link.getDBLinkName());

        return new Change[] { change };
    }
}
