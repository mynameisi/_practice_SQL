package helper.db.framework;

import helper.io.FileIO;

import java.io.File;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0 extends DB implements DBFrameWork {
	private ComboPooledDataSource connectionPool = null;
	private final String cleanUPSQL;

	//I may use logger later
	private static final Logger logger = LoggerFactory.getLogger(C3P0.class);

	public C3P0(String Driver, String URL, String user, String pass, String cleanUp) {
		this.cleanUPSQL = cleanUp;
		try {
			Class.forName(Driver);
			logger.debug("Driver loaded");
			connectionPool = new ComboPooledDataSource();
			
			connectionPool.setDriverClass(Driver); //loads the jdbc driver         
		} catch (Exception e) {
			e.printStackTrace();
		}

		connectionPool.setJdbcUrl(URL);
		connectionPool.setUser(user);
		connectionPool.setPassword(pass);

		// the settings below are optional -- c3p0 can work with defaults
		connectionPool.setMinPoolSize(5);
		connectionPool.setAcquireIncrement(5);
		connectionPool.setMaxPoolSize(20);

	}

	public void shutdown() {
		try {
			cleanUP(connectionPool.getConnection(), cleanUPSQL, FileIO.findRootResource(C3P0.class, "dropDB.sql"));
			connectionPool.close();
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
