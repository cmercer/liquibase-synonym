package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;
import liquibase.statement.ext.AddSynonymStatement;

import static liquibase.util.StringUtils.trimToNull;

public class AddSynonymGenerator extends AbstractSqlGenerator<AddSynonymStatement> {

    @Override
    public ValidationErrors validate(AddSynonymStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.checkRequiredField("sourceTableName", statement.getSourceTableName());
        validationErrors.checkRequiredField("synonymName", statement.getSynonymName());
        return validationErrors;
    }

    @Override
    public Sql[] generateSql(AddSynonymStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE SYNONYM ");
        if (trimToNull(statement.getSchemaName()) != null) {
            builder.append(statement.getSchemaName()).append(".");
        }
        builder.append(statement.getSynonymName());
        builder.append(" FOR ");
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
