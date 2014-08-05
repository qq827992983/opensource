package y2javaee.xmal2.operation;

import java.sql.*;

import y2javaee.xmal2.common.DBConnection;
import y2javaee.xmal2.entity.Users;
/**
 * Users.java���ҵ�������
 *
 */
public class UserBo {
	
	private Connection conn; //���ڱ������ݿ����Ӷ���
	private PreparedStatement ps;//����ִ��SQL���
	private ResultSet rs; //���ڱ����ѯ�����
	/**
	 * ͨ���û����������жϸ��û��Ƿ����
	 * @param userName
	 * @param password
	 * @return ������ڣ����ظö��󣬷��򷵻�null
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
	 * ���ݴ��ݵĲ������������ݿ����Ӳ���
	 * @param user
	 * @return ������Ӱ������ݿ�����
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
