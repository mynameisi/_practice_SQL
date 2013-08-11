package helper.db.framework;

import helper.io.Msg;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class BONECP implements DBFrameWork {
	private BoneCP connectionPool = null;

	public BONECP(String Driver, String URL, String user, String pass) {

		try {
			Class.forName(Driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		BoneCPConfig config = new BoneCPConfig();
		config.setJdbcUrl(URL);
		config.setUsername(user);
		config.setPassword(pass);
		config.setMinConnectionsPerPartition(5);
		config.setMaxConnectionsPerPartition(10);
		config.setPartitionCount(1);
		try {
			connectionPool = new BoneCP(config);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void shutdown() {
		Msg.debugMsg(DBFrameWork.class, "Database is shutting down");
		Statement st = null;
		Connection conn = null;
		try {
			conn = connectionPool.getConnection();
			st = conn.createStatement();
			st.execute("SHUTDOWN");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
				if (connectionPool != null) {
					connectionPool.shutdown();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void update(String expression) {
		Statement st = null;
		Connection conn = null;
		try {
			conn = connectionPool.getConnection();

			st = conn.createStatement(); // statements
			int i = st.executeUpdate(expression); // run the query
			if (i == -1) {
				System.out.println("db error : " + expression);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public void batchUpdate(File f) {
		Statement st = null;
		Connection conn = null;
		Scanner sc = null;
		try {
			conn = connectionPool.getConnection();
			st = conn.createStatement(); // statements
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
				st.executeUpdate(finalSQL);
				sql = new StringBuilder();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (sc != null) {
					sc.close();
				}
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean query(String sql, boolean showResult) {
		Statement st = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = connectionPool.getConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//后三句话能够很好的体现DBCP默认8个连接共同存在的实质
		//System.out.print("| ");
		//Thread.sleep(1000);
		//System.out.println("");
		boolean hasContent = false;
		try {
			st = conn.createStatement();
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
				if (st != null) {
					st.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return showResult;

	}

	private void showResultSetContent(ResultSet rs) throws SQLException {
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
