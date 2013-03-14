package liquibase.change.ext.synonym;

import static liquibase.change.ext.synonym.Constants.SUPPORTS_SYNONYMS;
import static liquibase.test.TestConstants.SCHEMA;
import static liquibase.test.TestConstants.SYNONYM;
import static org.junit.Assert.*;

import liquibase.database.Database;
import liquibase.exception.ValidationErrors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DropSynonymGeneratorTestCase {

    private DropSynonymGenerator dropSynonymGenerator;

    @Before
    public void before() {
        dropSynonymGenerator = new DropSynonymGenerator();
    }

    @After
    public void after() {
        dropSynonymGenerator = null;
    }

    @Test
    public void testValidate() throws IllegalAccessException, InstantiationException {
        for(Class<? extends Database> clazz : SUPPORTS_SYNONYMS) {
            Database database = clazz.newInstance();
            testGoodValidate(database);
            testBadValidate(database);
        }
    }

    public void testGoodValidate(Database database) {
        DropSynonymStatement statement = new DropSynonymStatement(null, SYNONYM, false);
        ValidationErrors errors = dropSynonymGenerator.validate(statement, database, null);
        assertNotNull(errors);
        assertFalse(errors.hasErrors());

        statement = new DropSynonymStatement(SCHEMA, SYNONYM, false);
        errors = dropSynonymGenerator.validate(statement, database, null);
        assertNotNull(errors);
        assertFalse(errors.hasErrors());
    }

    public void testBadValidate(Database database){
        DropSynonymStatement statement =  new DropSynonymStatement(null, null, false);
        ValidationErrors errors = dropSynonymGenerator.validate(statement, database, null);
        assertNotNull(errors);
        assertTrue(errors.hasErrors());
    }
}
