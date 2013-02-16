package liquibase.change.ext.synonym;

import static liquibase.test.LiquibaseTestUtil.updateChangeLog;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;

import liquibase.test.DbConnectionRule;
import org.junit.Test;

/**
 * This is a base test case to test common synonym functionality
 * for all databases.  Specialized functionality, such as oracles "public"
 * synonyms, should be in an oracle specific subclass.
 *
 */
public abstract class AbstractXmlTestCase {

    @Test
    public void testSimpleSynonym() throws Throwable {
        Connection connection = getConnection();
        String result = updateChangeLog(connection, "simple.xml");
        assertNotNull(result);
        assertTrue(result.length() > 0);
        assertTrue(result.toLowerCase().contains("synonym"));
    }


    public Connection getConnection() {
        return getDbConnectionRule().getConnection();
    }

    public abstract DbConnectionRule getDbConnectionRule();
}
