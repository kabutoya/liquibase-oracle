package liquibase.ext.ora.dropdblink;

import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.ext.ora.dblink.DBLinkChange;


@DatabaseChange(name="dropDBLink", description = "Drop DB-Link", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class DropDBLinkChange extends DBLinkChange {
    public DropDBLinkChange() {
    }

    public String getConfirmationMessage() {
        return "Drop DB-Link " + getDblinkName() + " connect to " + getUser();
    }
}