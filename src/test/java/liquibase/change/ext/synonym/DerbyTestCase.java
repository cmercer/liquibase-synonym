package liquibase.change.ext.synonym;

import liquibase.test.DbConnectionRule;
import org.junit.Rule;


/**
 *
 */
public class DerbyTestCase extends AbstractTestCase{

    @Rule
    public DbConnectionRule dbConnectionRule = new DbConnectionRule(
            "jdbc:derby:memory:testDB;create=true", "", ""
    );

    @Override
    public DbConnectionRule getDbConnectionRule() {
        return dbConnectionRule;
    }
}
