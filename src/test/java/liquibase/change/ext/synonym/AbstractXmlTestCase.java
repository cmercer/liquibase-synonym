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
    public void testSimpleCreateSynonym() throws Throwable {
        Connection connection = getConnection();
        String result = updateChangeLog(connection, "simple-create.xml");
        result = result.toLowerCase();
        assertNotNull(result);
        assertTrue(result.length() > 0);
        assertTrue(result.contains("synonym"));
        assertTrue(result.contains("create"));
        assertTrue(result.indexOf("create") < result.indexOf("synonym"));
        assertTrue(result.contains("test-synonym"));
    }

    @Test
    public void testSimpleDropSynonym() throws Throwable{
        Connection connection = getConnection();
        String result = updateChangeLog(connection, "simple-drop.xml");
        result = result.toLowerCase();
        assertNotNull(result);
        assertTrue(result.length() > 0);
        assertTrue(result.contains("synonym"));
        assertTrue(result.contains("drop"));
        assertTrue(result.indexOf("drop") < result.indexOf("synonym"));
        assertTrue(result.contains("test-synonym"));
    }

    @Test
    public void testDropWithPublicSynonym() throws Throwable{
        Connection connection = getConnection();
        String result = updateChangeLog(connection, "drop-with-public.xml");
        result = result.toLowerCase();
        assertNotNull(result);
        assertTrue(result.length() > 0);
        assertTrue(result.contains("synonym"));
        assertTrue(result.contains("drop"));
        assertTrue(result.indexOf("drop") < result.indexOf("synonym"));
        assertTrue(result.contains("test-synonym"));
        assertTrue(result.contains("test-schema"));
        assertTrue(result.contains("public"));
        assertTrue(result.indexOf("drop") < result.indexOf("public"));
        assertTrue(result.indexOf("public") < result.indexOf("synonym"));
    }


    public Connection getConnection() {
        return getDbConnectionRule().getConnection();
    }

    public abstract DbConnectionRule getDbConnectionRule();
}
