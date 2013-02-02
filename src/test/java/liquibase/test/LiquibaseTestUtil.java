package liquibase.test;

import java.io.StringWriter;
import java.sql.Connection;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

/**
 *
 */
public class LiquibaseTestUtil {


    public static String updateChangeLog(Connection connection, String changeLog) throws Exception{
        JdbcConnection jdbcConnection =  new JdbcConnection(connection);
        DatabaseFactory factory =  DatabaseFactory.getInstance();
        Database database = factory.findCorrectDatabaseImplementation(jdbcConnection);
        Liquibase liquibase = new Liquibase(
                changeLog,
                new ClassLoaderResourceAccessor(LiquibaseTestUtil.class.getClassLoader()),
                database
        );
        StringWriter sw = new StringWriter();
        liquibase.update(null, sw);
        return sw.toString();
    }
}
