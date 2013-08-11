package helper.db.framework;

import helper.io.FileIO;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 这个Enum类对数据库进行先关的操作 之所以为enum而不是utility final class，是因为他需要初始化，共享资源
 * 
 * @author Administrator
 * 
 */
public class NOPOOL extends DB implements DBFrameWork {
	private final String URL;
	private final String user;
	private final String pass;
	private final String cleanUp;

	public NOPOOL(String driver, String URL, String user, String pass, String cleanUp) {
		this.URL = URL;
		this.user = user;
		this.pass = pass;
		this.cleanUp = cleanUp;
		try {
			Class.forName(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void shutdown() {
		try {
			cleanUP(DriverManager.getConnection(URL, user, pass), cleanUp, FileIO.findRootResource(BONECP.class, "dropDB.sql"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean query(String sql, boolean showResult) {
		boolean result = false;
		try {
			result = query(DriverManager.getConnection(URL, user, pass), sql, showResult);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public void update(String expression) {
		try {
			update(DriverManager.getConnection(URL, user, pass), expression, null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void batchUpdate(File f) {
		try {
			update(DriverManager.getConnection(URL, user, pass), null, f);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
