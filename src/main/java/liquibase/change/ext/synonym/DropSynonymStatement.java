package liquibase.change.ext.synonym;

import liquibase.statement.AbstractSqlStatement;

/**
 *
 */
public class DropSynonymStatement extends AbstractSqlStatement {

    private String schemaName;
    private String synonymName;

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getSynonymName() {
        return synonymName;
    }

    public void setSynonymName(String synonymName) {
        this.synonymName = synonymName;
    }
}
