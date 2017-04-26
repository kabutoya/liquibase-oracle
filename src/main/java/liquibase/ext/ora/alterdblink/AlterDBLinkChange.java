package liquibase.ext.ora.alterdblink;

import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.ext.ora.dblink.DBLinkChange;


@DatabaseChange(name="alterDBLink", description = "Alter DB-Link", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class AlterDBLinkChange extends DBLinkChange {
    public AlterDBLinkChange() {
    }

    public String getConfirmationMessage() {
        return "Alter DB-Link " + getDblinkName() + " connect to " + getUser();
    }
}