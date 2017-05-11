package liquibase.ext.ora.snapshot;

import liquibase.CatalogAndSchema;
import liquibase.database.AbstractJdbcDatabase;
import liquibase.database.Database;
import liquibase.database.core.InformixDatabase;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.DatabaseException;
import liquibase.executor.ExecutorService;
import liquibase.ext.ora.structure.Synonym;
import liquibase.snapshot.*;
import liquibase.snapshot.jvm.JdbcSnapshotGenerator;
import liquibase.statement.core.GetViewDefinitionStatement;
import liquibase.statement.core.RawSqlStatement;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Schema;
import liquibase.structure.core.View;
import liquibase.util.StringUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SynonymSnapshotGenerator extends JdbcSnapshotGenerator {

    public SynonymSnapshotGenerator() {
        super(Synonym.class, new Class[] { Schema.class });
    }
    @Override
    public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
        if(!(database instanceof OracleDatabase)) {
            return PRIORITY_NONE;
        }
        return super.getPriority(objectType, database);
    }

    @Override
    protected DatabaseObject snapshotObject(DatabaseObject example, DatabaseSnapshot snapshot) throws DatabaseException {
        return example;
    }

    @Override
    protected void addTo(DatabaseObject foundObject, DatabaseSnapshot snapshot) throws DatabaseException, InvalidExampleException {
        if (!snapshot.getSnapshotControl().shouldInclude(Synonym.class)) {
            return;
        }

        if (foundObject instanceof Schema) {
            /* 美容ではPublicSynonymは存在せず、かつPublicSynonymはシステム系のものも多く
               特定が困難(TABLE_OWNERをリストアップするしかない)ため、対象外としておく。 */
            String privateQuery = "select SYNONYM_NAME as NAME, TABLE_OWNER as SRC_SCHEMA, TABLE_NAME as SRC_NAME from USER_SYNONYMS order by SYNONYM_NAME asc";

            Schema schema = (Schema) foundObject;
            Database database = snapshot.getDatabase();
            List<Map<String, ?>> synonyms = ExecutorService.getInstance().getExecutor(database).queryForList(new RawSqlStatement(privateQuery));
            for (Map<String, ?> synonymRow : synonyms) {
                Synonym syno = new Synonym();
                syno.setSrcName((String) synonymRow.get("SRC_NAME"));
                syno.setSrcSchemaName((String) synonymRow.get("SRC_SCHEMA"));
                syno.setName((String) synonymRow.get("NAME"));
                syno.setSchema(schema);
                schema.addDatabaseObject(syno);
            }

        }
    }

}
