package liquibase.change.ext.synonym;

import liquibase.database.Database;
import liquibase.database.core.*;
import liquibase.exception.SetupException;
import liquibase.statement.SqlStatement;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreateSynonymChangeTest {

    private static final String SOURCE_SCHEMA = "SOURCE_SCHEMA";
    private static final String SOURCE_TABLE = "SOURCE_TABLE";
    private static final String FULL_SOURCE_TABLE = SOURCE_SCHEMA + "." + SOURCE_TABLE;
    private static final String SCHEMA = "SCHEMA";
    private static final String SYNONYM = "SYNONYM";
    private static final String FULL_SYNONYM = SCHEMA + "." + SYNONYM;
    private static final String SOURCE_SERVER = "SOURCE_SERVER";
    private static final String SOURCE_DATABASE = "SOURCE_DATABASE";
    private static final String SQLSERVER_PARTIAL_SOURCE_TABLE = SOURCE_DATABASE + "." + FULL_SOURCE_TABLE;
    private static final String SQLSERVER_FULL_SOURCE_TABLE = SOURCE_SERVER + "." + SQLSERVER_PARTIAL_SOURCE_TABLE;
    private static final String BLANK = "";

    private static final String CONFIRMATION_MESSAGE_START = "Created synonym '";
    private static final String CONFIRMATION_MESSAGE_MIDDLE = "' for table '";
    private static final String CONFIRMATION_MESSAGE_END = "'";

    private static final String CONFIRMATION_MESSAGE_ALL_COMPONENTS = CONFIRMATION_MESSAGE_START + FULL_SYNONYM + CONFIRMATION_MESSAGE_MIDDLE + FULL_SOURCE_TABLE + CONFIRMATION_MESSAGE_END;
    private static final String CONFIRMATION_MESSAGE_FULL_SYNONYM = CONFIRMATION_MESSAGE_START + FULL_SYNONYM + CONFIRMATION_MESSAGE_MIDDLE + SOURCE_TABLE + CONFIRMATION_MESSAGE_END;
    private static final String CONFIRMATION_MESSAGE_FULL_SOURCE_TABLE = CONFIRMATION_MESSAGE_START + SYNONYM + CONFIRMATION_MESSAGE_MIDDLE + FULL_SOURCE_TABLE + CONFIRMATION_MESSAGE_END;
    private static final String CONFIRMATION_MESSAGE_SQLSERVER_PARTIAL_SOURCE_TABLE = CONFIRMATION_MESSAGE_START + FULL_SYNONYM + CONFIRMATION_MESSAGE_MIDDLE + SQLSERVER_PARTIAL_SOURCE_TABLE + CONFIRMATION_MESSAGE_END;
    private static final String CONFIRMATION_MESSAGE_SQLSERVER_FULL_SOURCE_TABLE = CONFIRMATION_MESSAGE_START + FULL_SYNONYM + CONFIRMATION_MESSAGE_MIDDLE + SQLSERVER_FULL_SOURCE_TABLE + CONFIRMATION_MESSAGE_END;

    @Test(expected = SetupException.class)
    public void testGetConfirmationMessageWithNullSourceTable() throws SetupException {
        CreateSynonymChange createSynonymChange = new CreateSynonymChange(SOURCE_SCHEMA, null, SCHEMA, SYNONYM);
        createSynonymChange.init();
        createSynonymChange.getConfirmationMessage();
    }

    @Test(expected = SetupException.class)
    public void testGetConfirmationMessageWithNullSynonym() throws SetupException {
        CreateSynonymChange createSynonymChange = new CreateSynonymChange(SOURCE_SCHEMA, SOURCE_TABLE, SCHEMA, null);
        createSynonymChange.init();
        createSynonymChange.getConfirmationMessage();
    }

    @Test(expected = SetupException.class)
    public void testGetConfirmationMessageWithBlankSourceTable() throws SetupException {
        CreateSynonymChange createSynonymChange = new CreateSynonymChange(SOURCE_SCHEMA, BLANK, SCHEMA, SYNONYM);
        createSynonymChange.init();
        createSynonymChange.getConfirmationMessage();
    }

    @Test(expected = SetupException.class)
    public void testGetConfirmationMessageWithBlankSynonym() throws SetupException {
        CreateSynonymChange createSynonymChange = new CreateSynonymChange(SOURCE_SCHEMA, SOURCE_TABLE, SCHEMA, BLANK);
        createSynonymChange.init();
        createSynonymChange.getConfirmationMessage();
    }

    @Test(expected = SetupException.class)
    public void testGetConfirmationMessageWithIncorrectSqlServerParams() throws SetupException {
        CreateSynonymChange createSynonymChange = new CreateSynonymChange(SOURCE_SERVER, BLANK, SOURCE_SCHEMA, SOURCE_TABLE, SCHEMA, SYNONYM);
        createSynonymChange.init();
        createSynonymChange.getConfirmationMessage();
    }

    @Test
    public void testGetConfirmationMessage() {
        String confirmationMessage;
        CreateSynonymChange createSynonymChange;

        createSynonymChange = new CreateSynonymChange(SOURCE_SCHEMA, SOURCE_TABLE, SCHEMA, SYNONYM);
        confirmationMessage = createSynonymChange.getConfirmationMessage();
        assertEquals("Confirmation message should properly reflect all components", CONFIRMATION_MESSAGE_ALL_COMPONENTS, confirmationMessage);

        createSynonymChange = new CreateSynonymChange(null, SOURCE_TABLE, SCHEMA, SYNONYM);
        confirmationMessage = createSynonymChange.getConfirmationMessage();
        assertEquals("Confirmation message should properly reflect fully-qualified synonym", CONFIRMATION_MESSAGE_FULL_SYNONYM, confirmationMessage);

        createSynonymChange = new CreateSynonymChange(SOURCE_SCHEMA, SOURCE_TABLE, null, SYNONYM);
        confirmationMessage = createSynonymChange.getConfirmationMessage();
        assertEquals("Confirmation message should properly reflect fully-qualified source table", CONFIRMATION_MESSAGE_FULL_SOURCE_TABLE, confirmationMessage);

        createSynonymChange = new CreateSynonymChange(BLANK, SOURCE_DATABASE, SOURCE_SCHEMA, SOURCE_TABLE, SCHEMA, SYNONYM);
        confirmationMessage = createSynonymChange.getConfirmationMessage();
        assertEquals("Confirmation message should properly reflect partially-qualified SQL Server source table", CONFIRMATION_MESSAGE_SQLSERVER_PARTIAL_SOURCE_TABLE, confirmationMessage);

        createSynonymChange = new CreateSynonymChange(SOURCE_SERVER, SOURCE_DATABASE, SOURCE_SCHEMA, SOURCE_TABLE, SCHEMA, SYNONYM);
        confirmationMessage = createSynonymChange.getConfirmationMessage();
        assertEquals("Confirmation message should properly reflect fully-qualified SQL Server source table", CONFIRMATION_MESSAGE_SQLSERVER_FULL_SOURCE_TABLE, confirmationMessage);
    }

    @Test
    public void testSupportsDatabase() {
        Database oracle = new OracleDatabase();
        Database mssql = new MSSQLDatabase();
        Database db2 = new DB2Database();
        Database derby = new DerbyDatabase();
        Database informix = new InformixDatabase();
        Database maxdb = new MaxDBDatabase();
        Database hsql = new HsqlDatabase();
        CreateSynonymChange createSynonymChange = new CreateSynonymChange(SOURCE_SCHEMA, SOURCE_TABLE, SCHEMA, SYNONYM);
        assertTrue("CreateSynonymChange should support Oracle", createSynonymChange.supportsDatabase(oracle));
        assertTrue("CreateSynonymChange should support MSSQL", createSynonymChange.supportsDatabase(mssql));
        assertTrue("CreateSynonymChange should support DB2", createSynonymChange.supportsDatabase(db2));
        assertTrue("CreateSynonymChange should support Derby", createSynonymChange.supportsDatabase(derby));
        assertTrue("CreateSynonymChange should support Informix", createSynonymChange.supportsDatabase(informix));
        assertTrue("CreateSynonymChange should support MaxDB", createSynonymChange.supportsDatabase(maxdb));
        assertFalse("CreateSynonymChange should not support HSQL", createSynonymChange.supportsDatabase(hsql));
    }

    @Test
    public void testGenerateStatements() {
        Database oracle = new OracleDatabase();
        CreateSynonymChange createSynonymChange = new CreateSynonymChange(SOURCE_SCHEMA, SOURCE_TABLE, SCHEMA, SYNONYM);
        SqlStatement[] sqlStatements = createSynonymChange.generateStatements(oracle);
        assertNotNull("generateStatements() should generate an array of SQL statements", sqlStatements);
        assertTrue("generateStatements() should generate exactly one SQL statement", sqlStatements.length == 1);

        SqlStatement statement = sqlStatements[0];
        assertNotNull(statement);
        assertTrue(statement.toString().length() > 0);
    }
}