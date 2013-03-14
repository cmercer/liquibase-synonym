package liquibase.change.ext.synonym;

import liquibase.statement.AbstractSqlStatement;

/**
 *
 */
public class DropSynonymStatement extends AbstractSqlStatement {

    private String schemaName;
    private String synonymName;
    private boolean publicSynonym;

    public DropSynonymStatement(String schemaName, String synonymName, boolean publicSynonym) {
        this.schemaName = schemaName;
        this.synonymName = synonymName;
        this.publicSynonym = publicSynonym;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public String getSynonymName() {
        return synonymName;
    }

    public boolean isPublicSynonym() {
        return publicSynonym;
    }


}
