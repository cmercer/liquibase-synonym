package liquibase.change.ext.synonym;

import liquibase.database.Database;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;

import static liquibase.util.StringUtils.trimToNull;

public class CreateSynonymGenerator extends AbstractSqlGenerator<CreateSynonymStatement> {

    @Override
    public ValidationErrors validate(CreateSynonymStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.checkRequiredField("sourceTableName", statement.getSourceTableName());
        validationErrors.checkRequiredField("synonymName", statement.getSynonymName());
        return validationErrors;
    }

    @Override
    public Sql[] generateSql(CreateSynonymStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE SYNONYM ");
        if (trimToNull(statement.getSchemaName()) != null) {
            builder.append(statement.getSchemaName()).append(".");
        }
        builder.append(statement.getSynonymName());
        builder.append(" FOR ");
        if (trimToNull(statement.getSourceDatabaseName()) != null) {
            if (trimToNull(statement.getSourceServerName()) != null) {
                builder.append(statement.getSourceServerName()).append(".");
            }
            builder.append(statement.getSourceDatabaseName()).append(".");
        }
        if (trimToNull(statement.getSourceSchemaName()) != null) {
            builder.append(statement.getSourceSchemaName()).append(".");
        }
        builder.append(statement.getSourceTableName());
        builder.append(";");
        return new Sql[] {
            new UnparsedSql(builder.toString())
        };
    }

}
