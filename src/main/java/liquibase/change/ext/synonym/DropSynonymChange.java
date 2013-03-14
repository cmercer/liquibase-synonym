package liquibase.change.ext.synonym;

import static java.lang.String.format;
import static liquibase.util.StringUtils.trimToNull;

import liquibase.change.AbstractChange;
import liquibase.change.ChangeMetaData;
import liquibase.database.Database;
import liquibase.exception.SetupException;
import liquibase.statement.SqlStatement;

/**
 *
 */
public class DropSynonymChange extends AbstractChange{

    private String synonymName;

    private String schemaName;

    private boolean publicSynonym;

    public DropSynonymChange() {
        super("dropSynonym", "Drop a synonym", ChangeMetaData.PRIORITY_DEFAULT);
    }

    @Override
    public String getConfirmationMessage() {

        String schema = trimToNull(schemaName);
        String synonym = trimToNull(synonymName);

        if (schema != null) {
            synonym = schema + "." + synonym;
        }

        return format("Dropped synonym '%1$s'", synonym);
    }

    private void validate() {
        if (trimToNull(synonymName) == null) {
            throw new AssertionError("Synonym name cannot be null");
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

    @Override
    public SqlStatement[] generateStatements(Database database) {

        String schemaName = getSchemaName();

        if(schemaName == null && database != null) {
            schemaName = database.getDefaultSchemaName();
        }

        SqlStatement[] sqlStatements;
        sqlStatements = new SqlStatement[] {
                new DropSynonymStatement(
                        schemaName,
                        getSynonymName(),
                        isPublicSynonym()
                )
        };

        return sqlStatements;
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

    public Boolean isPublicSynonym() {
        return publicSynonym;
    }

    public void setPublicSynonym(Boolean publicSynonym) {
        this.publicSynonym = publicSynonym;
    }
}
