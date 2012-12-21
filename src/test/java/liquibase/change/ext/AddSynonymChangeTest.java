package liquibase.change.ext;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AddSynonymChangeTest {

    private static final String SOURCE_SCHEMA = "SOURCE_SCHEMA";
    private static final String SOURCE_TABLE = "SOURCE_TABLE";
    private static final String FULL_SOURCE_TABLE = SOURCE_SCHEMA + "." + SOURCE_TABLE;
    private static final String SCHEMA = "SCHEMA";
    private static final String SYNONYM = "SYNONYM";
    private static final String FULL_SYNONYM = SCHEMA + "." + SYNONYM;
    private static final String BLANK = "";

    private static final String CONFIRMATION_MESSAGE_START = "Created synonym '";
    private static final String CONFIRMATION_MESSAGE_MIDDLE = "' for table '";
    private static final String CONFIRMATION_MESSAGE_END = "'";

    private static final String CONFIRMATION_MESSAGE_ALL_COMPONENTS = CONFIRMATION_MESSAGE_START + FULL_SYNONYM + CONFIRMATION_MESSAGE_MIDDLE + FULL_SOURCE_TABLE + CONFIRMATION_MESSAGE_END;
    private static final String CONFIRMATION_MESSAGE_FULL_SYNONYM = CONFIRMATION_MESSAGE_START + FULL_SYNONYM + CONFIRMATION_MESSAGE_MIDDLE + SOURCE_TABLE + CONFIRMATION_MESSAGE_END;
    private static final String CONFIRMATION_MESSAGE_FULL_SOURCE_TABLE = CONFIRMATION_MESSAGE_START + SYNONYM + CONFIRMATION_MESSAGE_MIDDLE + FULL_SOURCE_TABLE + CONFIRMATION_MESSAGE_END;

    @Test(expected = AssertionError.class)
    public void testGetConfirmationMessageWithNullSourceTable() {
        AddSynonymChange addSynonymChange = createAddSynonymChange(SOURCE_SCHEMA, null, SCHEMA, SYNONYM);
        addSynonymChange.getConfirmationMessage();
    }

    @Test(expected = AssertionError.class)
    public void testGetConfirmationMessageWithNullSynonym() {
        AddSynonymChange addSynonymChange = createAddSynonymChange(SOURCE_SCHEMA, SOURCE_TABLE, SCHEMA, null);
        addSynonymChange.getConfirmationMessage();
    }

    @Test(expected = AssertionError.class)
    public void testGetConfirmationMessageWithBlankSourceTable() {
        AddSynonymChange addSynonymChange = createAddSynonymChange(SOURCE_SCHEMA, BLANK, SCHEMA, SYNONYM);
        addSynonymChange.getConfirmationMessage();
    }

    @Test(expected = AssertionError.class)
    public void testGetConfirmationMessageWithBlankSynonym() {
        AddSynonymChange addSynonymChange = createAddSynonymChange(SOURCE_SCHEMA, SOURCE_TABLE, SCHEMA, BLANK);
        addSynonymChange.getConfirmationMessage();
    }

    @Test
    public void testGetConfirmationMessage() {
        String confirmationMessage = null;
        AddSynonymChange addSynonymChange = null;

        addSynonymChange = createAddSynonymChange(SOURCE_SCHEMA, SOURCE_TABLE, SCHEMA, SYNONYM);
        confirmationMessage = addSynonymChange.getConfirmationMessage();
        assertEquals("Confirmation message should properly reflect all components", CONFIRMATION_MESSAGE_ALL_COMPONENTS, confirmationMessage);

        addSynonymChange = createAddSynonymChange(null, SOURCE_TABLE, SCHEMA, SYNONYM);
        confirmationMessage = addSynonymChange.getConfirmationMessage();
        assertEquals("Confirmation message should properly reflect fully-qualified synonym", CONFIRMATION_MESSAGE_FULL_SYNONYM, confirmationMessage);

        addSynonymChange = createAddSynonymChange(SOURCE_SCHEMA, SOURCE_TABLE, null, SYNONYM);
        confirmationMessage = addSynonymChange.getConfirmationMessage();
        assertEquals("Confirmation message should properly reflect fully-qualified source table", CONFIRMATION_MESSAGE_FULL_SOURCE_TABLE, confirmationMessage);
    }

    private AddSynonymChange createAddSynonymChange(String sourceSchema, String sourceTable, String schema, String synonym) {
        AddSynonymChange addSynonymChange = new AddSynonymChange();
        addSynonymChange.setSourceSchemaName(sourceSchema);
        addSynonymChange.setSourceTableName(sourceTable);
        addSynonymChange.setSchemaName(schema);
        addSynonymChange.setSynonymName(synonym);
        return addSynonymChange;
    }

}
