package liquibase.change.ext.synonym;

import static org.junit.Assert.*;
import liquibase.database.core.MySQLDatabase;
import liquibase.database.core.OracleDatabase;
import liquibase.sql.Sql;

import org.junit.Test;

public class CreatePublicSymTest {
	
	@Test
	public void testPublicSynonym(){
		CreateSynonymStatement pubStatement = new CreateSynonymStatement(
				"SRC_SERVER", "SRC_DB", "SRC_SCHEMA", "SRC_TABLE", "SCHEMA", "SYN_NAME", true);
        CreateSynonymGenerator createSynonymGenerator = new CreateSynonymGenerator();
        {//variable scope preventor
        Sql[] oraclesql = createSynonymGenerator.generateSql(pubStatement, new OracleDatabase(), null);
        assertTrue("public synonym did not gen for Oracle", oraclesql[0].toSql().contains("CREATE PUBLIC SYNONYM SCHEMA"));
        
        Sql[] mysqlsql = createSynonymGenerator.generateSql(pubStatement, new MySQLDatabase(), null);
        assertTrue("public should not generate for MySQL", mysqlsql[0].toSql().contains("CREATE SYNONYM SCHEMA"));
        }
        CreateSynonymStatement nonPubStatement = new CreateSynonymStatement(
				"NP_SERVER", "NP_DB", "NPSRC_SCHEMA", "NPSRC_TABLE", "NPSCHEMA", "NPSYN_NAME");
        {
        Sql[] nporaclesql = createSynonymGenerator.generateSql(nonPubStatement, new OracleDatabase(), null);
        assertTrue("public synonym gen-d for Oracle when public was false", !nporaclesql[0].toSql().contains("CREATE PUBLIC SYNONYM"));
        }
   	}
}
