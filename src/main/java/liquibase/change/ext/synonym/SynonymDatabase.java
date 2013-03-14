package liquibase.change.ext.synonym;

import static liquibase.change.ext.synonym.Constants.*;
import static liquibase.util.StringUtils.trimToNull;

import java.io.IOException;
import java.io.Writer;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import liquibase.change.Change;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.changelog.RanChangeSet;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.structure.DatabaseObject;
import liquibase.exception.DatabaseException;
import liquibase.exception.DatabaseHistoryException;
import liquibase.exception.DateParseException;
import liquibase.exception.LiquibaseException;
import liquibase.exception.RollbackImpossibleException;
import liquibase.exception.StatementNotSupportedOnDatabaseException;
import liquibase.exception.UnsupportedChangeException;
import liquibase.sql.visitor.SqlVisitor;
import liquibase.statement.DatabaseFunction;
import liquibase.statement.SqlStatement;
import liquibase.util.StringUtils;

/**
 *
 */
class SynonymDatabase implements Database {

    private final Database delegateDatabase;

    public SynonymDatabase(Database delegateDatabase) {
        this.delegateDatabase = delegateDatabase;
    }

    public boolean supportsSynonyms() {
        return SUPPORTS_SYNONYMS.contains(delegateDatabase.getClass());
    }

    public boolean supportsPublicSynonyms() {
        return SUPPORTS_PUBLIC.contains(delegateDatabase.getClass());
    }

    public boolean supportsReplaceSynonyms() {
        return SUPPORTS_REPLACE.contains(delegateDatabase.getClass());
    }

    public boolean supportsRemoteSynonyms() {
        return SUPPORTS_REMOTE_SYNONYMS.contains(delegateDatabase.getClass());
    }

    /**
     * Generates a "real" schemaName, using @{link #getDefaultSchemaName()}
     * if the schemaName param is null or blank.
     * Can still return null if {@link #getDefaultSchemaName()} returns null.
     *
     * @param schemaName The schemaName, can be null.
     * @return The real schemaName to use, can be null
     */
    public String generateRealSchemaName(String schemaName) {
        schemaName = trimToNull(schemaName);
        if(schemaName == null) {
            schemaName = getDefaultSchemaName();
        }
        return schemaName;
    }

    @Override
    public boolean isCorrectDatabaseImplementation(DatabaseConnection conn) throws DatabaseException {
        return delegateDatabase.isCorrectDatabaseImplementation(conn);
    }

    @Override
    public String getDefaultDriver(String url) {
        return delegateDatabase.getDefaultDriver(url);
    }

    @Override
    public DatabaseConnection getConnection() {
        return delegateDatabase.getConnection();
    }

    @Override
    public void setConnection(DatabaseConnection conn) {
        delegateDatabase.setConnection(conn);
    }

    @Override
    public boolean requiresUsername() {
        return delegateDatabase.requiresUsername();
    }

    @Override
    public boolean requiresPassword() {
        return delegateDatabase.requiresPassword();
    }

    @Override
    public boolean getAutoCommitMode() {
        return delegateDatabase.getAutoCommitMode();
    }

    @Override
    public boolean supportsDDLInTransaction() {
        return delegateDatabase.supportsDDLInTransaction();
    }

    @Override
    public String getDatabaseProductName() {
        return delegateDatabase.getDatabaseProductName();
    }

    @Override
    public String getDatabaseProductVersion() throws DatabaseException {
        return delegateDatabase.getDatabaseProductVersion();
    }

    @Override
    public int getDatabaseMajorVersion() throws DatabaseException {
        return delegateDatabase.getDatabaseMajorVersion();
    }

    @Override
    public int getDatabaseMinorVersion() throws DatabaseException {
        return delegateDatabase.getDatabaseMinorVersion();
    }

    @Override
    public String getTypeName() {
        return delegateDatabase.getTypeName();
    }

    @Override
    public String getDefaultCatalogName() throws DatabaseException {
        return delegateDatabase.getDefaultCatalogName();
    }

    @Override
    public String getDefaultSchemaName() {
        return delegateDatabase.getDefaultSchemaName();
    }

