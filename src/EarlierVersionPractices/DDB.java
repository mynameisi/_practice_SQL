package EarlierVersionPractices;

import helper.CNST;
import helper.Msg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class DDB {
	public static void resultContent(String SQL) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Class.forName(CNST.INST.DRIVER);
			conn = DriverManager.getConnection(CNST.INST.DB_URL, CNST.INST.USER, CNST.INST.PASS);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SQL);
			ResultSetMetaData rsMetaData = rs.getMetaData();
			int numberOfColumns = rsMetaData.getColumnCount();
			Msg.userMsgLn("你的SQL的结果是:");
			// 注意：i的起始值和以什么方式确定上限

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
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					Msg.debugMsg(DDB.class, "ResultSet closed");
				}
				if (stmt != null) {
					stmt.close();

				}
				if (conn != null) {
					conn.close();
					Msg.debugMsg(DDB.class, "Connection closed");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean hasResult(String SQL) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Boolean result = null;
		try {
			Class.forName(CNST.INST.DRIVER);
			conn = DriverManager.getConnection(CNST.INST.DB_URL, CNST.INST.USER, CNST.INST.PASS);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SQL);
			result = rs.isBeforeFirst();// 这句话SQL运行后数据库中有没有内容
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					Msg.debugMsg(DDB.class, "ResultSet closed");
				}
				if (stmt != null) {
					stmt.close();

				}
				if (conn != null) {
					conn.close();
					Msg.debugMsg(DDB.class, "Connection closed");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
