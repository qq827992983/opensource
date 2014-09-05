package y2javaee.xmal2.common;

import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class DBConnection {
	private static final Logger Log = LoggerFactory.getLogger(DBConnection.class);
	private static Connection conn;

	/**
	 * 使用JNDI方式连接数据库，并返回连接
	 * 
	 * @return Connection
	 */
	public static synchronized Connection getConnectionForJndi()
			throws DBMysqlException {

		try {
			Context ic = new InitialContext();

			DataSource source = (DataSource) ic
					.lookup("java:comp/env/jdbc/ownHome");
			conn = source.getConnection();
		} catch (Exception e) {
			throw new DBMysqlException("不能取得数据库连接!");
		}
		return conn;
	}

	/**
	 * 使用配置文件方式连接数据库，并返回连接
	 * 
	 * @return Connection
	 */
	public static synchronized Connection getConnectionForProperty()
			throws DBMysqlException {

		// 读出配置信息
		String driverClassName = Env.getInstance().getProperty("driver");
		String url = Env.getInstance().getProperty("url");
		String password = Env.getInstance().getProperty("password");
		String user = Env.getInstance().getProperty("user");
		Connection con = null;
		try {
			// 加载数据库驱动程序
			Class.forName(driverClassName);
			con = DriverManager.getConnection(url, user, password);
			if(con != null)
				Log.debug("链接数据库成功！");
		} catch (Exception ex) {
			throw new DBMysqlException("不能取得数据库连接!");
		}
		return con;
	}

	/**
	 * 关闭数据库连接
	 * 
	 */
	public static void closeConnection() {
		try {
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭Statement对象
	 * 
	 * @param stm
	 */
	public static void closeStatement(Statement stm) {
		try {
			if (stm != null)
				stm.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭PreparedStatement对象
	 * 
	 * @param ps
	 */
	public static void closeStatement(PreparedStatement ps) {
		try {
			if (ps != null)
				ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭ResultSet结果集对象
	 * 
	 * @param rs
	 */
	public static void closeResultSet(ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
