package liquibase.change.ext;

import static java.lang.String.format;

import liquibase.change.AbstractChange;
import liquibase.database.Database;
import liquibase.exception.SetupException;
import liquibase.statement.SqlStatement;

import static liquibase.util.StringUtils.trimToNull;

/**
 *
 */
public class AddSynonymChange extends AbstractChange {

    private String sourceTableName;

    private String sourceSchemaName;

    private String synonymName;

    private String schemaName;

    protected AddSynonymChange() {
        super("addSynonym", "Add Synonym", 5);
    }

    public AddSynonymChange(String sourceSchemaName, String sourceTableName, String schemaName, String synonymName) throws IllegalArgumentException {
        super("addSynonym", "Add Synonym", 5);
        this.sourceSchemaName = sourceSchemaName;
        this.sourceTableName = sourceTableName;
        this.schemaName = schemaName;
        this.synonymName = synonymName;
    }


    @Override
    public String getConfirmationMessage() {
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

}
