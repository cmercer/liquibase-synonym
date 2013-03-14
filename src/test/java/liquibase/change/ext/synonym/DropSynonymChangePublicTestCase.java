package liquibase.change.ext.synonym;

import static liquibase.change.ext.synonym.Constants.SUPPORTS_PUBLIC;
import static liquibase.change.ext.synonym.Constants.SUPPORTS_SYNONYMS;
import static liquibase.test.TestConstants.*;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import liquibase.database.Database;
import liquibase.exception.SetupException;
import liquibase.sql.Sql;
import liquibase.statement.SqlStatement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class DropSynonymChangePublicTestCase {

    private DropSynonymGenerator dropSynonymGenerator;
    private DropSynonymChange dropSynonymChange;

    @Before
    public void before() {
        dropSynonymGenerator = new DropSynonymGenerator();
        dropSynonymChange = new DropSynonymChange();
    }

    @After
    public void after() {
        dropSynonymGenerator = null;
        dropSynonymChange = null;
    }

    @Test
    public void testPublic() throws IllegalAccessException, InstantiationException, SetupException {
        for(Class<? extends Database> clazz : SUPPORTS_PUBLIC) {
            Database database = clazz.newInstance();
            testPublic(database);
        }
    }

    public void testPublic(Database database) throws SetupException {
        dropSynonymChange.setPublicSynonym(true);
        dropSynonymChange.setSchemaName(SCHEMA);
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
        assertTrue(sql.contains(PUBLIC));
        assertTrue(sql.indexOf(PUBLIC) < sql.indexOf(SCHEMA));
        assertTrue(sql.indexOf(SCHEMA) < sql.indexOf(SYNONYM));
    }

    @Test
    public void testNotSupportPublic() throws IllegalAccessException, InstantiationException, SetupException {
        Set<Class<? extends Database>> notSupportPublic =
                new HashSet<Class<? extends Database>> (SUPPORTS_SYNONYMS);
        notSupportPublic.removeAll(SUPPORTS_PUBLIC);
        for(Class<? extends Database> clazz : notSupportPublic) {
            Database database = clazz.newInstance();
            testNotSupportsPublic(database);
        }
    }

    public void testNotSupportsPublic(Database database) throws SetupException {
        dropSynonymChange.setPublicSynonym(true);
        dropSynonymChange.setSchemaName(SCHEMA);
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
        assertFalse(sql.contains(PUBLIC));
        assertTrue(sql.indexOf(SCHEMA) < sql.indexOf(SYNONYM));
    }
}
