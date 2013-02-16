package liquibase.change.ext.synonym;

import java.io.IOException;

import liquibase.test.DbConnectionRule;
import org.junit.Rule;


/**
 *
 */
public class DerbyXmlTestCase extends AbstractXmlTestCase {

    //used to make sure we don't create a derby.log file that we have to clean up later.
    static {
        System.setProperty("derby.stream.error.method","DerbyTestCase.disableDerbyLogFile");
    }

    @Rule
    public DbConnectionRule dbConnectionRule = new DbConnectionRule(
            "jdbc:derby:memory:testDB;create=true;", "", ""
    );

    @Override
    public DbConnectionRule getDbConnectionRule() {
        return dbConnectionRule;
    }

    public static java.io.OutputStream disableDerbyLogFile(){
        return new java.io.OutputStream() {
            public void write(int b) throws IOException {
                //do nothing
            }
        };
    }

}
