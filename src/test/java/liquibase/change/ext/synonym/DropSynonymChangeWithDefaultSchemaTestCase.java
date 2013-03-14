package liquibase.change.ext.synonym;

import static liquibase.change.ext.synonym.Constants.SUPPORTS_SYNONYMS;
import static liquibase.test.TestConstants.SCHEMA;
import static liquibase.test.TestConstants.SYNONYM;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import liquibase.database.Database;
import liquibase.exception.DatabaseException;
import liquibase.exception.SetupException;
import liquibase.sql.Sql;
import liquibase.statement.SqlStatement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class DropSynonymChangeWithDefaultSchemaTestCase {

    private DropSynonymChange dropSynonymChange;
    private DropSynonymGenerator dropSynonymGenerator;

    @Before
    public void before() {
        dropSynonymChange = new DropSynonymChange();
        dropSynonymGenerator = new DropSynonymGenerator();
    }

    @After
    public void after() {
        dropSynonymChange = null;
        dropSynonymGenerator = null;
    }

    @Test
    public void testDropWithDefaultSchema()
            throws SetupException, IllegalAccessException, InstantiationException, DatabaseException {

        for(Class<? extends Database> clazz : SUPPORTS_SYNONYMS) {
            Database database = clazz.newInstance();
            database.setDefaultSchemaName(SCHEMA);
            testDropWithDefaultSchema(database);
        }
    }

    private void testDropWithDefaultSchema(Database database) throws SetupException {

        dropSynonymChange.setSynonymName(SYNONYM);
        dropSynonymChange.init();
        SqlStatement[] statements = dropSynonymChange.generateStatements(database);
        assertTrue(statements.length == 1);
        SqlStatement sqlStatement = statements[0];
        assertTrue(sqlStatement instanceof DropSynonymStatement);
        DropSynonymStatement statement = (DropSynonymStatement)sqlStatement;

        Sql[] resultsArray = dropSynonymGenerator.generateSql(statement, database, null);
        assertNotNull(resultsArray);
        assertTrue(resultsArray.length == 1);
        String sql = resultsArray[0].toSql();
        assertTrue(sql.contains(SYNONYM));
        assertTrue(sql.indexOf("DROP") < sql.indexOf(SYNONYM));
        assertTrue(sql.indexOf(SCHEMA) < sql.indexOf(SYNONYM));
    }
}
