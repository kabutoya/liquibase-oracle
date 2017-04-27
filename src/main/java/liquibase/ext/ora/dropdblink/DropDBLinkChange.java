package liquibase.ext.ora.dropdblink;

import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.ext.ora.dblink.DBLinkChange;
import liquibase.statement.SqlStatement;

@DatabaseChange(name="dropDBLink", description = "Drop DB-Link", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class DropDBLinkChange extends DBLinkChange {
    public SqlStatement[] generateStatements(Database database) {
        return constnstructStatment(new DropDBLinkStatement());
    }
    public String getConfirmationMessage() {
        return "Drop DB-Link " + getDblinkName() + " connect to " + getUser();
    }
}


