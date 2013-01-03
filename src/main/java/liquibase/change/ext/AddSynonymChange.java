package liquibase.change.ext;

import static java.lang.String.format;

import liquibase.change.AbstractChange;
import liquibase.change.ChangeMetaData;
import liquibase.database.Database;
import liquibase.exception.SetupException;
import liquibase.statement.SqlStatement;
import liquibase.statement.ext.AddSynonymStatement;

import static liquibase.util.StringUtils.trimToNull;

/**
 * Adds a synonym to the database.
 * Checks to make sure that the database being used support synonyms.
 */
public class AddSynonymChange extends AbstractChange {

    private String sourceTableName;

    private String sourceSchemaName;

    private String synonymName;

    private String schemaName;

    private String sourceServerName;

    private String sourceDatabaseName;

    protected AddSynonymChange() {
        super("addSynonym", "Add Synonym", ChangeMetaData.PRIORITY_DEFAULT);
    }

    public AddSynonymChange(
            String sourceSchemaName,
            String sourceTableName,
            String schemaName,
            String synonymName
    ) {

        super("addSynonym", "Add Synonym", ChangeMetaData.PRIORITY_DEFAULT);
        this.sourceSchemaName = sourceSchemaName;
        this.sourceTableName = sourceTableName;
        this.schemaName = schemaName;
        this.synonymName = synonymName;
    }

    public AddSynonymChange(
            String sourceServerName,
            String sourceDatabaseName,
            String sourceSchemaName,
            String sourceTableName,
            String schemaName,
            String synonymName
    ) {
        super("addSynonym", "Add Synonym", ChangeMetaData.PRIORITY_DEFAULT);
        this.sourceServerName = sourceServerName;
        this.sourceDatabaseName = sourceDatabaseName;
        this.sourceSchemaName = sourceSchemaName;
        this.sourceTableName = sourceTableName;
        this.schemaName = schemaName;
        this.synonymName = synonymName;
    }

    @Override
    public String getConfirmationMessage() {
        String sourceServer = trimToNull(sourceServerName);
        String sourceDatabase = trimToNull(sourceDatabaseName);
        String sourceSchema = trimToNull(sourceSchemaName);
        String sourceTable = trimToNull(sourceTableName);
        String schema = trimToNull(schemaName);
        String synonym = trimToNull(synonymName);

        if (schema != null) {
            synonym = schema + "." + synonym;
        }

        if (sourceSchema != null) {
            sourceTable = sourceSchema + "." + sourceTable;
        }

        if (sourceDatabase != null) {
            if (sourceServer != null) {
                sourceTable = sourceServer + "." + sourceDatabase + "." + sourceTable;
            } else {
                sourceTable = sourceDatabase + "." + sourceTable;
            }

        }

        return format("Created synonym '%1$S' for table '%2$S'", synonym, sourceTable);
    }

    @Override
    public SqlStatement[] generateStatements(Database database) {
        SqlStatement[] sqlStatements;
        if (sourceDatabaseName != null) {
            sqlStatements = new SqlStatement[] {
                new AddSynonymStatement(sourceServerName, sourceDatabaseName, sourceSchemaName, sourceTableName, schemaName, synonymName)
            };
        } else {
            sqlStatements = new SqlStatement[] {
                new AddSynonymStatement(sourceSchemaName, sourceTableName, schemaName, synonymName)
            };
        }
        return sqlStatements;
    }

    private void validate() {
        if (trimToNull(synonymName) == null) {
            throw new AssertionError("Synonym name cannot be null");
        }
        if (trimToNull(sourceTableName) == null) {
            throw new AssertionError("Source table name cannot be null");
        }
        if (trimToNull(sourceServerName) != null && trimToNull(sourceDatabaseName) == null) {
            throw new AssertionError("If a source server is provided, a source database must also be provided");
        }
    }

    @Override
    public void init() throws SetupException {
        try {
            validate();
        } catch (AssertionError e) {
            throw new SetupException(e.getMessage());
        }
    }

    protected boolean supportsDatabase(Database database) {
        return ("oracle".equalsIgnoreCase(database.getTypeName())
                || "mssql".equalsIgnoreCase(database.getTypeName())
                || "db2".equalsIgnoreCase(database.getTypeName())
                || "derby".equalsIgnoreCase(database.getTypeName())
                || "informix".equalsIgnoreCase(database.getTypeName())
                || "maxdb".equalsIgnoreCase(database.getTypeName()));
    }

    public String getSourceTableName() {
        return sourceTableName;
    }

    public String getSourceSchemaName() {
        return sourceSchemaName;
    }

    public String getSynonymName() {
        return synonymName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public String getSourceServerName() {
        return sourceServerName;
    }

    public String getSourceDatabaseName() {
        return sourceDatabaseName;
    }

}
