package liquibase.change.ext;

import static java.lang.String.format;

import liquibase.change.AbstractChange;
import liquibase.database.Database;
import liquibase.statement.SqlStatement;
import static liquibase.util.StringUtils.trimToNull;

/**
 *
 */
public class AddSynonymChange extends AbstractChange{

    private String sourceTableName;

    private String sourceSchemaName;

    private String synonymName;

    private String schemaName;

    protected AddSynonymChange() {
        super("addSynonym", "Add Synonym", 5);
    }

    @Override
    public String getConfirmationMessage() {

        validate();

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

    public String getSourceTableName() {
        return sourceTableName;
    }

    public void setSourceTableName(String sourceTableName) {
        this.sourceTableName = sourceTableName;
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
}
