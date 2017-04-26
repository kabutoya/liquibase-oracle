package liquibase.ext.ora.createdblink;

import liquibase.change.Change;
import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.ext.ora.check.CheckAttribute;
import liquibase.ext.ora.dblink.DBLinkChange;
import liquibase.ext.ora.dblink.DbLinkAttribute;
import liquibase.ext.ora.dblink.DbLinkState;
import liquibase.ext.ora.dropcheck.DropCheckChange;
import liquibase.statement.SqlStatement;


@DatabaseChange(name="createDBLink", description = "Create DB-Link", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class CreateDBLinkChange extends DBLinkChange {
    public CreateDBLinkChange() {
    }

    public String getConfirmationMessage() {
        return "Create DB-Link " + getDblinkName() + " connect to " + getUser();
    }
}