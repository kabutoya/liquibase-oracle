package liquibase.ext.ora.createdblink;

import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.ext.ora.dblink.DBLinkChange;
import liquibase.statement.SqlStatement;


@DatabaseChange(name="createDBLink", description = "Create DB-Link", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class CreateDBLinkChange extends DBLinkChange {
    public SqlStatement[] generateStatements(Database database) {
        return constnstructStatment(new CreateDBLinkStatement());
    }

    public String getConfirmationMessage() {
        return "Create DB-Link " + getDblinkName() + " connect to " + getUser();
    }
}