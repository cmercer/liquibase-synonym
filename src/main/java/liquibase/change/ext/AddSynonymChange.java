package liquibase.change.ext;

import static java.lang.String.format;

import liquibase.change.AbstractChange;
import liquibase.change.ChangeMetaData;
import liquibase.database.Database;
import liquibase.database.core.DB2Database;
import liquibase.database.core.DerbyDatabase;
import liquibase.database.core.InformixDatabase;
import liquibase.database.core.MSSQLDatabase;
import liquibase.database.core.MaxDBDatabase;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.SetupException;
import liquibase.statement.SqlStatement;

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


    public AddSynonymChange() {
        super("addSynonym", "Add Synonym", ChangeMetaData.PRIORITY_DEFAULT);
    }

    public AddSynonymChange(
            String sourceSchemaName,
            String sourceTableName,
            String schemaName,
            String synonymName
    ) throws IllegalArgumentException {

        super("addSynonym", "Add Synonym", 5);
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
        return new SqlStatement[0];
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

        boolean supported = false;

        if(database instanceof OracleDatabase) {
            supported = true;
        } else if(database instanceof MSSQLDatabase) {
            supported = true;
        } else if(database instanceof DB2Database) {
            supported = true;
        } else if(database instanceof DerbyDatabase) {
            supported = true;
        } else if(database instanceof InformixDatabase) {
            supported = true;
        } else if(database instanceof  MaxDBDatabase) {
            supported = true;
        }

        return supported;
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

}
