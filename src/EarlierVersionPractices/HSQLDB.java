package EarlierVersionPractices;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.hsqldb.server.Server;

public class HSQLDB {

	Server server = null;
	Connection conn = null; //our connnection to the db - presist for life of program

	// we dont want this garbage collected until we are done
	public HSQLDB(String db_file_name_prefix) throws Exception { // note more general exception

		// Load the HSQL Database Engine JDBC driver
		// hsqldb.jar should be in the class path or made part of the current jar
		Class.forName("org.hsqldb.jdbcDriver");

		// connect to the database.   This will load the db files and start the
		// database if it is not alread running.
		// db_file_name_prefix is used to open or create files that hold the state
		// of the db.
		// It can contain directory names relative to the
		// current working directory
		conn = DriverManager.getConnection("jdbc:hsqldb:" + db_file_name_prefix + ";shutdown=true", // filenames
				"sa", // username
				""); // password
	}

	public HSQLDB(String db_file_name_prefix, String serverAddress, int portNumber) throws Exception {
		server = new Server();
		server.setAddress("localhost");
		server.setDatabaseName(0, "testdb");
		server.setDatabasePath(0, db_file_name_prefix);
		server.setPort(1234);
		server.setTrace(true);
		server.setLogWriter(new PrintWriter(System.out));
		server.start();
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace(System.out);
		}
		try {
			String connectionURL = "jdbc:hsqldb:hsql://" + serverAddress + ":" + portNumber + "/testdb";
			System.out.println(connectionURL);
			conn = DriverManager.getConnection(connectionURL, "SA", "");
		} catch (SQLException e) {
			e.printStackTrace(System.out);
		}
	}

	public void shutdown() throws SQLException {

		Statement st = conn.createStatement();

		// db writes out to files and performs clean shuts down
		// otherwise there will be an unclean shutdown
		// when program ends
		st.execute("SHUTDOWN");
		if (conn != null) {
			conn.close(); // if there are no other open connection
		}
		if (server != null) {
			server.shutdown();
		}
	}

	//use for SQL command SELECT
	public synchronized void query(String expression) throws SQLException {

		Statement st = null;
		ResultSet rs = null;

		st = conn.createStatement(); // statement objects can be reused with

		// repeated calls to execute but we
		// choose to make a new one each time
		rs = st.executeQuery(expression); // run the query

		// do something with the result set.
		dump(rs);
		st.close(); // NOTE!! if you close a statement the associated ResultSet is

		// closed too
		// so you should copy the contents to some other object.
		// the result set is invalidated also  if you recycle an Statement
		// and try to execute some other query before the result set has been
		// completely examined.
	}

	//use for SQL commands CREATE, DROP, INSERT and UPDATE
	public synchronized void update(String expression) throws SQLException {
		Statement st = null;
		st = conn.createStatement(); // statements
		int i = st.executeUpdate(expression); // run the query
		if (i == -1) {
			System.out.println("db error : " + expression);
		}
		st.close();
	} // void update()

	public synchronized void batchUpdate(File f) throws Exception {
		Statement st = null;
		st = conn.createStatement(); // statements
		Scanner sc = null;
		sc = new Scanner(f);
		StringBuilder sql = new StringBuilder();
		while (sc.hasNextLine()) {
			String sqlLine = sc.nextLine().trim();
			if (sqlLine.startsWith("--")) {
				continue;
			}
			sql.append(sqlLine + ' ');
			if (!sqlLine.contains(";")) {
				continue;
			}
			String finalSQL = sql.toString();
			System.out.println(finalSQL);
			st.executeUpdate(finalSQL);
			sql = new StringBuilder();
		}
		System.out.println("creation done");
		st.clearBatch();
		sc.close();
	}

	public static void dump(ResultSet rs) throws SQLException {

		// the order of the rows in a cursor
		// are implementation dependent unless you use the SQL ORDER statement
		ResultSetMetaData meta = rs.getMetaData();
		int colmax = meta.getColumnCount();
		int i;
		Object o = null;

		// the result set is a cursor into the data.  You can only
		// point to one row at a time
		// assume we are pointing to BEFORE the first row
		// rs.next() points to next row and returns true
		// or false if there is no next row, which breaks the loop
		for (; rs.next();) {
			for (i = 0; i < colmax; ++i) {
				o = rs.getObject(i + 1); // Is SQL the first column is indexed

				// with 1 not 0
				System.out.print(o.toString() + " ");
			}

			System.out.println(" ");
		}
	} //void dump( ResultSet rs )

	public static void main(String[] args) {

		HSQLDB db = null;

		try {
			db = new HSQLDB("db_file");
		} catch (Exception ex1) {
			ex1.printStackTrace(); // could not start db

			return; // bye bye
		}

		try {

			//make an empty table
			//
			// by declaring the id column IDENTITY, the db will automatically
			// generate unique values for new rows- useful for row keys
			db.update("CREATE TABLE sample_table ( id INTEGER IDENTITY, str_col VARCHAR(256), num_col INTEGER)");
		} catch (SQLException ex2) {

			//ignore
			//ex2.printStackTrace();  // second time we run program
			//  should throw execption since table
			// already there
			//
			// this will have no effect on the db
		}

		try {

			// add some rows - will create duplicates if run more then once
			// the id column is automatically generated
			db.update("INSERT INTO sample_table(str_col,num_col) VALUES('Ford', 100)");
			db.update("INSERT INTO sample_table(str_col,num_col) VALUES('Toyota', 200)");
			db.update("INSERT INTO sample_table(str_col,num_col) VALUES('Honda', 300)");
			db.update("INSERT INTO sample_table(str_col,num_col) VALUES('GM', 400)");

			// do a query
			db.query("SELECT * FROM sample_table WHERE num_col < 250");

			// at end of program
			db.shutdown();
		} catch (SQLException ex3) {
			ex3.printStackTrace();
		}
	} // main()

} // class Testdb