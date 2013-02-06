package liquibase.test;

import static liquibase.util.StringUtils.trimToNull;

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
        return updateChangeLog(connection, changeLog, null);
    }

    public static String updateChangeLog(Connection connection, String changeLog, String defaultSchemaName) throws Exception{
        JdbcConnection jdbcConnection =  new JdbcConnection(connection);
        DatabaseFactory factory =  DatabaseFactory.getInstance();
        Database database = factory.findCorrectDatabaseImplementation(jdbcConnection);
        if(trimToNull(defaultSchemaName) != null) {
            database.setDefaultSchemaName(defaultSchemaName);
        }
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
