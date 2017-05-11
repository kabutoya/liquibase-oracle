package liquibase.ext.ora.trriger;

import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;


public class TriggerGenerator extends AbstractSqlGenerator<TriggerStatement> {

    public boolean supports(TriggerStatement createTriggertStatement, Database database) {
        return database instanceof OracleDatabase;
    }

    public ValidationErrors validate(TriggerStatement triggerStatement, Database database,
                                     SqlGeneratorChain sqlGeneratorChain) {
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.checkRequiredField("triggerName", triggerStatement.getTriggerName());
        validationErrors.checkRequiredField("triggerSql", triggerStatement.getTriggerSql());
        return validationErrors;
    }

    public Sql[] generateSql(TriggerStatement triggerStatement, Database database,
                             SqlGeneratorChain sqlGeneratorChain) {
        return new Sql[]{new UnparsedSql(triggerStatement.getTriggerSql())};
    }
}
