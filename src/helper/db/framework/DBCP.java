package helper.db.framework;

import helper.io.FileIO;

import java.io.File;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

public class DBCP extends DB implements DBFrameWork {

	private DataSource connectionPool = null;
	private final String cleanUpSQL;

	public DBCP(String driver, String url, String user, String pass, String cleanUp) {

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Properties dbcpProperties = new Properties();
		dbcpProperties.put("url", url);
		dbcpProperties.put("username", user);
		dbcpProperties.put("password", pass);
		try {
			connectionPool = (BasicDataSource) BasicDataSourceFactory.createDataSource(dbcpProperties);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.cleanUpSQL = cleanUp;
	}
	
	public void shutdown() {
		try {
			cleanUP(connectionPool.getConnection(),cleanUpSQL, FileIO.findRootResource(BONECP.class, "dropDB.sql"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void update(String expression) {
		try {
			update(connectionPool.getConnection(), expression, null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void batchUpdate(File f) {
		try {
			update(connectionPool.getConnection(), null, f);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean query(String sql, boolean showResult) {
		boolean result = false;
		try {
			result = query(connectionPool.getConnection(), sql, showResult);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

}
