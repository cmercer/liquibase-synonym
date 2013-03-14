package liquibase.change.ext.synonym;

import static liquibase.test.TestConstants.*;
import static org.junit.Assert.*;

import liquibase.database.Database;
import liquibase.database.core.DB2Database;
import liquibase.database.core.DerbyDatabase;
import liquibase.database.core.HsqlDatabase;
import liquibase.database.core.InformixDatabase;
import liquibase.database.core.MSSQLDatabase;
import liquibase.database.core.MaxDBDatabase;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.SetupException;
import org.junit.Test;

public class CreateSynonymChangeTestCase {


    private static final String FULL_SOURCE_TABLE = SOURCE_SCHEMA + "." + SOURCE_DB_OBJECT;
    private static final String FULL_SYNONYM = SCHEMA + "." + SYNONYM;

    private static final String SQLSERVER_PARTIAL_SOURCE_TABLE = SOURCE_DB + "." + FULL_SOURCE_TABLE;
    private static final String SQLSERVER_FULL_SOURCE_TABLE = SOURCE_SERVER + "." + SQLSERVER_PARTIAL_SOURCE_TABLE;
    private static final String BLANK = "";

    private static final String CONFIRMATION_MESSAGE_START = "Created synonym '";
    private static final String CONFIRMATION_MESSAGE_MIDDLE = "' for object '";
    private static final String CONFIRMATION_MESSAGE_END = "'";

    private static final String CONFIRMATION_MESSAGE_ALL_COMPONENTS = CONFIRMATION_MESSAGE_START + FULL_SYNONYM + CONFIRMATION_MESSAGE_MIDDLE + FULL_SOURCE_TABLE + CONFIRMATION_MESSAGE_END;
    private static final String CONFIRMATION_MESSAGE_FULL_SYNONYM = CONFIRMATION_MESSAGE_START + FULL_SYNONYM + CONFIRMATION_MESSAGE_MIDDLE + SOURCE_DB_OBJECT + CONFIRMATION_MESSAGE_END;
    private static final String CONFIRMATION_MESSAGE_FULL_SOURCE_TABLE = CONFIRMATION_MESSAGE_START + SYNONYM + CONFIRMATION_MESSAGE_MIDDLE + FULL_SOURCE_TABLE + CONFIRMATION_MESSAGE_END;
    private static final String CONFIRMATION_MESSAGE_SQLSERVER_PARTIAL_SOURCE_TABLE = CONFIRMATION_MESSAGE_START + FULL_SYNONYM + CONFIRMATION_MESSAGE_MIDDLE + SQLSERVER_PARTIAL_SOURCE_TABLE + CONFIRMATION_MESSAGE_END;
    private static final String CONFIRMATION_MESSAGE_SQLSERVER_FULL_SOURCE_TABLE = CONFIRMATION_MESSAGE_START + FULL_SYNONYM + CONFIRMATION_MESSAGE_MIDDLE + SQLSERVER_FULL_SOURCE_TABLE + CONFIRMATION_MESSAGE_END;

    @Test(expected = SetupException.class)
    public void testGetConfirmationMessageWithNullSourceTable() throws SetupException {
        CreateSynonymChange createSynonymChange =
                new CreateSynonymChange(SOURCE_SCHEMA, null, SCHEMA, SYNONYM);
        createSynonymChange.init();
        createSynonymChange.getConfirmationMessage();
    }

    @Test(expected = SetupException.class)
    public void testGetConfirmationMessageWithNullSynonym() throws SetupException {
        CreateSynonymChange change = new CreateSynonymChange(SOURCE_SCHEMA, SOURCE_DB_OBJECT, SCHEMA, null);
        change.init();
        change.getConfirmationMessage();
    }

    @Test(expected = SetupException.class)
    public void testGetConfirmationMessageWithBlankSourceTable() throws SetupException {
        CreateSynonymChange change = new CreateSynonymChange(SOURCE_SCHEMA, BLANK, SCHEMA, SYNONYM);
        change.init();
        change.getConfirmationMessage();
    }

