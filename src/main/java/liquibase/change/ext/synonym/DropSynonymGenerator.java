package liquibase.change.ext.synonym;

import liquibase.database.Database;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;

/**
 *
 */
public class DropSynonymGenerator extends AbstractSqlGenerator<DropSynonymStatement> {

    @Override
    public ValidationErrors validate(DropSynonymStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        return null;
    }

    @Override
    public Sql[] generateSql(DropSynonymStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        return new Sql[0];
    }
}
