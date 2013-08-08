package helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
	public static void resultContent(String SQL) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Class.forName(CNST.DRIVER);
			conn = DriverManager.getConnection(CNST.DB_URL, CNST.USER, CNST.PASS);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SQL);
			ResultSetMetaData rsMetaData = rs.getMetaData();
			int numberOfColumns = rsMetaData.getColumnCount();
			Msg.mmsg("你的SQL的结果是:");
			// 注意：i的起始值和以什么方式确定上限

			Msg.msep(numberOfColumns, '-');
			for (int i = 1; i <= numberOfColumns; i++) {
				String name = rsMetaData.getColumnName(i);
				System.out.printf("%-20s|", name);

			}
			Msg.mmsg("");
			Msg.msep(numberOfColumns, '-');
			while (rs.next()) {
				for (int i = 0; i < numberOfColumns; i++) {
					System.out.printf("%-20s|", rs.getObject(i + 1));
				}
				System.out.println();
			}
			Msg.msep(numberOfColumns, '-');
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					Msg.msg(DB.class, "ResultSet closed");
				}
				if (stmt != null) {
					stmt.close();

				}
				if (conn != null) {
					conn.close();
					Msg.msg(DB.class, "Connection closed");
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
			Class.forName(CNST.DRIVER);
			conn = DriverManager.getConnection(CNST.DB_URL, CNST.USER, CNST.PASS);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SQL);
			result = rs.isBeforeFirst();// 这句话SQL运行后数据库中有没有内容
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					Msg.msg(DB.class, "ResultSet closed");
				}
				if (stmt != null) {
					stmt.close();

				}
				if (conn != null) {
					conn.close();
					Msg.msg(DB.class, "Connection closed");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
