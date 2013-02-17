package liquibase.change.ext.synonym;

import static liquibase.change.ext.synonym.Constants.SUPPORTS_PUBLIC;
import static liquibase.change.ext.synonym.Constants.SUPPORTS_REPLACE;

import liquibase.database.Database;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;

import static liquibase.util.StringUtils.trimToNull;

import java.util.ArrayList;
import java.util.List;

public class CreateSynonymGenerator extends AbstractSqlGenerator<CreateSynonymStatement> {

    @Override
    public ValidationErrors validate(
            CreateSynonymStatement statement,
            Database database,
            SqlGeneratorChain sqlGeneratorChain) {

        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.checkRequiredField("sourceTableName", statement.getSourceTableName());
        validationErrors.checkRequiredField("synonymName", statement.getSynonymName());
        return validationErrors;
    }

    @Override
    public Sql[] generateSql(
            CreateSynonymStatement statement,
            Database database,
            SqlGeneratorChain sqlGeneratorChain) {

        List<Sql> results = new ArrayList<Sql>();
        StringBuilder builder = new StringBuilder();

        builder.append("CREATE ");
        if(statement.isReplaceIfExists() && supportsReplace(database)) {
            builder.append("OR REPLACE ");
        }
        if(statement.isPublicSynonym() && supportsPublic(database)){
        	builder.append("PUBLIC ");
        }
        builder.append("SYNONYM ");
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

        results.add(new UnparsedSql(builder.toString()));
        return results.toArray(new Sql[results.size()]);
    }

    /**
     * return true if the database supports public synonyms
     */
    protected boolean supportsPublic(Database database) {
        return SUPPORTS_PUBLIC.contains(database.getClass());
    }

    protected boolean supportsReplace(Database database) {
        return SUPPORTS_REPLACE.contains(database.getClass());
    }
}