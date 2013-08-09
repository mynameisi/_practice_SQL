package helper;

import java.io.File;

public class Test {
	

	public static void main(String[] args) throws Exception {
		HSQLDB testdb = new HSQLDB("./opt/db/testdb");
		//Testdb testdb=new Testdb("./opt/db/testdb","localhost",1234);
		File sqlFile = new File("C:/Documents and Settings/Administrator/git/_practice_SQL/src/createDB.sql");
		testdb.batchUpdate(sqlFile);
		String query = "select * from customer";
		testdb.query(query);
		testdb.shutdown();
	}

}
