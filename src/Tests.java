import static org.junit.Assert.assertTrue;
import helper.CNST;
import helper.DB;
import helper.FileIO;
import helper.FileIO.SqlResults;
import helper.Msg;
import helper.SQL;

import java.io.File;

import org.junit.Test;

public class Tests {
	public boolean testSQL(int sqlFileNumber) {
		Msg.debugSep();
		Msg.userMsgLn("***************SQL " + sqlFileNumber + "  测试***************");
		// get the file name of both user file and sql practice number
		String userFileName = sqlFileNumber + ".sql";
		File userFile = FileIO.findSiblingResource(Tests.class, userFileName);
		String answerSQL = "sql" + sqlFileNumber;

		// show the content and result of user file
		// test the result
		SqlResults userSQL = FileIO.compactSQLFromFile(userFile);
		String compactSQL = userSQL.compat.toString();
		String fullSQL = userSQL.full.toString();
		Msg.userMsgLn("你输入的SQL是:\n" + fullSQL);
		DB.resultContent(compactSQL);

		String newSQL = SQL.produceMius(compactSQL, CNST.getSQL(answerSQL));
		Msg.debugMsg(Tests.class, "Your SQL is: \n" + userSQL);
		Msg.debugMsg(Tests.class, "The new SQL is: \n" + newSQL);

		return !DB.hasResult(newSQL);
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
