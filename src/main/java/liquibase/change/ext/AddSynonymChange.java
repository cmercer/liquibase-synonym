package liquibase.change.ext;

import static java.lang.String.format;

import liquibase.change.AbstractChange;
import liquibase.database.Database;
import liquibase.statement.SqlStatement;

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

        String schema = synonymName;
        String table = sourceTableName;

        if(schemaName != null) {
            schema = schemaName + "." + synonymName;
        }

        if(sourceSchemaName != null) {
            table = sourceSchemaName + "." + sourceTableName;
        }

        return format("Created synonym '%1$S' for table '%2$S'", schema, table);
    }

    @Override
    public SqlStatement[] generateStatements(Database database) {
        return new SqlStatement[0];
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