    @Override
    public String getLiquibaseSchemaName() {
        return delegateDatabase.getLiquibaseSchemaName();
    }

    @Override
    public void setDefaultSchemaName(String schemaName) throws DatabaseException {
        delegateDatabase.setDefaultSchemaName(schemaName);
    }

    @Override
    public boolean supportsInitiallyDeferrableColumns() {
        return delegateDatabase.supportsInitiallyDeferrableColumns();
    }

    @Override
    public boolean supportsSequences() {
        return delegateDatabase.supportsSequences();
    }

    @Override
    public boolean supportsDropTableCascadeConstraints() {
        return delegateDatabase.supportsDropTableCascadeConstraints();
    }

    @Override
    public boolean supportsAutoIncrement() {
        return delegateDatabase.supportsAutoIncrement();
    }

    @Override
    public String getDateLiteral(String isoDate) {
        return delegateDatabase.getDateLiteral(isoDate);
    }

    @Override
    public String getCurrentDateTimeFunction() {
        return delegateDatabase.getCurrentDateTimeFunction();
    }

    @Override
    public void setCurrentDateTimeFunction(String function) {
        delegateDatabase.setCurrentDateTimeFunction(function);
    }

    @Override
    public String getLineComment() {
        return delegateDatabase.getLineComment();
    }

    @Override
    public String getAutoIncrementClause(BigInteger startWith, BigInteger incrementBy) {
        return delegateDatabase.getAutoIncrementClause(startWith, incrementBy);
    }

    @Override
    public String getDatabaseChangeLogTableName() {
        return delegateDatabase.getDatabaseChangeLogTableName();
    }

    @Override
    public String getDatabaseChangeLogLockTableName() {
        return delegateDatabase.getDatabaseChangeLogLockTableName();
    }

    @Override
    public void setDatabaseChangeLogTableName(String tableName) {
        delegateDatabase.setDatabaseChangeLogTableName(tableName);
    }

    @Override
    public void setDatabaseChangeLogLockTableName(String tableName) {
        delegateDatabase.setDatabaseChangeLogLockTableName(tableName);
    }

    @Override
    public String getConcatSql(String... values) {
        return delegateDatabase.getConcatSql(values);
    }

    @Override
    public boolean hasDatabaseChangeLogTable() throws DatabaseException {
        return delegateDatabase.hasDatabaseChangeLogTable();
    }

    @Override
    public void setCanCacheLiquibaseTableInfo(boolean canCacheLiquibaseTableInfo) {
        delegateDatabase.setCanCacheLiquibaseTableInfo(canCacheLiquibaseTableInfo);
    }

    @Override
    public boolean hasDatabaseChangeLogLockTable() throws DatabaseException {
        return delegateDatabase.hasDatabaseChangeLogLockTable();
    }

    @Override
    public void checkDatabaseChangeLogTable(boolean updateExistingNullChecksums, DatabaseChangeLog databaseChangeLog, String[] contexts) throws DatabaseException {
        delegateDatabase.checkDatabaseChangeLogTable(updateExistingNullChecksums, databaseChangeLog, contexts);
    }

    @Override
    public void checkDatabaseChangeLogLockTable() throws DatabaseException {
        delegateDatabase.checkDatabaseChangeLogLockTable();
    }

    @Override
    public void dropDatabaseObjects(String schema) throws DatabaseException {
        delegateDatabase.dropDatabaseObjects(schema);
    }

    @Override
    public void tag(String tagString) throws DatabaseException {
        delegateDatabase.tag(tagString);
    }

    @Override
    public boolean doesTagExist(String tag) throws DatabaseException {
        return delegateDatabase.doesTagExist(tag);
    }

    @Override
    public boolean isSystemTable(String catalogName, String schemaName, String tableName) {
        return delegateDatabase.isSystemTable(catalogName, schemaName, tableName);
    }

    @Override
    public boolean isLiquibaseTable(String tableName) {
        return delegateDatabase.isLiquibaseTable(tableName);
    }

    @Override
    public boolean shouldQuoteValue(String value) {
        return delegateDatabase.shouldQuoteValue(value);
    }

