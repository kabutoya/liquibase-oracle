package liquibase.ext.ora.alterdblink;

import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.ext.ora.dblink.DBLinkChange;
import liquibase.statement.SqlStatement;


@DatabaseChange(name="alterDBLink", description = "Alter DB-Link", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class AlterDBLinkChange extends DBLinkChange {
    public SqlStatement[] generateStatements(Database database) {
        return constnstructStatment(new AlterDBLinkStatement());
    }

    public String getConfirmationMessage() {
        return "Alter DB-Link " + getDblinkName() + " connect to " + getUser();
    }
}