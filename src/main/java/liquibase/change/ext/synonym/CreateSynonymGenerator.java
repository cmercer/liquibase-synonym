package liquibase.change.ext.synonym;

import liquibase.database.Database;
import liquibase.database.structure.Schema;
import liquibase.exception.ValidationErrors;
import liquibase.sql.SingleLineComment;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;

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
            Database delegateDatabase,
            SqlGeneratorChain sqlGeneratorChain) {

        SynonymDatabase database = new SynonymDatabase(delegateDatabase);
        if(database.supportsSynonyms()) {
            String schemaName = database.generateRealSchemaName(statement.getSchemaName());
            StringBuilder builder = new StringBuilder();

            builder.append("CREATE ");
            builder.append(replaceIfNeeded(statement, database));
            builder.append(publicIfNeeded(statement, database));
            builder.append("SYNONYM ");
            builder.append(database.escapeSynonymName(schemaName, statement.getSynonymName()));
            builder.append(" FOR ");
            builder.append(
                    database.escapeRemoteObject(
                        statement.getSourceServerName(),
                        statement.getSourceDatabaseName(),
                        statement.getSourceSchemaName(),
                        statement.getSourceTableName()
                )
            );
            builder.append(";");
            UnparsedSql sql;

            if(schemaName != null && database.supportsSchemas()) {
                sql = new UnparsedSql(builder.toString(), database, new Schema(statement.getSchemaName()));
            } else {
                sql = new UnparsedSql(builder.toString(), database);
            }

            return new Sql[]{sql};
        } else {

            String synonymName = database.escapeSynonymName(
                    database.generateRealSchemaName(statement.getSchemaName()),
                    statement.getSynonymName()
            );
            StringBuilder builder = new StringBuilder();
            builder.append("Database ").append(database.getTypeName()).append(" does not support synonyms")
                    .append(" therefore synonym ").append(synonymName).append(" was not created\n");
            return new Sql[]{new SingleLineComment(builder.toString(), database.getLineComment())};
        }
    }

    protected String replaceIfNeeded(CreateSynonymStatement statement, SynonymDatabase database) {
        String value = "";
        if(statement.isReplaceIfExists() && database.supportsReplaceSynonyms()) {
            value =  "OR REPLACE ";
        }

        return value;
    }

    protected String publicIfNeeded(CreateSynonymStatement statement, SynonymDatabase database) {
        String value = "";
        if(statement.isPublicSynonym() && database.supportsPublicSynonyms()) {
            value = "PUBLIC ";
        }

        return value;
    }
}