    @Override
    public boolean supportsTablespaces() {
        return delegateDatabase.supportsTablespaces();
    }

    @Override
    public String getViewDefinition(String schemaName, String name) throws DatabaseException {
        return delegateDatabase.getViewDefinition(schemaName, name);
    }

    @Override
    public boolean isSystemView(String catalogName, String schemaName, String name) {
        return delegateDatabase.isSystemView(catalogName, schemaName, name);
    }

    @Override
    public String getDateLiteral(Date date) {
        return delegateDatabase.getDateLiteral(date);
    }

    @Override
    public String getTimeLiteral(Time time) {
        return delegateDatabase.getTimeLiteral(time);
    }

    @Override
    public String getDateTimeLiteral(Timestamp timeStamp) {
        return delegateDatabase.getDateTimeLiteral(timeStamp);
    }

    @Override
    public String getDateLiteral(java.util.Date defaultDateValue) {
        return delegateDatabase.getDateLiteral(defaultDateValue);
    }

    @Override
    public String escapeTableName(String schemaName, String tableName) {
        return delegateDatabase.escapeTableName(schemaName, tableName);
    }

    @Override
    public String escapeIndexName(String schemaName, String indexName) {
        return delegateDatabase.escapeIndexName(schemaName, indexName);
    }

    @Override
    public String escapeDatabaseObject(String objectName) {
        return delegateDatabase.escapeDatabaseObject(objectName);
    }

    @Override
    public String escapeColumnName(String schemaName, String tableName, String columnName) {
        return delegateDatabase.escapeColumnName(schemaName, tableName, columnName);
    }

    @Override
    public String escapeColumnNameList(String columnNames) {
        return delegateDatabase.escapeColumnNameList(columnNames);
    }

    @Override
    public String convertRequestedSchemaToSchema(String requestedSchema) throws DatabaseException {
        return delegateDatabase.convertRequestedSchemaToSchema(requestedSchema);
    }

    @Override
    public String convertRequestedSchemaToCatalog(String requestedSchema) throws DatabaseException {
        return delegateDatabase.convertRequestedSchemaToCatalog(requestedSchema);
    }

    @Override
    public boolean supportsSchemas() {
        return delegateDatabase.supportsSchemas();
    }

    @Override
    public String generatePrimaryKeyName(String tableName) {
        return delegateDatabase.generatePrimaryKeyName(tableName);
    }

    public String escapeRemoteObject(String server, String database, String schemaName, String synonymName) {
        StringBuilder builder = new StringBuilder();
        if(supportsRemoteSynonyms() && trimToNull(server) != null) {
            builder.append(escapeDatabaseObject(server)).append('.');
        }

        if(supportsRemoteSynonyms() && trimToNull(database) != null) {
            builder.append(escapeDatabaseObject(database)).append('.');
        }

        builder.append(escapeTableName(schemaName, synonymName));
        return builder.toString();
    }

    public String escapeSynonymName(String schemaName, String synonymName) {
        if (schemaName == null) {
            schemaName = getDefaultSchemaName();
        }

        if (StringUtils.trimToNull(schemaName) == null) {
            return escapeDatabaseObject(synonymName);
        } else {
            return escapeDatabaseObject(schemaName) + "." + escapeDatabaseObject(synonymName);
        }
    }

    @Override
    public String escapeSequenceName(String schemaName, String sequenceName) {
        return delegateDatabase.escapeSequenceName(schemaName, sequenceName);
    }

    @Override
    public String escapeViewName(String schemaName, String viewName) {
        return delegateDatabase.escapeViewName(schemaName, viewName);
    }

    @Override
    public ChangeSet.RunStatus getRunStatus(ChangeSet changeSet) throws DatabaseException, DatabaseHistoryException {
        return delegateDatabase.getRunStatus(changeSet);
    }

    @Override
    public RanChangeSet getRanChangeSet(ChangeSet changeSet) throws DatabaseException, DatabaseHistoryException {
        return delegateDatabase.getRanChangeSet(changeSet);
    }

