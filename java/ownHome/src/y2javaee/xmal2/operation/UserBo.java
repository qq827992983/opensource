package y2javaee.xmal2.operation;

import java.sql.*;

import y2javaee.xmal2.common.DBConnection;
import y2javaee.xmal2.entity.Users;
/**
 * Users.java类的业务操作类
 *
 */
public class UserBo {
	
	private Connection conn; //用于保存数据库连接对象
	private PreparedStatement ps;//用于执行SQL语句
	private ResultSet rs; //用于保存查询结果集
	/**
	 * 通过用户名和密码判断该用户是否存在
	 * @param userName
	 * @param password
	 * @return 如果存在，返回该对象，否则返回null
	 */
	public Users validUser(String userName,String password){
		Users user=null;
		String sql="select * from users where userName=? and password=? ";
		try{
			conn=DBConnection.getConnectionForJndi();
			ps=conn.prepareStatement(sql);
			ps.setString(1, userName);
			ps.setString(2, password);
			
			rs=ps.executeQuery();
			if(rs.next()){
				user=new Users();
				user.setUserId(rs.getInt("userId"));
				user.setUserName(rs.getString("userName"));
				user.setRealName(rs.getString("realName"));
				user.setSex(rs.getString("sex"));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DBConnection.closeResultSet(rs);
			DBConnection.closeStatement(ps);
			DBConnection.closeConnection();
		}
		return user;
	}
	/**
	 * 根据传递的参数，进行数据库的添加操作
	 * @param user
	 * @return 操作所影响的数据库行数
	 */
	public int insertUser(Users user){
		int count=0;
		String sql="insert into users values(?,?,?,?) ";
		try{
			conn=DBConnection.getConnectionForJndi();
			ps=conn.prepareStatement(sql);
			ps.setString(1, user.getUserName());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getRealName());
			ps.setString(4, user.getSex());
			
			count=ps.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DBConnection.closeResultSet(rs);
			DBConnection.closeStatement(ps);
			DBConnection.closeConnection();
		}
		return count;
	}
	
}
