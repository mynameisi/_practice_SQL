import static org.junit.Assert.assertTrue;
import helper.CNST;
import helper.FileIO;
import helper.FileIO.SqlResults;
import helper.Msg;
import helper.DB;
import helper.SQL;

import java.io.File;

import org.junit.AfterClass;
import org.junit.Test;

public class Tests {
	public boolean testSQL(int sqlFileNumber) {
		try {
			Thread.sleep(CNST.INST.INTERVAL_JUNIT);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Msg.userMsgLn("***************SQL " + sqlFileNumber + "  测试***************");
		// get the file name of both user file and sql practice number
		File userFile = FileIO.findSiblingResource(Tests.class, sqlFileNumber + ".sql");
		SqlResults userSQL = FileIO.compactSQLFromFile(userFile);
		String compactSQL = userSQL.compat.toString();
		String fullSQL = userSQL.full.toString();
		Msg.userMsgLn("你输入的SQL是:\n" + fullSQL);
		DB.INST.query(compactSQL, true);
		//DB.resultContent(compactSQL);

		String newSQL = SQL.produceMius(compactSQL, CNST.INST.getSQL("sql" + sqlFileNumber));

		//boolean result=!DB.hasResult(newSQL);
		boolean result = DB.INST.query(newSQL, false);
		//MyDB.INSTANCE.cleanAndShutDown();
		return result;
	}
	
	@AfterClass
	public static void shutDownDB(){
		DB.INST.shutdown();
	}

	@Test
	public void testSQL0() {
		assertTrue(testSQL(0));
	}

	@Test
	public void testSQL1() {
		assertTrue(testSQL(1));
	}

	@Test
	public void testSQL2() {
		assertTrue(testSQL(2));
	}

	@Test
	public void testSQL3() {
		assertTrue(testSQL(3));
	}

	@Test
	public void testSQL4() {
		assertTrue(testSQL(4));
	}

	@Test
	public void testSQL5() {
		assertTrue(testSQL(5));
	}

	@Test
	public void testSQL6() {
		assertTrue(testSQL(6));
	}

}
