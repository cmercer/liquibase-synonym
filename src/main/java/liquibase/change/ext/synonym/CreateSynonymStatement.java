package liquibase.change.ext.synonym;

import liquibase.statement.AbstractSqlStatement;

public class CreateSynonymStatement extends AbstractSqlStatement {

    private String sourceServerName;
    private String sourceDatabaseName;
    private String sourceSchemaName;
    private String sourceTableName;
    private String schemaName;
    private String synonymName;
    private boolean publicSynonym;
    private boolean replaceSynonym;

	public CreateSynonymStatement(
            String sourceServerName,
            String sourceDatabaseName,
            String sourceSchemaName,
            String sourceTableName,
            String schemaName,
            String synonymName) {
        this.sourceServerName = sourceServerName;
        this.sourceDatabaseName = sourceDatabaseName;
        this.sourceSchemaName = sourceSchemaName;
        this.sourceTableName = sourceTableName;
        this.schemaName = schemaName;
        this.synonymName = synonymName;
        this.publicSynonym = false;
    }

	public CreateSynonymStatement(
            String sourceServerName,
            String sourceDatabaseName,
            String sourceSchemaName,
            String sourceTableName,
            String schemaName,
            String synonymName,
            boolean publicSynonym) {
		this(sourceServerName,sourceDatabaseName,sourceSchemaName,
				sourceTableName, schemaName, synonymName);
		this.publicSynonym = publicSynonym;
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

    public boolean isPublicSynonym() {
		return publicSynonym;
	}

	public void setPublicSynonym(boolean publicSynonym) {
		this.publicSynonym = publicSynonym;
	}

    public boolean isReplaceSynonym() {
        return replaceSynonym;
    }

    public void setReplaceSynonym(boolean replaceSynonym) {
        this.replaceSynonym = replaceSynonym;
    }
}
