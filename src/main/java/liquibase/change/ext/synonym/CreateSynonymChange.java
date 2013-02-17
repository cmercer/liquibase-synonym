package liquibase.change.ext.synonym;

import static java.lang.String.format;
import static liquibase.change.ext.synonym.Constants.SUPPORTS_SYNONYMS;

import liquibase.change.AbstractChange;
import liquibase.change.ChangeMetaData;
import liquibase.database.Database;
import liquibase.exception.SetupException;
import liquibase.statement.SqlStatement;

import static liquibase.util.StringUtils.trimToNull;

/**
 * Adds a synonym to the database.
 * Checks to make sure that the database being used support synonyms.
 * If the database does not support synonyms, it will not product any sql output.
 */
public class CreateSynonymChange extends AbstractChange {

    private String sourceServerName;

    private String sourceDatabaseName;

    private String sourceObjectName;

    private String sourceSchemaName;

    private String synonymName;

    private String schemaName;

    private boolean publicSynonym = false;

    private boolean replaceIfExists = false;

    public CreateSynonymChange() {
        super("createSynonym", "Create Synonym", ChangeMetaData.PRIORITY_DEFAULT);
    }

    public CreateSynonymChange(
            String sourceSchemaName,
            String sourceObjectName,
            String schemaName,
            String synonymName) {
        this(null, null, sourceSchemaName, sourceObjectName, schemaName, synonymName, false, false);
    }

    public CreateSynonymChange(
            String sourceServerName,
            String sourceDatabaseName,
            String sourceSchemaName,
            String sourceObjectName,
            String schemaName,
            String synonymName,
            boolean publicSynonym,
            boolean replaceIfExists) {

        super("createSynonym", "Create Synonym", ChangeMetaData.PRIORITY_DEFAULT);
        setSourceServerName(sourceServerName);
        setSourceDatabaseName(sourceDatabaseName);
        setSourceSchemaName(sourceSchemaName);
        setSourceObjectName(sourceObjectName);
        setSchemaName(schemaName);
        setSynonymName(synonymName);
        setPublicSynonym(publicSynonym);
        setReplaceIfExists(replaceIfExists);
    }

    @Override
    public String getConfirmationMessage() {
        String sourceServer = trimToNull(sourceServerName);
        String sourceDatabase = trimToNull(sourceDatabaseName);
        String sourceSchema = trimToNull(sourceSchemaName);
        String sourceTable = trimToNull(sourceObjectName);
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

        return format("Created synonym '%1$S' for object '%2$S'", synonym, sourceTable);
    }

    @Override
    public SqlStatement[] generateStatements(Database database) {
        String schemaName = getSchemaName();

        if(schemaName == null && database != null) {
            schemaName = database.getDefaultSchemaName();
        }

        SqlStatement[] sqlStatements;
            sqlStatements = new SqlStatement[] {
                new CreateSynonymStatement(
                        getSourceServerName(),
                        getSourceDatabaseName(),
                        getSourceSchemaName(),
                        getSourceObjectName(),
                        schemaName,
                        getSynonymName(),
                        isPublicSynonym(),
                        isReplaceIfExists()
                )
            };

        return sqlStatements;
    }

    private void validate() {
        if (trimToNull(synonymName) == null) {
            throw new AssertionError("Synonym name cannot be null");
        }
        if (trimToNull(sourceObjectName) == null) {
            throw new AssertionError("Source Object name cannot be null");
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
       return SUPPORTS_SYNONYMS.contains(database.getClass());
    }

    public String getSourceServerName() {
        return sourceServerName;
    }

    public void setSourceServerName(String sourceServerName) {
        this.sourceServerName = sourceServerName;
    }

    public String getSourceDatabaseName() {
        return sourceDatabaseName;
    }

    public void setSourceDatabaseName(String sourceDatabaseName) {
        this.sourceDatabaseName = sourceDatabaseName;
    }

    public String getSourceObjectName() {
        return sourceObjectName;
    }

    public void setSourceObjectName(String sourceObjectName) {
        this.sourceObjectName = sourceObjectName;
    }

    public String getSourceSchemaName() {
        return sourceSchemaName;
    }

    public void setSourceSchemaName(String sourceSchemaName) {
        this.sourceSchemaName = sourceSchemaName;
    }

    public String getSynonymName() {
        return synonymName;
    }

    public void setSynonymName(String synonymName) {
        this.synonymName = synonymName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public boolean isPublicSynonym() {
        return publicSynonym;
    }

    public void setPublicSynonym(boolean publicSynonym) {
        this.publicSynonym = publicSynonym;
    }

    public boolean isReplaceIfExists() {
        return replaceIfExists;
    }

    public void setReplaceIfExists(boolean replaceIfExists) {
        this.replaceIfExists = replaceIfExists;
    }
}
