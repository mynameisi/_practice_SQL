package helper.db.framework;

import helper.io.FileIO;

import java.io.File;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class BONECP extends DB implements DBFrameWork {
	private BoneCP connectionPool = null;
	private final String cleanUPSQL;

	//I may use logger later
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(BONECP.class);

	public BONECP(String Driver, String URL, String user, String pass, String cleanUp) {
		this.cleanUPSQL = cleanUp;
		try {
			Class.forName(Driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		BoneCPConfig config = new BoneCPConfig();
		config.setJdbcUrl(URL);
		config.setUsername(user);
		config.setPassword(pass);
		config.setCloseConnectionWatch(true);
		//config.setMinConnectionsPerPartition(5);
		//config.setMaxConnectionsPerPartition(10);
		config.setPartitionCount(1);
		try {
			connectionPool = new BoneCP(config);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void shutdown() {
		try {
			cleanUP(connectionPool.getConnection(), cleanUPSQL, FileIO.findRootResource(BONECP.class, "dropDB.sql"));
			connectionPool.shutdown();
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
