import static org.junit.Assert.assertTrue;
import helper.Context;
import helper.FileIO;
import helper.FileIO.SqlResults;
import helper.Msg;
import helper.db.SQL;
import helper.db.framework.DBFrameWork;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class _JunitTests {
	final static Logger logger = LoggerFactory.getLogger("_JunitTests.class");
	final static DBFrameWork myDB = Context.getMyDB();

	public boolean testSQL(int sqlFileNumber) {
		try {
			Thread.sleep(Context.INTERVAL_JUNIT);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//Msg.userMsgLn("***************SQL " + sqlFileNumber + "  测试***************");
		logger.info("***************SQL " + sqlFileNumber + "  测试***************");
		// get the file name of both user file and squeal practice number
		File userFile = FileIO.findSiblingResource(_JunitTests.class, sqlFileNumber + ".sql");
		SqlResults userSQL = FileIO.compactSQLFromFile(userFile);
		String compactSQL = userSQL.compat.toString();
		String fullSQL = userSQL.full.toString();
		if (compactSQL.trim().isEmpty()) {

			//logger.warn("你没有输入任何的SQL, 需要输入SQL才能看到结果");
			Msg.userMsgLn("你没有输入任何的SQL, 需要输入SQL才能看到结果");
			return false;
		}
		Msg.userMsgLn("你输入的SQL是:\n" + fullSQL);
		try {
			myDB.query(compactSQL, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String answer = SQL.findAnwer("sql" + sqlFileNumber);
		String newSQL = SQL.produceMius(compactSQL, answer);

		boolean result = false;
		try {
			result = myDB.query(newSQL, false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	@BeforeClass
	public static void initDB() {
		//CNST.initDB();
	}

	@AfterClass
	public static void shutDownDB() {
		try {
			myDB.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
