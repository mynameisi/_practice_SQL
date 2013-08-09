package helper;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public enum MyDB {
	INSTANCE;
	MyDB() {

	}

	Connection conn = null; //our connnection to the db - presist for life of program

	Thread sdThread = null;

	public void start() {
		flag = true;
		if (conn == null) {
			sdThread = new Thread(new Runnable() {
				public void run() {
					shutdown();
				}
			});
			sdThread.start();
			try {
				Class.forName(CNST.DRIVER);
				conn = DriverManager.getConnection(CNST.DB_URL, CNST.USER, CNST.PASS);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	static boolean flag = true;

	static Object shutDownLock = new Object();

	public void shutdown() {
		synchronized (shutDownLock) {
			while (true) {
				Msg.debugMsg(MyDB.class, "flag is "+flag+" now");
				if (flag == true) {
					try {
						shutDownLock.wait();
					} catch (InterruptedException e) {
						Msg.debugMsg(MyDB.class, "restarted counting down");
						continue;
					}
				} else {
					try {
						Msg.debugMsg(MyDB.class, "waiting started");
						shutDownLock.wait(1000);
						Msg.debugMsg(MyDB.class, "waiting ended");
					} catch (InterruptedException e) {
						Msg.debugMsg(MyDB.class, "restarted counting down");
						continue;
					}
					if (flag == false) {
						Msg.debugMsg(MyDB.class, "The database now is shutting down");
						break;
					}
				}
			}

			Statement st;
			try {
				st = conn.createStatement();
				st.execute("SHUTDOWN");
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					} // if there are no other open connection
				}
			}

		}

	}

	public void cleanAndShutDown() {
		synchronized (shutDownLock) {
			flag = false;
			sdThread.interrupt();
		}
	}

	//use for SQL command SELECT
	public synchronized boolean query(String sql, boolean showResult) {
		Statement st = null;
		ResultSet rs = null;
		boolean hasContent = false;
		try {
			st = conn.createStatement();
			Msg.debugMsg(MyDB.class, "executing query: " + sql);
			rs = st.executeQuery(sql);
			hasContent = !rs.isBeforeFirst();
			if (showResult) {
				showResultSetContent(rs);
			}
			return hasContent;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return showResult;

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

	private static void showResultSetContent(ResultSet rs) throws SQLException {
		ResultSetMetaData rsMetaData = rs.getMetaData();
		int numberOfColumns = rsMetaData.getColumnCount();

		Msg.userMsgLn("你的SQL的结果是:");
		Msg.userSep(numberOfColumns, '-');
		for (int i = 1; i <= numberOfColumns; i++) {
			String name = rsMetaData.getColumnName(i);
			Msg.userMsgF("%-20s|", name);

		}
		Msg.userMsgLn("");
		Msg.userSep(numberOfColumns, '-');
		while (rs.next()) {
			for (int i = 0; i < numberOfColumns; i++) {
				Msg.userMsgF("%-20s|", rs.getObject(i + 1));
			}
			Msg.userMsgLn("");
		}
		Msg.userSep(numberOfColumns, '-');
	} //void dump( ResultSet rs )

}