    @Override
    public void markChangeSetExecStatus(ChangeSet changeSet, ChangeSet.ExecType execType) throws DatabaseException {
        delegateDatabase.markChangeSetExecStatus(changeSet, execType);
    }

    @Override
    public List<RanChangeSet> getRanChangeSetList() throws DatabaseException {
        return delegateDatabase.getRanChangeSetList();
    }

    @Override
    public java.util.Date getRanDate(ChangeSet changeSet) throws DatabaseException, DatabaseHistoryException {
        return delegateDatabase.getRanDate(changeSet);
    }

    @Override
    public void removeRanStatus(ChangeSet changeSet) throws DatabaseException {
        delegateDatabase.removeRanStatus(changeSet);
    }

    @Override
    public void commit() throws DatabaseException {
        delegateDatabase.commit();
    }

    @Override
    public void rollback() throws DatabaseException {
        delegateDatabase.rollback();
    }

    @Override
    public String escapeStringForDatabase(String string) {
        return delegateDatabase.escapeStringForDatabase(string);
    }

    @Override
    public void close() throws DatabaseException {
        delegateDatabase.close();
    }

    @Override
    public boolean supportsRestrictForeignKeys() {
        return delegateDatabase.supportsRestrictForeignKeys();
    }

    @Override
    public String escapeConstraintName(String constraintName) {
        return delegateDatabase.escapeConstraintName(constraintName);
    }

    @Override
    public boolean isAutoCommit() throws DatabaseException {
        return delegateDatabase.isAutoCommit();
    }

    @Override
    public void setAutoCommit(boolean b) throws DatabaseException {
        delegateDatabase.setAutoCommit(b);
    }

    @Override
    public boolean isLocalDatabase() throws DatabaseException {
        return delegateDatabase.isLocalDatabase();
    }

    @Override
    public void executeStatements(Change change, DatabaseChangeLog changeLog, List<SqlVisitor> sqlVisitors) throws LiquibaseException {
        delegateDatabase.executeStatements(change, changeLog, sqlVisitors);
    }

    @Override
    public void execute(SqlStatement[] statements, List<SqlVisitor> sqlVisitors) throws LiquibaseException {
        delegateDatabase.execute(statements, sqlVisitors);
    }

    @Override
    public void saveStatements(Change change, List<SqlVisitor> sqlVisitors, Writer writer) throws IOException, LiquibaseException {
        delegateDatabase.saveStatements(change, sqlVisitors, writer);
    }

    @Override
    public void executeRollbackStatements(Change change, List<SqlVisitor> sqlVisitors) throws LiquibaseException{
        delegateDatabase.executeRollbackStatements(change, sqlVisitors);
    }

    @Override
    public void saveRollbackStatement(Change change, List<SqlVisitor> sqlVisitors, Writer writer) throws IOException, LiquibaseException {
        delegateDatabase.saveRollbackStatement(change, sqlVisitors, writer);
    }

    @Override
    public int getNextChangeSetSequenceValue() throws LiquibaseException {
        return delegateDatabase.getNextChangeSetSequenceValue();
    }

    @Override
    public java.util.Date parseDate(String dateAsString) throws DateParseException {
        return delegateDatabase.parseDate(dateAsString);
    }

    @Override
    public List<DatabaseFunction> getDatabaseFunctions() {
        return delegateDatabase.getDatabaseFunctions();
    }

    @Override
    public void reset() {
        delegateDatabase.reset();
    }

    @Override
    public boolean supportsForeignKeyDisable() {
        return delegateDatabase.supportsForeignKeyDisable();
    }

    @Override
    public boolean disableForeignKeyChecks() throws DatabaseException {
        return delegateDatabase.disableForeignKeyChecks();
    }

    @Override
    public void enableForeignKeyChecks() throws DatabaseException {
        delegateDatabase.enableForeignKeyChecks();
    }

    @Override
    public boolean isReservedWord(String string) {
        return delegateDatabase.isReservedWord(string);
    }

    @Override
    public DatabaseObject[] getContainingObjects() {
        return delegateDatabase.getContainingObjects();
    }

    @Override
    public int getPriority() {
        return delegateDatabase.getPriority();
    }
}
