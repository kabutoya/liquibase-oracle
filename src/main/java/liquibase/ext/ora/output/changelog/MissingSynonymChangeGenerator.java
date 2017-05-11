package liquibase.ext.ora.output.changelog;

import liquibase.change.Change;
import liquibase.database.Database;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.AbstractChangeGenerator;
import liquibase.diff.output.changelog.ChangeGeneratorChain;
import liquibase.diff.output.changelog.MissingObjectChangeGenerator;
import liquibase.ext.ora.createSynonym.CreateSynonymChange;
import liquibase.ext.ora.structure.Synonym;
import liquibase.structure.DatabaseObject;

public class MissingSynonymChangeGenerator extends AbstractChangeGenerator implements MissingObjectChangeGenerator {
    @Override
    public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
        if (Synonym.class.isAssignableFrom(objectType)) {
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
    public Change[] fixMissing(DatabaseObject missingObject, DiffOutputControl control, Database referenceDatabase, final Database comparisonDatabase, ChangeGeneratorChain chain) {
        Synonym syno = (Synonym) missingObject;

        CreateSynonymChange change = new CreateSynonymChange();
        change.setReplace(true);
        change.setPublic(syno.isPublic());

        change.setSynonymSchemaName(syno.getSchema().getName());
        change.setSynonymName(syno.getName());

        change.setObjectSchemaName(syno.getSrcSchemaName());
        change.setObjectName(syno.getSrcName());

        return new Change[] { change };
    }

}
