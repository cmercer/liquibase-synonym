package liquibase.change.ext.synonym;

import static liquibase.test.TestConstants.SCHEMA;
import static liquibase.test.TestConstants.SYNONYM;
import static org.junit.Assert.*;

import liquibase.exception.SetupException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DropSynonymChangeTestCase {

    private DropSynonymChange dropSynonymChange;

    @Before
    public void before() {
        dropSynonymChange = new DropSynonymChange();
}

    @After
    public void after() {
        dropSynonymChange = null;
    }

    @Test(expected = SetupException.class)
    public void testNullSynonym() throws SetupException{
        dropSynonymChange.init();
    }

    @Test(expected = SetupException.class)
    public void testBlankSynonym() throws SetupException {
        dropSynonymChange.setSchemaName("");
        dropSynonymChange.init();
    }

    @Test(expected = SetupException.class)
    public void testWhitespaceSynonym() throws SetupException {
        dropSynonymChange.setSchemaName("   ");
        dropSynonymChange.init();
    }

    @Test
    public void testSetSynonym() throws SetupException {
        dropSynonymChange.setSynonymName(SYNONYM);
        dropSynonymChange.init();
        assertEquals(SYNONYM, dropSynonymChange.getSynonymName());
    }

    @Test
    public void testSetSchema() throws SetupException {
        dropSynonymChange.setSchemaName(SCHEMA);
        dropSynonymChange.setSynonymName(SYNONYM);
        dropSynonymChange.init();
        assertEquals(SYNONYM, dropSynonymChange.getSynonymName());
        assertEquals(SCHEMA, dropSynonymChange.getSchemaName());
    }

    @Test
    public void testPublicSynonym() throws Exception{
        boolean publicSynonym = true;
        dropSynonymChange.setSchemaName(SCHEMA);
        dropSynonymChange.setSynonymName(SYNONYM);
        dropSynonymChange.setPublicSynonym(publicSynonym);
        dropSynonymChange.init();
        assertEquals(SYNONYM, dropSynonymChange.getSynonymName());
        assertEquals(SCHEMA, dropSynonymChange.getSchemaName());
        assertEquals(publicSynonym, dropSynonymChange.isPublicSynonym());

        publicSynonym = false;
        dropSynonymChange.setPublicSynonym(publicSynonym);
        dropSynonymChange.init();
        assertEquals(publicSynonym, dropSynonymChange.isPublicSynonym());
    }

    @Test
    public void testGetConfirmationMessage() throws SetupException {

        dropSynonymChange.setSynonymName(SYNONYM);
        dropSynonymChange.init();
        String message = dropSynonymChange.getConfirmationMessage();
        assertTrue(message.contains(SYNONYM));
        assertFalse(message.contains(SCHEMA));

        dropSynonymChange.setSchemaName(SCHEMA);
        message = dropSynonymChange.getConfirmationMessage();
        assertTrue(message.contains(SYNONYM));
        assertTrue(message.contains(SCHEMA));
        assertTrue(message.indexOf(SCHEMA) < message.indexOf(SYNONYM));
    }
}
