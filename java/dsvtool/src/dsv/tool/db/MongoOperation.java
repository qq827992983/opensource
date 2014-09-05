package dsv.tool.db;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoOperation extends Connector{
	String url;
	final String SQL_SELECT_USERNAME = "select username from ofuser where student_number=\'%s\'";
	final String SQL_SELECT_ACCOUT = "select account from ofuser where uid=%s";
	final String SQL_SELECT_ORGNIZATION = "select id from orgnization where name=\'%s\'";
	final String SQL_SELECT_SUB_ORGNIZATION = "select name from orgnization where parend=%s";
	final String SQL_SELECT_ORGNIZATION_PARENT = "select parent from orgnization where name=%s";
	final String SQL_SELECT_USER_ORGNIZATION = "select orgnization_id from user_orgnization where uid=%s";
	final String SQL_SELECT_ORGNIZATION_NAME = "select name from orgnization where id=%s";
	final String SQL_SELECT_ORGNIZATION_ID = "select id from orgnization where name=%s";
	private static final Logger Log = LoggerFactory.getLogger(MongoOperation.class);
	
	public MongoOperation(String dbName, String host, String port,String userName, String password) {
		super(dbName, host, port, userName, password);
		url = "mongodb://"+host+"/"+ dbName;
	}

	@Override
	public JSONObject connect() {
		try{
			Class.forName("com.mongodb.jdbc.MongoDriver");
			con = DriverManager.getConnection(url, userName, password);
			if(con != null){
				return success;
			}
		}catch(Exception e){
			Log.error("DB connection:" + e);
		}
		return failed;
	}
	
	public String selectUsername(String studentNumber){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String key = null;
		String value = null;
		
		if(studentNumber == null || "".equals(studentNumber)){
			return null;
		}
		
		try{
			sql = String.format(SQL_SELECT_USERNAME, studentNumber);
			Log.debug("sql:" + sql);
			pstmt = con.prepareStatement(sql);
			while(rs.next()){
				ResultSetMetaData metaData = rs.getMetaData();
				for (int j = 1; j <= metaData.getColumnCount(); ++j) {
					key = metaData.getColumnName(j).toLowerCase();
					if(key.equals("username")){
						value = rs.getString(key);
						break;
					}
				}
			}
			pstmt.close();
			rs.close();
			pstmt = null;
			rs = null;
		}catch(Exception e){
			Log.error("Exception:" + e);
			rs = null;
		}
		return value;
	}
	
	public ResultSet selectAccount(String username){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		if(username == null || "".equals(username)){
			return null;
		}
		
		try{
			sql = String.format(SQL_SELECT_ACCOUT, username);
			Log.debug("sql:" + sql);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			pstmt.close();
		}catch(Exception e){
			Log.error("Exception:" + e);
			rs = null;
		}
		return rs;
	}
	
	public boolean checkOrgnizationIsExist(String orgnizationName) throws JSONException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String key = null;
		String value = null;

		if(orgnizationName == null || "".equals(orgnizationName)){
			return false;
		}
		
		try{
			sql = String.format(SQL_SELECT_ORGNIZATION, orgnizationName);
			Log.debug("sql:" + sql);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				ResultSetMetaData metaData = rs.getMetaData();
				for (int j = 1; j <= metaData.getColumnCount(); ++j) {
					key = metaData.getColumnName(j).toLowerCase();
					value = rs.getString(j);
					if(key.equals("id") && value != null){
						return true;
					}
				}
			}
			pstmt.close();
			rs.close();
			pstmt = null;
			rs = null;
		}catch(Exception e){
			Log.error("Exception:" + e);
			return false;
		}
		
		return false;
	}
	
	private long selectOrgnizationParent(String orgnizationName){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String key = null;
		long value = -1;

		if(orgnizationName == null || "".equals(orgnizationName)){
			return -1;
		}
		
		try{
			sql = String.format(SQL_SELECT_ORGNIZATION_PARENT, orgnizationName);
			Log.debug("sql:" + sql);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				ResultSetMetaData metaData = rs.getMetaData();
				for (int j = 1; j <= metaData.getColumnCount(); ++j) {
					key = metaData.getColumnName(j).toLowerCase();
					if(key.equals("parent")){
						value = rs.getLong(j);
					}
				}
			}
			pstmt.close();
			rs.close();
			pstmt = null;
			rs = null;
		}catch(Exception e){
			Log.error("Exception:" + e);
			value = -1;
		}
		
		return value;
	}
	
	public ResultSet selectSubOrgnization(String orgnizationName){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String key = null;
		String value = null;

		if(orgnizationName == null || "".equals(orgnizationName)){
			return null;
		}
		
		try{
			sql = String.format(SQL_SELECT_SUB_ORGNIZATION, orgnizationName);
			Log.debug("sql:" + sql);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			pstmt.close();
		}catch(Exception e){
			Log.error("Exception:" + e);
			return null;
		}
		
		return rs;
	}
	
	public long selectUserOrgnization(String uid){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String key = null;
		long value = -1;

		if(uid == null || "".equals(uid)){
			return -1;
		}
		
		try{
			sql = String.format(SQL_SELECT_USER_ORGNIZATION, uid);
			Log.debug("sql:" + sql);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				ResultSetMetaData metaData = rs.getMetaData();
				for (int j = 1; j <= metaData.getColumnCount(); ++j) {
					key = metaData.getColumnName(j).toLowerCase();
					if(key.equals("orgnization_id") && value > 0){
						value = rs.getLong(j);
					}
				}
			}
			pstmt.close();
			rs.close();
			pstmt = null;
			rs = null;
		}catch(Exception e){
			Log.error("Exception:" + e);
			return -1;
		}
		
		return value;
	}
	
	public String selectOrgnizationname(String orgId){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String key = null;
		String value = null;
		
		if(orgId == null || "".equals(orgId)){
			return null;
		}
		
		try{
			sql = String.format(SQL_SELECT_ORGNIZATION_NAME, orgId);
			Log.debug("sql:" + sql);
			pstmt = con.prepareStatement(sql);
			while(rs.next()){
				ResultSetMetaData metaData = rs.getMetaData();
				for (int j = 1; j <= metaData.getColumnCount(); ++j) {
					key = metaData.getColumnName(j).toLowerCase();
					if(key.equals("name")){
						value = rs.getString(key);
						break;
					}
				}
			}
			pstmt.close();
			rs.close();
			pstmt = null;
			rs = null;
		}catch(Exception e){
			Log.error("Exception:" + e);
			rs = null;
		}
		return value;
	}
	
	public long selectOrgnizationId(String orgNmae){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String key = null;
		long value = -1;
		
		if(orgNmae == null || "".equals(orgNmae)){
			return -1;
		}
		
		try{
			sql = String.format(SQL_SELECT_ORGNIZATION_ID, orgNmae);
			Log.debug("sql:" + sql);
			pstmt = con.prepareStatement(sql);
			while(rs.next()){
				ResultSetMetaData metaData = rs.getMetaData();
				for (int j = 1; j <= metaData.getColumnCount(); ++j) {
					key = metaData.getColumnName(j).toLowerCase();
					if(key.equals("name")){
						value = rs.getLong(key);
						break;
					}
				}
			}
			pstmt.close();
			rs.close();
			pstmt = null;
			rs = null;
		}catch(Exception e){
			Log.error("Exception:" + e);
			return -1;
		}
		return value;
	}
	
}
