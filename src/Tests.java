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
		// get the file name of both user file and sql practice number
		String userFileName = sqlFileNumber + ".sql";
		String userFilePath = Tests.class.getResource(userFileName).getFile();
		String answerSQL = "sql" + sqlFileNumber;
		// get the corresponding file
		File userFile = new File(userFilePath);
		// show the content and result of user file
		// test the result
		SqlResults userSQL = FileIO.compactSQLFromFile(userFile);
		String compactSQL = userSQL.compat.toString();
		String fullSQL = userSQL.full.toString();
		Msg.mmsg("你输入的SQL是:\n" + fullSQL);
		DB.resultContent(compactSQL);
		
		String newSQL = SQL.produceMius(compactSQL, CNST.getSQL(answerSQL));
		Msg.msg(Tests.class, "Your SQL is: \n" + userSQL);
		Msg.msg(Tests.class, "The new SQL is: \n" + newSQL);

		return !DB.hasResult(newSQL);
	}

	@Test
	public void testSQL1() {
		Msg.sep();
		assertTrue(testSQL(1));
	}

	@Test
	public void testSQL2() {
		Msg.sep();
		assertTrue(testSQL(2));
	}

}