    @Test(expected = SetupException.class)
    public void testGetConfirmationMessageWithBlankSynonym() throws SetupException {
        CreateSynonymChange change = new CreateSynonymChange(SOURCE_SCHEMA, SOURCE_DB_OBJECT, SCHEMA, BLANK);
        change.init();
        change.getConfirmationMessage();
    }

    @Test(expected = SetupException.class)
    public void testGetConfirmationMessageWithIncorrectSqlServerParams() throws SetupException {
        CreateSynonymChange change;
        change = new CreateSynonymChange(
            SOURCE_SERVER, BLANK, SOURCE_SCHEMA, SOURCE_DB_OBJECT, SCHEMA, SYNONYM, false, false
        );
        change.init();
        change.getConfirmationMessage();
    }

    @Test
    public void testGetConfirmationMessage() {
        String message;
        CreateSynonymChange change;

        change = new CreateSynonymChange(SOURCE_SCHEMA, SOURCE_DB_OBJECT, SCHEMA, SYNONYM);
        message = change.getConfirmationMessage();
        assertEquals(
                "Confirmation message should properly reflect all components",
                CONFIRMATION_MESSAGE_ALL_COMPONENTS,
                message
        );

        change = new CreateSynonymChange(null, SOURCE_DB_OBJECT, SCHEMA, SYNONYM);
        message = change.getConfirmationMessage();
        assertEquals(
                "Confirmation message should properly reflect fully-qualified synonym",
                CONFIRMATION_MESSAGE_FULL_SYNONYM,
                message
        );

        change = new CreateSynonymChange(SOURCE_SCHEMA, SOURCE_DB_OBJECT, null, SYNONYM);
        message = change.getConfirmationMessage();
        assertEquals(
                "Confirmation message should properly reflect fully-qualified source table",
                CONFIRMATION_MESSAGE_FULL_SOURCE_TABLE,
                message
        );

        change = new CreateSynonymChange(
            BLANK, SOURCE_DB, SOURCE_SCHEMA, SOURCE_DB_OBJECT, SCHEMA, SYNONYM, false, false
        );
        message = change.getConfirmationMessage();
        assertEquals(
                "Confirmation message should properly reflect partially-qualified SQL Server source table",
                CONFIRMATION_MESSAGE_SQLSERVER_PARTIAL_SOURCE_TABLE,
                message
        );

        change = new CreateSynonymChange(
                SOURCE_SERVER, SOURCE_DB, SOURCE_SCHEMA, SOURCE_DB_OBJECT, SCHEMA, SYNONYM, false, false
        );
        message = change.getConfirmationMessage();
        assertEquals(
                "Confirmation message should properly reflect fully-qualified SQL Server source table",
                CONFIRMATION_MESSAGE_SQLSERVER_FULL_SOURCE_TABLE,
                message
        );
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
        CreateSynonymChange createSynonymChange = new CreateSynonymChange(SOURCE_SCHEMA, SOURCE_DB_OBJECT, SCHEMA, SYNONYM);
        assertTrue("CreateSynonymChange should support Oracle", createSynonymChange.supportsDatabase(oracle));
        assertTrue("CreateSynonymChange should support MSSQL", createSynonymChange.supportsDatabase(mssql));
        assertTrue("CreateSynonymChange should support DB2", createSynonymChange.supportsDatabase(db2));
        assertTrue("CreateSynonymChange should support Derby", createSynonymChange.supportsDatabase(derby));
        assertTrue("CreateSynonymChange should support Informix", createSynonymChange.supportsDatabase(informix));
        assertTrue("CreateSynonymChange should support MaxDB", createSynonymChange.supportsDatabase(maxdb));
        assertFalse("CreateSynonymChange should not support HSQL", createSynonymChange.supportsDatabase(hsql));
    }
}
