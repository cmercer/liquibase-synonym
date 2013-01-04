package liquibase.sqlgenerator.ext;

import liquibase.change.ext.AddSynonymChange;
import liquibase.database.Database;
import liquibase.database.core.MSSQLDatabase;
import liquibase.database.core.OracleDatabase;
import liquibase.sql.Sql;
import liquibase.statement.SqlStatement;
import liquibase.statement.ext.AddSynonymStatement;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class AddSynonymGeneratorTest {

    private static final String SOURCE_SCHEMA = "SOURCE_SCHEMA";
    private static final String SOURCE_TABLE = "SOURCE_TABLE";
    private static final String FULL_SOURCE_TABLE = SOURCE_SCHEMA + "." + SOURCE_TABLE;
    private static final String SCHEMA = "SCHEMA";
    private static final String SYNONYM = "SYNONYM";
    private static final String FULL_SYNONYM = SCHEMA + "." + SYNONYM;
    private static final String SOURCE_SERVER = "SOURCE_SERVER";
    private static final String SOURCE_DATABASE = "SOURCE_DATABASE";
    private static final String SQLSERVER_PARTIAL_SOURCE_TABLE = SOURCE_DATABASE + "." + FULL_SOURCE_TABLE;
    private static final String SQLSERVER_FULL_SOURCE_TABLE = SOURCE_SERVER + "." + SQLSERVER_PARTIAL_SOURCE_TABLE;

    private static final String SQL_START = "CREATE SYNONYM ";
    private static final String SQL_MIDDLE = " FOR ";
    private static final String SQL_END = ";";

    private static final String SQL_ALL_COMPONENTS = SQL_START + FULL_SYNONYM + SQL_MIDDLE + FULL_SOURCE_TABLE + SQL_END;
    private static final String SQL_FULL_SYNONYM = SQL_START + FULL_SYNONYM + SQL_MIDDLE + SOURCE_TABLE + SQL_END;
    private static final String SQL_FULL_SOURCE_TABLE = SQL_START + SYNONYM + SQL_MIDDLE + FULL_SOURCE_TABLE + SQL_END;
    private static final String SQL_PARTIAL_COMPONENTS = SQL_START + SYNONYM + SQL_MIDDLE + SOURCE_TABLE + SQL_END;
    private static final String SQL_SQLSERVER_ALL_COMPONENTS = SQL_START + FULL_SYNONYM + SQL_MIDDLE + SQLSERVER_FULL_SOURCE_TABLE + SQL_END;
    private static final String SQL_SQLSERVER_PARTIAL_COMPONENTS = SQL_START + FULL_SYNONYM + SQL_MIDDLE + SQLSERVER_PARTIAL_SOURCE_TABLE + SQL_END;

    @Test
    public void testValidate() {
    }

    @Test
    public void testGenerateSql() {
        OracleDatabase oracleDatabase = new OracleDatabase();
        Sql sql = produceSqlStatement(SOURCE_SCHEMA, SOURCE_TABLE, SCHEMA, SYNONYM, oracleDatabase);
        assertEquals("SQL statement should properly reflect fully-qualified arguments", SQL_ALL_COMPONENTS, sql.toSql());

        sql = produceSqlStatement(null, SOURCE_TABLE, SCHEMA, SYNONYM, oracleDatabase);
        assertEquals("SQL statement should properly reflect fully-qualified synonym", SQL_FULL_SYNONYM, sql.toSql());

        sql = produceSqlStatement(SOURCE_SCHEMA, SOURCE_TABLE, null, SYNONYM, oracleDatabase);
        assertEquals("SQL statement should properly reflect fully-qualified source table", SQL_FULL_SOURCE_TABLE, sql.toSql());

        sql = produceSqlStatement(null, SOURCE_TABLE, null, SYNONYM, oracleDatabase);
        assertEquals("SQL statement should properly reflect partially-qualified arguments", SQL_PARTIAL_COMPONENTS, sql.toSql());

        MSSQLDatabase mssqlDatabase = new MSSQLDatabase();
        sql = produceSqlStatement(SOURCE_SERVER, SOURCE_DATABASE, SOURCE_SCHEMA, SOURCE_TABLE, SCHEMA, SYNONYM, mssqlDatabase);
        assertEquals("SQL statement should properly reflect fully-qualified SQL Server source", SQL_SQLSERVER_ALL_COMPONENTS, sql.toSql());

        sql = produceSqlStatement(null, SOURCE_DATABASE, SOURCE_SCHEMA, SOURCE_TABLE, SCHEMA, SYNONYM, mssqlDatabase);
        assertEquals("SQL statement should properly reflect partially-qualified SQL Server source", SQL_SQLSERVER_PARTIAL_COMPONENTS, sql.toSql());
    }

    private Sql produceSqlStatement(String sourceSchema, String sourceTable, String schema, String synonym, Database database) {
        AddSynonymChange addSynonymChange = new AddSynonymChange(sourceSchema, sourceTable, schema, synonym);
        return produceSqlStatement(addSynonymChange, database);
    }

    private Sql produceSqlStatement(String sourceServer, String sourceDatabase, String sourceSchema, String sourceTable, String schema, String synonym, Database database) {
        AddSynonymChange addSynonymChange = new AddSynonymChange(sourceServer, sourceDatabase, sourceSchema, sourceTable, schema, synonym);
        return produceSqlStatement(addSynonymChange, database);
    }

    private Sql produceSqlStatement(AddSynonymChange addSynonymChange, Database database) {
        AddSynonymGenerator addSynonymGenerator = new AddSynonymGenerator();
        SqlStatement[] statements = addSynonymChange.generateStatements(database);
        Sql[] sql = addSynonymGenerator.generateSql((AddSynonymStatement) statements[0], database, null);
        return sql[0];
    }

}
