package liquibase.ext.ora.altertablespace;

import liquibase.statement.AbstractSqlStatement;

public class AlterTablespaceStatement extends AbstractSqlStatement {
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
}
