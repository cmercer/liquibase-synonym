package liquibase.change.ext.synonym;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.sql.Connection;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.test.DbConnectionRule;
import org.junit.Rule;
import org.junit.Test;

/**
 *
 */
public class DerbyTestCase {

    @Rule
    public DbConnectionRule dbConnectionRule = new DbConnectionRule(
            "jdbc:derby:memory:testDB;create=true", "", ""
    );

    @Test
    public void testSimpleSynonym() throws Throwable {
        Connection connection = dbConnectionRule.getConnection();
        JdbcConnection jdbcConnection =  new JdbcConnection(connection);
        DatabaseFactory factory =  DatabaseFactory.getInstance();
        Database database = factory.findCorrectDatabaseImplementation(jdbcConnection);
        Liquibase liquibase = new Liquibase(
                "simple.xml",
                new ClassLoaderResourceAccessor(DerbyTestCase.class.getClassLoader()),
                database);
        StringWriter sw = new StringWriter();
        liquibase.update(null, sw);
        System.out.println(sw.toString());
        assertNotNull(sw.toString());
        assertTrue(sw.toString().length() > 0);
        assertTrue(sw.toString().toLowerCase().contains("synonym"));
    }
}
