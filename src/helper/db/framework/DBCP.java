package helper.db.framework;

import helper.CNST;
import helper.Msg;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.KeyedObjectPoolFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPoolFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

public enum DBCP implements DB_Framwork {
	INST;

	private DataSource dataSource = null;
	GenericObjectPool<?> connectionPool = new GenericObjectPool<Object>();

	DBCP() {

		try {
			Class.forName(CNST.DRIVER);
			System.out.println("Driver Registered");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String URL = CNST.DB_URL + "user=sa";
		dataSource = setupDataSource(URL);
		System.out.println("Datasource Obtained");

	}

	public void shutdown() {
		Msg.debugMsg(DB_Framwork.class, "Database is shutting down");
		Statement st = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
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
					connectionPool.close();
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
			conn = dataSource.getConnection();

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
			conn = dataSource.getConnection();
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

	public DataSource setupDataSource(String connectURI) {
		//
		// First, we'll create a ConnectionFactory that the
		// pool will use to create Connections.
		// We'll use the DriverManagerConnectionFactory,
		// using the connect string passed in the command line
		// arguments.
		//
		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI, null);

		KeyedObjectPoolFactory<?, ?> statementPool = new GenericKeyedObjectPoolFactory<Object, Object>(null);

		//
		// Next we'll create the PoolableConnectionFactory, which wraps
		// the "real" Connections created by the ConnectionFactory with
		// the classes that implement the pooling functionality.
		//

		@SuppressWarnings("unused")
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, connectionPool, statementPool, connectURI, false, false);
		//
		// Now we'll need a ObjectPool that serves as the
		// actual pool of connections.
		//
		// We'll use a GenericObjectPool instance, although
		// any ObjectPool implementation will suffice.
		//
		//@SuppressWarnings("unchecked")
		//GenericObjectPool<?> cp = new GenericObjectPool<Object>(poolableConnectionFactory);

		//
		// Finally, we create the PoolingDriver itself,
		// passing in the object pool we created.
		//
		//cp.setMaxActive(100);
		PoolingDataSource dataSource = new PoolingDataSource(connectionPool);

		return dataSource;
	}
}
