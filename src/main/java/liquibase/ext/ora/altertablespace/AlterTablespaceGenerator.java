package liquibase.ext.ora.altertablespace;

import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.ext.ora.createSynonym.CreateSynonymStatement;
import liquibase.ext.ora.structure.Tablespace;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;

public class AlterTablespaceGenerator extends AbstractSqlGenerator<AlterTablespaceStatement> {

	public boolean supports(CreateSynonymStatement statement, Database database) {
		return database instanceof OracleDatabase;
	}

	public ValidationErrors validate(AlterTablespaceStatement statement, Database database, SqlGeneratorChain chain) {

		ValidationErrors validationErrors = new ValidationErrors();
		validationErrors.checkRequiredField("type", statement.getType());
		validationErrors.checkRequiredField("tablespaceName", statement.getTablespaceName());
		validationErrors.checkRequiredField("objectName", statement.getObjectName());
		return validationErrors;
	}

	public Sql[] generateSql(AlterTablespaceStatement statement, Database database, SqlGeneratorChain chain) {
		String objectType = statement.getType();
		String targetType;
		if(Tablespace.TYPE_INDEX.equalsIgnoreCase(objectType)) {
			objectType = Tablespace.TYPE_INDEX;
			targetType = "REBUILD TABLESPACE";
		} else if(Tablespace.TYPE_TABLE.equalsIgnoreCase(objectType)) {
			objectType = Tablespace.TYPE_TABLE;
			targetType = "MOVE TABLESPACE";
		} else {
			objectType = "MATERIALIZED VIEW";
			targetType = "MOVE TABLESPACE";
		}

		StringBuilder sb = new StringBuilder("ALTER ");
		sb.append(objectType).append(" ");
		if(statement.getObjectSchemaName() !=null && !statement.getObjectSchemaName().isEmpty()){
			sb.append(statement.getObjectSchemaName()).append(".");
		}
		sb.append(statement.getObjectName()).append(" ")
		  .append(targetType).append(" ")
  		  .append(statement.getTablespaceName()).append(" ");
		return new Sql[] { new UnparsedSql(sb.toString()) };
	}
}
