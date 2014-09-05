package y2javaee.xmal2.operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import y2javaee.xmal2.common.DBConnection;
import y2javaee.xmal2.entity.Book;

public class BookBo {
	private Connection conn; //用于保存数据库连接对象
	private PreparedStatement ps;//用于执行SQL语句
	private ResultSet rs; //用于保存查询结果集

	public int insertBook(Book book) {
		int count=0;
		String sql="insert into book(name,sex,phone,address,mobilePhone,company,comPhone,comAddress,relation,userId) values(?,?,?,?,?,?,?,?,?,?)";
		try{
			conn=DBConnection.getConnectionForProperty();
			ps=conn.prepareStatement(sql);
			ps.setString(1, book.getName());
			ps.setString(2, book.getSex());
			ps.setString(3, book.getPhone());
			ps.setString(4, book.getAddress());
			ps.setString(5, book.getMobilePhone());
			ps.setString(6, book.getCompany());
			ps.setString(7, book.getComPhone());
			ps.setString(8, book.getComAddress());
			ps.setString(9, book.getRelation());
			ps.setInt(10, 1);
			
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
