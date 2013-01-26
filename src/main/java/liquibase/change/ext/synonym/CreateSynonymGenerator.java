package liquibase.change.ext.synonym;

import liquibase.database.Database;
import liquibase.database.core.DB2Database;
import liquibase.database.core.DerbyDatabase;
import liquibase.database.core.InformixDatabase;
import liquibase.database.core.MSSQLDatabase;
import liquibase.database.core.MaxDBDatabase;
import liquibase.database.core.OracleDatabase;
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
        builder.append("CREATE ");
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
        return new Sql[] {
            new UnparsedSql(builder.toString())
        };
    }
    
    /**
     * return true if the database supports public synonyms
     */
    protected boolean supportsPublic(Database database) {
        if(database instanceof OracleDatabase) {
            return true;
        } else if(database instanceof InformixDatabase) {
            return true;
        } else if(database instanceof  MaxDBDatabase) {
            return true;
        }else{
        	return false;
        }
    }
}