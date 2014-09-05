package dsv.tool.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Connector {
	String dbName;
	String host;
	String prot;
	String userName;
	String password;
	Connection con;
	PreparedStatement pstmtSelectData = null;
	ResultSet rsSelectData = null;

	final String SQL_SELECT_COUNT = "select count(*) from %s";
	final String SQL_SELECT_ALL = "select * from %s";
	final String SQL_SELECT_STUDENT_NUMBER = "select student_number from %s limit %s,%s";
	final String SQL_SELECT_DATA = "select * from %s limit %s,%s";
	final String SQL_SELECT_BY_STUDENT_NUMBER = "select * from %s where student_number=\'%s\'";
	final String SQL_SELECT_IS_EXIST = "select count(*) from %s where student_number=\'%s\'";
	
	Logger Log = LoggerFactory.getLogger(Connector.class);
	static JSONObject success;
	static JSONObject failed;
	
	static{
		try{
			success = new JSONObject("{\"ret\":0,\"errcode\":0,\"msg\":\"操作成功\"}");
			failed =  new JSONObject("{\"ret\":-1,\"errcode\":-1,\"msg\":\"操作失败\"}");
		}catch(Exception e){
			
		}
	}
	
	public Connector(String dbName, String host, String port, String userName, String password){
		this.dbName = dbName;
		this.host = host;
		this.prot = port;
		this.userName = userName;
		this.password = password;
	}
	
	public abstract JSONObject connect();
	
	public void close(){
		try{
			con.close();
		}catch(Exception e){
			Log.error("Exception:" + e);
		}
	}
	
	public JSONObject resultToJson(ResultSet rs){
		JSONObject result = null;
		String key = null;
		String value = null;
		
		if(rs == null){
			return failed;
		}
		
		try{
			result = new JSONObject();
			ResultSetMetaData metaData = rs.getMetaData();

			for (int j = 1; j <= metaData.getColumnCount(); ++j) {
				key = metaData.getColumnName(j).toLowerCase();
				if(key.equals("_id")){
					continue;
				}
				value = rs.getString(key);
				result.put(key, value);
			}
		}catch(Exception e){
			Log.error("Exception:" + e);
			return failed;
		}
		return result;
	}
	
	public JSONObject compareResultSet(JSONObject src, JSONObject dest) throws JSONException{
		JSONObject result = null;
		String key = null;
		String value = null;
		boolean flag = true;
		
		if(src == null || dest == null){
			return failed;
		}
		
		try{
			result = new JSONObject();
			JSONArray array = src.names();
			if(array.length() < 1){
				return failed;
			}
			for(int i=0; i<array.length(); i++){
				key = array.getString(i);
				value = src.getString(key);
				if(key.equals("card_type") || key.equals("name") || key.equals("sex") || key.equals("status")
						|| key.equals("card_number")|| key.equals("cellphone")|| key.equals("identity"))
				{
					if(!value.equals(dest.getString(key))){
						flag = false;
						break;
					}
				}
			}
			
			if(flag){
				array = dest.names();
				if(array.length() < 1){
					return failed;
				}
				for(int i=0; i<array.length(); i++){
					key = array.getString(i);
					value = src.getString(key);
					if(key.equals("card_type") || key.equals("name") || key.equals("sex") || key.equals("status")
							|| key.equals("card_number")|| key.equals("cellphone")|| key.equals("identity"))
					{
						if(!value.equals(src.getString(key))){
							flag = false;
							break;
						}
					}
				}
			}
		}catch(Exception e){
			Log.error("Exception:" + e);
			return failed;
		}
		
		if(flag){
			return success;
		}else{
			result.put("self", src);
			result.put("their", dest);
			return result;
		}
	}
	
	public JSONObject compareResultSet(ResultSet src, ResultSet dest) throws JSONException{
		JSONObject result = null;
		String key = null;
		String value = null;
		boolean flag = true;
		
		if(src == null || dest == null){
			return failed;
		}
		
		try{	
			result = new JSONObject();
			ResultSetMetaData metaData = src.getMetaData();

			for (int j = 1; j <= metaData.getColumnCount(); ++j) {
				if(key.equals("_id")){
					continue;
				}
				key = metaData.getColumnName(j).toLowerCase();
				value = src.getString(j);
				if(key.equals("card_type") || key.equals("name") || key.equals("sex") || key.equals("status")
						|| key.equals("card_number")|| key.equals("cellphone")|| key.equals("identity"))
				{
					if(!value.equals(dest.getString(key))){
						flag = false;
						break;
					}
				}
			}
			
			if(flag){
				metaData = dest.getMetaData();
				for (int j = 1; j <= metaData.getColumnCount(); ++j) {
					if(key.equals("_id")){
						continue;
					}
					key = metaData.getColumnName(j).toLowerCase();
					value = src.getString(j);
					if(key.equals("card_type") || key.equals("name") || key.equals("sex") || key.equals("status")
							|| key.equals("card_number")|| key.equals("cellphone")|| key.equals("identity")){
						if(!value.equals(src.getString(key))){
							flag = false;
							break;
						}	
					}					
				}
			}
			src.close();
			src = null;
			dest.close();
			dest = null;
		}catch(Exception e){
			Log.error("Exception:" + e);
			return failed;
		}
		if(flag){
			return success;
		}else{
			result.put("ret", 1);
			result.put("self", resultToJson(src));
			result.put("their", resultToJson(dest));
			return result;
		}
	}
	
	public long selectCount(String table){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long count = 0;
		
		if(table == null || "".equals(table)){
			return -1;
		}
		
		try{
			String sql = String.format(SQL_SELECT_COUNT, table);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				count = rs.getLong(1); 
			}
			rs.close();
			rs = null;
			pstmt.close();
			pstmt = null;
		}catch(Exception e){
			Log.error("Exception:" + e);
			count = -1;
		}
		return count;
	}
	
	public JSONObject selectAllData(String table) throws JSONException{
		String sql = null;
		PreparedStatement pstmtAllData = null;
		ResultSet rsAllData = null;
		String key = null;
		String value = null;
		JSONObject result = null;
		JSONArray array = null;
		
		if(table == null || "".equals(table)){
			return null;
		}
		
		try{
			array = new JSONArray();
			sql = String.format(SQL_SELECT_ALL, table);
			Log.debug("sql:" + sql);
			pstmtAllData = con.prepareStatement(sql);
			rsAllData = pstmtAllData.executeQuery();
			while(rsAllData.next()){
				ResultSetMetaData metaData = rsAllData.getMetaData();
				result = new JSONObject();
				for (int j = 1; j <= metaData.getColumnCount(); ++j) {
					key= metaData.getColumnName(j).toLowerCase();
					if(key.equals("_id")){
						continue;
					}
					value = rsAllData.getString(key);
					result.put(key, value);
				}
				array.put(result);
			}
			pstmtAllData.close();
			rsAllData.close();
			pstmtAllData = null;
			rsAllData.close();	
		}catch(Exception e){
			Log.error("Exception:" + e);
			result = null;
		}
		result = new JSONObject();
		result.put("result", "ok");
		result.put("data", array);
		return result;
	}
	
	public ResultSet selectData(String table, int pos, int len) throws SQLException{
		String sql = null;
		
		if(table == null || "".equals(table)){
			return null;
		}
		
		if(pos < 0 || len < 0){
			return null;
		}
		
		try{
			sql = String.format(SQL_SELECT_DATA, table, pos, len);
			Log.debug("sql:" + sql);
			pstmtSelectData = con.prepareStatement(sql);
			rsSelectData = pstmtSelectData.executeQuery();
		}catch(Exception e){
			Log.error("Exception:" + e);
			return null;
		}
		return rsSelectData;
	}
	
	public void SelectDataClose(){
		try{
			pstmtSelectData.close();
			rsSelectData.close();
			pstmtSelectData = null;
			rsSelectData.close();	
		}catch(Exception e){
			Log.error("Exception:" + e);
		}
	}
	
	public List<String> selectStudentNumber(String table, int pos, int len){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		List<String> list = null;
		
		if(table == null || "".equals(table)){
			return null;
		}
		
		if(pos < 0 || len < 0){
			return null;
		}
		
		try{
			list = new ArrayList<String>();
			sql = String.format(SQL_SELECT_STUDENT_NUMBER, table, pos, len);
			Log.debug("sql:" + sql);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				ResultSetMetaData metaData = rs.getMetaData();

				for (int j = 1; j <= metaData.getColumnCount(); ++j) {
					String name = metaData.getColumnName(j).toLowerCase();
					String value = "";
					if (name.equals("student_number")) {
						value = rs.getString(j);
						list.add(value);
					}
				}
			}
			pstmt.close();
			rs.close();
		}catch(Exception e){
			Log.error("Exception:" + e);
			rs = null;
		}
		return list;
	}
	
	public JSONObject selectDataByStudentNumber(String table, String studentNumber){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		JSONObject ret = null;
		
		if(table == null || "".equals(table)){
			return null;
		}
		
		if(studentNumber == null || "".equals(studentNumber)){
			return null;
		}
		
		try{
			ret = new JSONObject();
			sql = String.format(SQL_SELECT_BY_STUDENT_NUMBER, table, studentNumber);
			Log.error("sql:" + sql);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				ResultSetMetaData metaData = rs.getMetaData();

				for (int j = 1; j <= metaData.getColumnCount(); ++j) {
					String name = metaData.getColumnName(j).toLowerCase();
					String value = "";
					if (name.equals("_id")) {
						continue;
					}
					
					ret.put(name, value);
				}
			}
			pstmt.close();
			rs.close();
		}catch(Exception e){
			Log.error("Exception:" + e);
			ret = null;
		}
		
		return ret;
	}
	
	public boolean checkUserIsExist(String table, String studentNumber){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		long count = -1;
		
		if(table == null || "".equals(table)){
			return false;
		}
		
		if(studentNumber == null || "".equals(studentNumber)){
			return false;
		}
		
		try{
			sql = String.format(SQL_SELECT_IS_EXIST, table, studentNumber);
			Log.debug("sql:" + sql);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				count = rs.getLong(1);
			}
			pstmt.close();
		}catch(Exception e){
			Log.error("Exception:" + e);
			return false;
		}
		if(count > 0){
			return true;
		}else{
			return false;
		}
		
	}
	
	public String getDbName() {
		return dbName;
	}

	protected void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getHost() {
		return host;
	}

	protected void setHost(String host) {
		this.host = host;
	}

	public String getProt() {
		return prot;
	}

	protected void setProt(String prot) {
		this.prot = prot;
	}

	public String getUserName() {
		return userName;
	}

	protected void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	protected void setPassword(String password) {
		this.password = password;
	}
}
