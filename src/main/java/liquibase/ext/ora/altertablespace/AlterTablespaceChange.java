package liquibase.ext.ora.altertablespace;

import liquibase.change.AbstractChange;
import liquibase.change.Change;
import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.statement.SqlStatement;

import java.text.MessageFormat;

@DatabaseChange(name = "alterTablespace", description = "Alter tablespace", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class AlterTablespaceChange extends AbstractChange {

	private String type;
	private String tablespaceName;
	private String objectName;
	private String objectSchemaName;

	public String getType() { return type; }

	public void setType(String type) { this.type = type; }

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getObjectSchemaName() {
		return objectSchemaName;
	}

	public void setObjectSchemaName(String objectSchemaName) {
		this.objectSchemaName = objectSchemaName;
	}

	public String getTablespaceName() { return tablespaceName;}

	public void setTablespaceName(String tablespaceName) { this.tablespaceName = tablespaceName; }

    @Override
    public String getConfirmationMessage() {
        return MessageFormat.format("tablespace {0} altered", objectName);
    }

	@Override
	protected Change[] createInverses() {
		return new Change[] {};
	}
	
	@Override
	public SqlStatement[] generateStatements(Database database) {
		AlterTablespaceStatement statement = new AlterTablespaceStatement();

		statement.setType(getType());
		statement.setTablespaceName(getTablespaceName());
		statement.setObjectName(getObjectName());
		statement.setObjectSchemaName(getObjectSchemaName());
		return new SqlStatement[] { statement };

	}

}
