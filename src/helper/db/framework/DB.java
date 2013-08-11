package helper.db.framework;

import helper.io.Msg;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DB {
	protected DB() {}

	public static final Logger logger = LoggerFactory.getLogger(DB.class);

	public void cleanUP(Connection conn, String sql, File f) {
		logger.info("clean SQL: " + sql);
		logger.info("clean file:" + f);
		update(conn, sql, f);
	}

	public void update(Connection conn, String sql, File f) {
		Statement st = null;
		Scanner sc = null;
		try {
			conn.setAutoCommit(false);
			st = conn.createStatement(); // statements

			if (f != null && f.isFile()) {
				sc = new Scanner(f);
				StringBuilder sqlBuilder = new StringBuilder();
				while (sc.hasNextLine()) {
					String sqlLine = sc.nextLine().trim();
					if (sqlLine.startsWith("--")) {
						continue;
					}
					sqlBuilder.append(sqlLine + ' ');
					if (!sqlLine.contains(";")) {
						continue;
					}
					String finalSQL = sqlBuilder.toString();
					st.addBatch(finalSQL);
					sqlBuilder = new StringBuilder();
				}
			}
			if (sql != null && !sql.isEmpty()) {
				st.addBatch(sql);
			}
			logger.debug("START BATCH UPDATE");
			st.executeBatch();
			conn.commit();
			//set it back to true so that MySQL+BoneCP doesn't hang at "executeBatch()"
			//the 2nd time this method is called
			conn.setAutoCommit(true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {

				if (sc != null) {
					sc.close();
				}
				logger.debug("Closed Scanner");
				if (st != null) {
					st.close();
				}
				logger.debug("Closed Statement");
				if (conn != null) {
					conn.close();
				}
				conn = null;
				logger.debug("Closed Connection");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean query(Connection conn, String sql, boolean showResult) {

		Statement st = null;
		ResultSet rs = null;

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
		return hasContent;

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
	}

}
