package liquibase.change.ext.synonym;

import static org.junit.Assert.*;

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

public class CreatePublicSymTestCase {

    private CreateSynonymStatement pubStatement;
    private CreateSynonymGenerator createSynonymGenerator;
    private CreateSynonymStatement nonPubStatement;

    @Before
    public void before() {
        pubStatement = new CreateSynonymStatement(
                "SRC_SERVER", "SRC_DB", "SRC_SCHEMA", "SRC_TABLE", "SCHEMA", "SYN_NAME", true, false);
        nonPubStatement = new CreateSynonymStatement(
                "NP_SERVER", "NP_DB", "NPSRC_SCHEMA", "NPSRC_TABLE", "NPSCHEMA", "NPSYN_NAME", false, false);
        createSynonymGenerator = new CreateSynonymGenerator();
    }

    @After
    public void after() {
        pubStatement = null;
        createSynonymGenerator = null;
        nonPubStatement = null;
    }

    @Test
    public void testOracle() {
       testSupportsPublic(new OracleDatabase());
    }

    @Test
    public void testInformix() {
        testSupportsPublic(new InformixDatabase());
    }

    @Test
    public void testMaxDb() {
        testSupportsPublic(new MaxDBDatabase());
    }

    @Test
    public void testMySql() {
        testDoesNotSupportPublic(new MySQLDatabase());
    }

    @Test
    public void testMsSql() {
        testDoesNotSupportPublic(new MSSQLDatabase());
    }

    protected void testSupportsPublic(Database database) {
        String name = database.getDatabaseProductName();
        Sql[] withPublic = createSynonymGenerator.generateSql(pubStatement, database, null);
        assertTrue(
                "public synonym did not gen for " + name,
                withPublic[0].toSql().contains("CREATE PUBLIC SYNONYM SCHEMA")
        );
        Sql[] withoutPublic = createSynonymGenerator.generateSql(nonPubStatement, database, null);
        assertTrue(
                "public synonym gen-d for " + name + " when public was false",
                !withoutPublic[0].toSql().contains("CREATE PUBLIC SYNONYM")
        );
    }

    protected void testDoesNotSupportPublic(Database database) {
        Sql[] withoutPublic = createSynonymGenerator.generateSql(pubStatement, database, null);
        assertTrue(
                "public should not generate for " + database.getTypeName(),
                withoutPublic[0].toSql().contains("CREATE SYNONYM SCHEMA")
        );
    }
}
