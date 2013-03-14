package liquibase.change.ext.synonym;

import static liquibase.change.ext.synonym.Constants.SUPPORTS_PUBLIC;
import static liquibase.util.StringUtils.trimToNull;

import liquibase.change.ext.synonym.structure.Synonym;
import liquibase.database.Database;
import liquibase.database.structure.Schema;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;

/**
 *
 */
public class DropSynonymGenerator extends AbstractSqlGenerator<DropSynonymStatement> {


    @Override
    public ValidationErrors validate(
            DropSynonymStatement statement,
            Database database,
            SqlGeneratorChain sqlGeneratorChain) {

        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.checkRequiredField("synonymName", statement.getSynonymName());
        return validationErrors;
    }

    @Override
    public Sql[] generateSql(DropSynonymStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {

        StringBuilder builder = new StringBuilder();
        builder.append("DROP ");
        if(statement.isPublicSynonym() && SUPPORTS_PUBLIC.contains(database.getClass())) {
            builder.append("PUBLIC ");
        }

        builder.append("SYNONYM ");
        if(trimToNull(statement.getSchemaName()) != null) {
            builder.append(statement.getSchemaName());
            builder.append(".");
        }
        builder.append(statement.getSynonymName());
        builder.append(";");

        UnparsedSql sql;
        if(trimToNull(statement.getSchemaName()) != null) {
            Schema schema = new Schema(statement.getSchemaName());
            Synonym synonym = new Synonym(database, schema, statement.getSynonymName());
            sql = new UnparsedSql(builder.toString(), database, schema, synonym);
        } else {
            Synonym synonym = new Synonym(database, null, statement.getSynonymName());
            sql = new UnparsedSql(builder.toString(), database, synonym);
        }
        return new Sql[]{sql};
    }
}
