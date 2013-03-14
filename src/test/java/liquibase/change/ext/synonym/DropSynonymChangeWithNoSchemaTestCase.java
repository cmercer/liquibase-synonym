package liquibase.change.ext.synonym;

import static liquibase.change.ext.synonym.Constants.SUPPORTS_SYNONYMS;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import liquibase.database.Database;
import liquibase.exception.SetupException;
import liquibase.sql.Sql;
import liquibase.statement.SqlStatement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DropSynonymChangeWithNoSchemaTestCase {

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
    public void testDropWithNoSchemaDefined() throws SetupException, IllegalAccessException, InstantiationException {
        for(Class<? extends Database> clazz : SUPPORTS_SYNONYMS) {
            Database database = clazz.newInstance();
            testDropWithNoSchemaDefined(database);
        }
    }


    private void testDropWithNoSchemaDefined(Database database) throws SetupException {
        String name = "synonymName";
        dropSynonymChange.setSynonymName(name);
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
        assertTrue(sql.contains(name));
        assertTrue(sql.indexOf("DROP") < sql.indexOf(name));
    }
}
