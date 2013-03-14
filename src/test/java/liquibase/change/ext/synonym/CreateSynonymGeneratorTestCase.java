package liquibase.change.ext.synonym;

import static liquibase.test.TestConstants.*;
import static org.apache.commons.lang.StringUtils.countMatches;
import static org.junit.Assert.*;

import liquibase.database.Database;
import liquibase.database.core.MSSQLDatabase;
import liquibase.database.core.MySQLDatabase;
import liquibase.database.core.OracleDatabase;
import liquibase.database.core.SQLiteDatabase;
import liquibase.exception.DatabaseException;
import liquibase.sql.Sql;
import liquibase.statement.SqlStatement;
import org.junit.Test;

public class CreateSynonymGeneratorTestCase {

    private static final String FULL_SOURCE_TABLE = SOURCE_SCHEMA + "." + SOURCE_DB_OBJECT;
    private static final String FULL_SYNONYM = SCHEMA + "." + SYNONYM;
    private static final String SQLSERVER_PARTIAL_SOURCE_TABLE = SOURCE_DB + "." + FULL_SOURCE_TABLE;
    private static final String SQLSERVER_FULL_SOURCE_TABLE = SOURCE_SERVER + "." + SQLSERVER_PARTIAL_SOURCE_TABLE;

    private static final String SQL_START = "CREATE SYNONYM ";
    private static final String SQL_MIDDLE = " FOR ";
    private static final String SQL_END = ";";

    private static final String SQL_ALL_COMPONENTS = SQL_START + FULL_SYNONYM + SQL_MIDDLE + FULL_SOURCE_TABLE + SQL_END;
    private static final String SQL_FULL_SYNONYM = SQL_START + FULL_SYNONYM + SQL_MIDDLE + SOURCE_DB_OBJECT + SQL_END;
    private static final String SQL_FULL_SOURCE_TABLE = SQL_START + SYNONYM + SQL_MIDDLE + FULL_SOURCE_TABLE + SQL_END;
    private static final String SQL_PARTIAL_COMPONENTS = SQL_START + SYNONYM + SQL_MIDDLE + SOURCE_DB_OBJECT + SQL_END;


    @Test
    public void testGenerateMSSQL() throws DatabaseException {
        MSSQLDatabase db = new MSSQLDatabase();
        Sql sql = produceSqlStatement(SOURCE_SERVER, SOURCE_DB, SOURCE_SCHEMA, SOURCE_DB_OBJECT, SCHEMA, SYNONYM, db);
        String result = sql.toSql();
        assertNotNull(result);
        assertEquals(6, countMatches(result, "["));
        assertEquals(6, countMatches(result, "]"));
        assertEquals(4, countMatches(result, "."));
        assertTrue(result.contains(SOURCE_SERVER));
        assertTrue(result.contains(SOURCE_DB));
        assertEquals(1, countMatches(result, SCHEMA));
        assertEquals(1, countMatches(result, SOURCE_SCHEMA));

        sql = produceSqlStatement(null, SOURCE_DB, SOURCE_SCHEMA, SOURCE_DB_OBJECT, SCHEMA, SYNONYM, db);
        result = sql.toSql();
        assertEquals(5, countMatches(result, "["));
        assertEquals(5, countMatches(result, "]"));
        assertEquals(3, countMatches(result, "."));
        assertFalse(result.contains(SOURCE_SERVER));
        assertTrue(result.contains(SOURCE_DB));
        assertEquals(1, countMatches(result, SCHEMA));
        assertEquals(1, countMatches(result, SOURCE_SCHEMA));

        sql = produceSqlStatement(null, null, SOURCE_SCHEMA, SOURCE_DB_OBJECT, SCHEMA, SYNONYM, db);
        result = sql.toSql();
        assertEquals(4, countMatches(result, "["));
        assertEquals(4, countMatches(result, "]"));
        assertEquals(2, countMatches(result, "."));
        assertFalse(result.contains(SOURCE_SERVER));
        assertFalse(result.contains(SOURCE_DB));
        assertTrue(result.contains(SOURCE_SCHEMA));
        assertTrue(result.contains(SYNONYM));
        assertEquals(1, countMatches(result, SCHEMA));
        assertEquals(1, countMatches(result, SOURCE_SCHEMA));

        db.setDefaultSchemaName("DEFAULT_SCHEMA_TEST");
        sql = produceSqlStatement(null, null, SOURCE_SCHEMA, SOURCE_DB_OBJECT, null, SYNONYM, db);
        result = sql.toSql();
        assertEquals(4, countMatches(result, "["));
        assertEquals(4, countMatches(result, "]"));
        assertEquals(2, countMatches(result, "."));
        assertFalse(result.contains(SOURCE_SERVER));
        assertFalse(result.contains(SOURCE_DB));
        assertTrue(result.contains(SOURCE_SCHEMA));
        assertTrue(result.contains(SYNONYM));
        assertFalse(result.contains(SCHEMA));
        assertEquals(1, countMatches(result, db.getDefaultSchemaName()));
        assertEquals(1, countMatches(result, SOURCE_SCHEMA));
    }

