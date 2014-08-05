package y2javaee.xmal2.common;

import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBConnection {

	private static Connection conn;

	/**
	 * ʹ��JNDI��ʽ�������ݿ⣬����������
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
			throw new DBMysqlException("����ȡ�����ݿ�����!");
		}
		return conn;
	}

	/**
	 * ʹ�������ļ���ʽ�������ݿ⣬����������
	 * 
	 * @return Connection
	 */
	public static synchronized Connection getConnectionForProperty()
			throws DBMysqlException {

		// ����������Ϣ
		String driverClassName = Env.getInstance().getProperty("driver");
		String url = Env.getInstance().getProperty("url");
		String password = Env.getInstance().getProperty("password");
		String user = Env.getInstance().getProperty("user");
		Connection con = null;
		System.out.println("driverClassName:"+driverClassName+",url:"+url+",user:"+user+",password:"+password);
		try {
			// �������ݿ���������
			Class.forName(driverClassName);
			con = DriverManager.getConnection(url, user, password);
			if(con != null)
				System.out.println("�������ݿ�ɹ���");
		} catch (Exception ex) {
			throw new DBMysqlException("����ȡ�����ݿ�����!");
		}
		return con;
	}

	/**
	 * �ر����ݿ�����
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
	 * �ر�Statement����
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
	 * �ر�PreparedStatement����
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
	 * �ر�ResultSet���������
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
