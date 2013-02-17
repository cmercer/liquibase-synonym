package liquibase.change.ext.synonym;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import liquibase.database.Database;
import liquibase.database.core.InformixDatabase;
import liquibase.database.core.MSSQLDatabase;
import liquibase.database.core.MaxDBDatabase;
import liquibase.database.core.MySQLDatabase;
import liquibase.database.core.OracleDatabase;
import liquibase.sql.Sql;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateOrReplaceSynonymTestCase {

    private CreateSynonymStatement replaceStatement;
    private CreateSynonymGenerator createSynonymGenerator;
    private CreateSynonymStatement nonReplaceStatement;
    private CreateSynonymStatement replaceAndPublicStatement;


    @Before
    public void before() {
        replaceStatement = new CreateSynonymStatement(
                "SRC_SERVER", "SRC_DB", "SRC_SCHEMA", "SRC_TABLE", "SCHEMA", "SYN_NAME", false, true);
        nonReplaceStatement = new CreateSynonymStatement(
                "NP_SERVER", "NP_DB", "NPSRC_SCHEMA", "NPSRC_TABLE", "NPSCHEMA", "NPSYN_NAME", false, false);
        replaceAndPublicStatement = new CreateSynonymStatement(
                "NP_SERVER", "NP_DB", "NPSRC_SCHEMA", "NPSRC_TABLE", "NPSCHEMA", "NPSYN_NAME", true, true);
        createSynonymGenerator = new CreateSynonymGenerator();
    }

    @After
    public void after() {
        replaceStatement = null;
        createSynonymGenerator = null;
        nonReplaceStatement = null;
    }

    @Test
    public void testOracle() {
        testSupportsReplace(new OracleDatabase());
        testSupportsPublicAndReplace(new OracleDatabase());
    }

    @Test
    public void testInformix() {
        testDoesNotSupportReplace(new InformixDatabase());
    }

    @Test
    public void testMaxDb() {
        testDoesNotSupportReplace(new MaxDBDatabase());
    }

    @Test
    public void testMySql() {
        testDoesNotSupportReplace(new MySQLDatabase());
    }

    @Test
    public void testMsSql() {
        testDoesNotSupportReplace(new MSSQLDatabase());
    }

    protected void testSupportsReplace(Database database) {
        String name = database.getDatabaseProductName();
        Sql[] replace = createSynonymGenerator.generateSql(replaceStatement, database, null);
        assertTrue(
                "create or replace synonym did not gen for " + name,
                replace[0].toSql().contains("CREATE OR REPLACE SYNONYM SCHEMA")
        );
        Sql[] withoutPublic = createSynonymGenerator.generateSql(nonReplaceStatement, database, null);
        assertTrue(
                "replace gen-d for " + name + " when replace was false",
                !withoutPublic[0].toSql().contains("CREATE OR REPLACE SYNONYM")
        );
    }

    protected void testDoesNotSupportReplace(Database database) {
        String name = database.getDatabaseProductName();
        Sql[] withoutPublic = createSynonymGenerator.generateSql(replaceStatement, database, null);
        String sql = withoutPublic[0].toSql();
        assertTrue("replace should not generate for " + name, sql.contains("CREATE SYNONYM SCHEMA"));
    }

    protected void testSupportsPublicAndReplace(Database database) {
        String name = database.getDatabaseProductName();
        Sql[] results = createSynonymGenerator.generateSql(replaceAndPublicStatement, database, null);
        String sql = results[0].toSql();
        assertNotNull("Should product Sql output", sql);
        String contain = "CREATE OR REPLACE PUBLIC SYNONYM";
        assertTrue("Should contain '" + contain + "' for " + name, sql.contains(contain));
    }

}