    @Test
    public void testGenerateSqlMySql() throws DatabaseException {
        MySQLDatabase db = new MySQLDatabase();
        Sql sql =
                produceSqlStatement(SOURCE_SERVER, SOURCE_DB, SOURCE_SCHEMA, SOURCE_DB_OBJECT, SCHEMA, SYNONYM, db);
        String result = sql.toSql();
        assertNotNull(result);
        assertTrue(result.startsWith("--"));
        assertEquals(4, countMatches(result, "`"));
        assertEquals(1, countMatches(result, "."));
        assertEquals(1, countMatches(result, SCHEMA));
        assertEquals(1, countMatches(result, SYNONYM));
    }

    @Test
    public void testGenerateSqlOracle() throws Exception {
        OracleDatabase oracleDatabase = new OracleDatabase();
        Sql sql = produceSqlStatement(
                SOURCE_SCHEMA, SOURCE_DB_OBJECT, SCHEMA, SYNONYM, oracleDatabase
        );
        assertEquals(
                "SQL statement should properly reflect fully-qualified arguments",
                SQL_ALL_COMPONENTS,
                sql.toSql()
        );

        sql = produceSqlStatement(null, SOURCE_DB_OBJECT, SCHEMA, SYNONYM, oracleDatabase);
        assertEquals("SQL statement should properly reflect fully-qualified synonym", SQL_FULL_SYNONYM, sql.toSql());

        sql = produceSqlStatement(SOURCE_SCHEMA, SOURCE_DB_OBJECT, null, SYNONYM, oracleDatabase);
        assertEquals(
                "SQL statement should properly reflect fully-qualified source table",
                SQL_FULL_SOURCE_TABLE,
                sql.toSql()
        );

        sql = produceSqlStatement(null, SOURCE_DB_OBJECT, null, SYNONYM, oracleDatabase);
        assertEquals(
                "SQL statement should properly reflect partially-qualified arguments",
                SQL_PARTIAL_COMPONENTS,
                sql.toSql()
        );
    }

    @Test()
    public void testGenerateSqlSqlLite() {
        SQLiteDatabase database = new SQLiteDatabase();
        Sql sql = produceSqlStatement(SOURCE_SCHEMA, SOURCE_DB_OBJECT, SCHEMA, SYNONYM, database);
        String result = sql.toSql();
        assertTrue(result.startsWith(database.getLineComment()));
        assertTrue(result.contains(SCHEMA));
        assertTrue(result.contains(SYNONYM));
        assertFalse(result.contains(SOURCE_SCHEMA));
        assertFalse(result.contains(SOURCE_DB_OBJECT));
    }

    @Test
    public void testDefaultSchemaSupport() throws DatabaseException {
        OracleDatabase oracleDatabase = new OracleDatabase();
        oracleDatabase.setDefaultSchemaName(SCHEMA);
        Sql sql = produceSqlStatement(SOURCE_SCHEMA, SOURCE_DB_OBJECT, null, SYNONYM, oracleDatabase);
        assertEquals("SQL statement should properly reflect fully-qualified arguments", SQL_ALL_COMPONENTS, sql.toSql());
        oracleDatabase.setDefaultSchemaName(null);
    }

    private Sql produceSqlStatement(
            String sourceSchema, String sourceTable, String schema, String synonym, Database database) {
        CreateSynonymChange createSynonymChange = new CreateSynonymChange(sourceSchema, sourceTable, schema, synonym);
        return produceSqlStatement(createSynonymChange, database);
    }

    private Sql produceSqlStatement(String sourceServer, String sourceDatabase, String sourceSchema, String sourceTable, String schema, String synonym, Database database) {
        CreateSynonymChange change;
        change = new CreateSynonymChange(sourceServer, sourceDatabase, sourceSchema, sourceTable, schema, synonym, false, false);
        return produceSqlStatement(change, database);
    }

    private Sql produceSqlStatement(CreateSynonymChange createSynonymChange, Database database) {
        CreateSynonymGenerator createSynonymGenerator = new CreateSynonymGenerator();
        SqlStatement[] statements = createSynonymChange.generateStatements(database);
        Sql[] sql = createSynonymGenerator.generateSql((CreateSynonymStatement) statements[0], database, null);
        assertEquals(1, sql.length);
        return sql[0];
    }

}
