package liquibase.statement.ext;

import liquibase.statement.AbstractSqlStatement;

public class AddSynonymStatement extends AbstractSqlStatement {

    private String sourceServerName;
    private String sourceDatabaseName;
    private String sourceSchemaName;
    private String sourceTableName;
    private String schemaName;
    private String synonymName;

    public AddSynonymStatement(String sourceServerName, String sourceDatabaseName, String sourceSchemaName, String sourceTableName, String schemaName, String synonymName) {
        this.sourceServerName = sourceServerName;
        this.sourceDatabaseName = sourceDatabaseName;
        this.sourceSchemaName = sourceSchemaName;
        this.sourceTableName = sourceTableName;
        this.schemaName = schemaName;
        this.synonymName = synonymName;
    }

    public AddSynonymStatement(String sourceSchemaName, String sourceTableName, String schemaName, String synonymName) {
        this.sourceSchemaName = sourceSchemaName;
        this.sourceTableName = sourceTableName;
        this.schemaName = schemaName;
        this.synonymName = synonymName;
    }


    public String getSourceServerName() {
        return sourceServerName;
    }

    public String getSourceDatabaseName() {
        return sourceDatabaseName;
    }

    public String getSourceSchemaName() {
        return sourceSchemaName;
    }

    public String getSourceTableName() {
        return sourceTableName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public String getSynonymName() {
        return synonymName;
    }

}
