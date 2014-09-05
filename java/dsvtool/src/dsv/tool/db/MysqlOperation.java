package dsv.tool.db;

import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MysqlOperation extends Connector{
	String url;
	final String SQL_SELECT_VALIDATION = "select student_number from validation where status=%s";
	final String SQL_SELECT_USER_IS_EXIST = "select count(*) from validation where student_number=%s";
	final String SQL_INSERT_VALIDATION = "insert into validation(student_number,status) values(\'%s\',%s)";
	final String SQL_CREATE_VALIDATION = "create table validation(student_number varchar(20) primary key, status int)";
	final String SQL_SELECT_ORGNIZATION = "select id from user_data where name=%s";
	final String SQL_SELECT_FIRST_ORGNIZATION = "select distinct(1st_orgnizationname) from user_data";
	final String SQL_SELECT_SECOND_ORGNIZATION = "select distinct(2nd_orgnizationname) from user_data";
	final String SQL_SELECT_THIRD_ORGNIZATION = "select distinct(3rd_orgnizationname) from user_data";
	final String SQL_SELECT_FOURTH_ORGNIZATION = "select distinct(4th_orgnizationname) from user_data";
	final String SQL_SELECT_FIFTT_ORGNIZATION = "select distinct(5th_orgnizationname) from user_data";
	
	final String SQL_SELECT_SECOND_ORGNIZATION_BY_PREV = "select distinct(2nd_orgnizationname) from user_data where 1st_orgnizationname=\'%s\'";
	final String SQL_SELECT_THIRD_ORGNIZATION_BY_PREV = "select distinct(3rd_orgnizationname) from user_data where 2nd_orgnizationname=\'%s\'";
	final String SQL_SELECT_FOURTH_ORGNIZATION_BY_PREV = "select distinct(4th_orgnizationname) from user_data where 3rd_orgnizationname=\'%s\'";
	final String SQL_SELECT_FIFTT_ORGNIZATION_BY_PREV = "select distinct(5th_orgnizationname) from user_data where 4th_orgnizationname=\'%s\'";
	
//	final String SQL_SELECT_FIRST_ORGNIZATION = "select first_orgnizationname from user_data";
//	final String SQL_SELECT_SECOND_ORGNIZATION = "select second_orgnizationname from user_data where first_orgnization=%s";
//	final String SQL_SELECT_THIRD_ORGNIZATION = "select third_orgnizationname from user_data where second_orgnization=%s";
//	final String SQL_SELECT_FOURTH_ORGNIZATION = "select fourth_orgnizationname from user_data where third_orgnization=%s";
//	final String SQL_SELECT_FIFTT_ORGNIZATION = "select fifth_orgnizationname from user_data where fourth_orgnization=%s";
	final String SQL_SELECT_SUB_ORGNIZATION = "select name from orgnization where parend=%s";
	final String SQL_SELECT_ORGNIZATION_ID = "select id from orgnization where name=\'%s\'";
	final String SQL_CREATE_ORGNIZATION = "create table orgnization(id int primary key NOT NULL, name varchar(100) NOT NULL, parent_id int, parent_name varchar(100) NOT NULL)";
	final String SQL_INSERT_ORGNIZATION = "insert into orgnization values(%s,\'%s\',%s,\'%s\')";
	final String SQL_DROP_ORGNIZATION = "drop table if exists orgnization";
	final String SQL_SELECT_ORGNIZATION_PARENT = "select parent from orgnization where name=%s";
	final String SQL_CREATE_USER_ORGNIZATION = "create table user_orgnization(student_number varchar(20) primary key NOT NULL, orgnization varchar(100) NOT NULL)";
	final String SQL_INSERT_USER_ORGNIZATION = "insert into user_orgnization values(\'%s\', \'%s\')";
	final String SQL_SELECT_USER_ORGNIZATION = "select orgnization from user_orgnization where student_number=%s";
	final String SQL_DROP_USER_ORGNIZATION = "drop table if exists user_orgnization";

	private static final Logger Log = LoggerFactory.getLogger(MysqlOperation.class);
	
	public MysqlOperation(String dbName, String host, String port, String userName, String password) {
		super(dbName, host, port, userName, password);
		url = "jdbc:mysql://" + host + "/odi_data_sync?useUnicode=true&characterEncoding=utf8";
	}

	@Override
	public JSONObject connect() {
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, userName, password);	
			if(con != null){
				return success;
			}
		}catch(Exception e){
			Log.error("DB connection:" + e);
		}
		return failed;
	}
	
	 public boolean tableIsExist(String name) {
		 boolean result = false;
	        try {
	            DatabaseMetaData meta = con.getMetaData();
	            ResultSet set = meta.getTables (null, null, name, null);
	            while (set.next()) {
	                result = true;
	            }
	        } catch (Exception e) {
	        	Log.error("Exception:" + e);
	        }
	        return result;
	 }
	
	public JSONObject createValidation(){
		PreparedStatement pstmt = null;
		boolean ret = false;
		
		try{
			pstmt = con.prepareStatement(SQL_CREATE_VALIDATION);
			ret = pstmt.execute();
			if(!ret){
				pstmt.close();
				pstmt = null;
				return failed;
			}
		}catch(Exception e){
			Log.error("Exception:" + e);
			return failed;
		}
		return success;
	}
	
	//status: 1:mysql结果不一致   2:数据有错误
	public JSONObject insertValidation(String student_number, int status){
		boolean ret = false;
		Statement stat;
		
		if(student_number == null || "".equals(student_number)){
			return failed;
		}
		if(status != 1 && status != 2 && status != 3  && status != 4){
			return failed;
		}
		
		try{
			String sql = String.format(SQL_INSERT_VALIDATION,student_number,status);
			Log.error(sql);
			stat = con.createStatement();
			ret = stat.execute(sql);
			if(!ret){
				return failed;
			}
		}catch(Exception e){
			Log.error("Exception:" + e);
			return failed;
		}
		return success;
	}
	
	public ResultSet selectValidation(int status){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		if(status != 1 || status != 2){
			return null;
		}
		
		try{
			sql = String.format(SQL_SELECT_VALIDATION, status);
			Log.debug("sql:" + sql);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			pstmt.close();
			pstmt = null;
		}catch(Exception e){
			Log.error("Exception:" + e);
			rs = null;
		}
		return rs;
	}
	
	public boolean userIsExistValidataion(String studentNumber){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		long count = -1;

		if(studentNumber == null || "".equals(studentNumber)){
			return false;
		}
		
		try{
			sql = String.format(SQL_SELECT_USER_IS_EXIST, studentNumber);
			Log.debug("sql:" + sql);
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
			return false;
		}
		
		return count > 0? true:false;
	}
	
	private JSONObject createOrgnization(){
		int ret = -1;
		
		try{
			Statement st=con.createStatement();
			ret =st.executeUpdate(SQL_CREATE_ORGNIZATION);

			if(ret < 0){
				return failed;
			}
		}catch(Exception e){
			Log.error("Exception:" + e);
			return failed;
		}
		return success;
	}
	
	private JSONObject insertOrgnization(int id, String name, int parentId, String parentName){
		int ret = -1;
		String sql = String.format(SQL_INSERT_ORGNIZATION, id, name, parentId, parentName);
		try{
			Statement st=con.createStatement();
			ret = st.executeUpdate(sql);
			if(ret < 0){
				return failed;
			}
		}catch(Exception e){
			Log.error("Exception:" + e);
			return failed;
		}
		
		return success;
	}
	
	private JSONObject insertOrgnization(){
		JSONObject rs = null;
		int id = 0;
		int parentId = -1;
		String name = "root";
		String parentName = "-1";
		int level = 0;
		
		try{
			rs = insertOrgnization(id, name, parentId, parentName);
			if(rs.getInt("ret") != 0){
				Log.error("create root orgnization error!");
				return failed;
			}
			
			level++;
			
			while(level < 6){
				switch(level){
				case 1:
					parentId = 0;
					parentName = "root";
					rs = selectSubOrgnizationFromUserdata(level);
					if(rs != null && rs.getInt("ret") == 0){
						JSONArray array = rs.getJSONArray("orgnizations");
						for(int i=0; i<array.length(); i++){
							JSONObject item = array.getJSONObject(i);
							name = item.getString("name");
							id++;
							JSONObject insertRet = insertOrgnization(id, name, parentId, parentName);
							if(insertRet.getInt("ret") != 0){
								Log.error(String.format("create orgnization id:%d#name:%s#parent_id:%d#parent_name:%s#", id,name,parentId,parentName));
								return failed;
							}
						}
					}
					rs = null;
					break;
				case 2:
				case 3:
				case 4:
				case 5:
					rs = selectSubOrgnizationFromUserdata(level - 1);
					if(rs != null && rs.getInt("ret") == 0){
						JSONArray array = rs.getJSONArray("orgnizations");
						for(int i=0; i<array.length(); i++){
							JSONObject item = array.getJSONObject(i);
							parentName = item.getString("name");
							parentId = (int)selectOrgnizationId(parentName);
							JSONObject subOrg = selectSubOrgnizationFromUserdata(level, parentName);
							if(subOrg != null && subOrg.getInt("ret") == 0){
								JSONArray subArray = subOrg.getJSONArray("orgnizations");
								for(int j=0; j<subArray.length(); j++){
									JSONObject subItem = subArray.getJSONObject(j);
									name = subItem.getString("name");
									id++;
									JSONObject insertRet = insertOrgnization(id, name, parentId, parentName);
									if(insertRet.getInt("ret") != 0){
										Log.error(String.format("create orgnization id:%d#name:%s#parent_id:%d#parent_name:%s#", id,name,parentId,parentName));
										return failed;
									}
								}
							}
						}
					}
					rs = null;
					break;

				default:
					Log.error("invalid level value!");
					break;
				}
				
				level++;
			}
			
		}catch(Exception e){
			Log.error("Exception:" + e);
			return failed;
		}
		return success;
	}
	
	private JSONObject selectSubOrgnizationFromUserdata(int level){
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String value = null;
		String key = null;
		JSONArray array = null;
		JSONObject ret = null;
		
		switch(level){
		case 1:
			sql = SQL_SELECT_FIRST_ORGNIZATION;
			break;
		case 2:
			sql = SQL_SELECT_SECOND_ORGNIZATION;
			break;
		case 3:
			sql = SQL_SELECT_THIRD_ORGNIZATION;
			break;
		case 4:
			sql = SQL_SELECT_FOURTH_ORGNIZATION;
			break;
		case 5:
			sql = SQL_SELECT_FIFTT_ORGNIZATION;
			break;
		default:
			return null;
		}
		
		try{
			array = new JSONArray();
			ret = new JSONObject();
			Log.debug("sql:" + sql);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				ResultSetMetaData metaData = rs.getMetaData();
				for (int j = 1; j <= metaData.getColumnCount(); ++j) {
					key = metaData.getColumnName(j).toLowerCase();
					value = rs.getString(key);
					if(value != null && !"".equals(value)){
						JSONObject json = new JSONObject();
						json.put("name", value);
						array.put(json);
					}
				}
			}
			pstmt.close();
			rs.close();
			rs = null;
			pstmt = null;
			ret.put("ret", 0);
			ret.put("orgnizations", array);
		}catch(Exception e){
			Log.error("Exception:" + e);
			ret = null;
		}
		return ret ;
	}
	
	private JSONObject selectSubOrgnizationFromUserdata(int level, String orgName){
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String value = null;
		String key = null;
		JSONArray array = null;
		JSONObject ret = null;
		
		switch(level){
		case 2:
			sql = String.format(SQL_SELECT_SECOND_ORGNIZATION_BY_PREV,orgName);
			break;
		case 3:
			sql = String.format(SQL_SELECT_THIRD_ORGNIZATION_BY_PREV,orgName);
			break;
		case 4:
			sql = String.format(SQL_SELECT_FOURTH_ORGNIZATION_BY_PREV,orgName);
			break;
		case 5:
			sql = String.format(SQL_SELECT_FIFTT_ORGNIZATION_BY_PREV,orgName);
			break;
		default:
			return null;
		}
		
		try{
			array = new JSONArray();
			ret = new JSONObject();
			Log.debug("sql:" + sql);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				ResultSetMetaData metaData = rs.getMetaData();
				for (int j = 1; j <= metaData.getColumnCount(); ++j) {
					key = metaData.getColumnName(j).toLowerCase();
					value = rs.getString(key);
					if(value != null && !"".equals(value)){
						JSONObject json = new JSONObject();
						json.put("name", value);
						array.put(json);
					}
				}
			}
			pstmt.close();
			rs.close();
			rs = null;
			pstmt = null;
			ret.put("ret", 0);
			ret.put("orgnizations", array);
		}catch(Exception e){
			Log.error("Exception:" + e);
			ret = null;
		}
		return ret ;
	}
	
	private long selectOrgnizationId(String orgnizationName){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String key = null;
		long id = -1;

		if(orgnizationName == null || "".equals(orgnizationName)){
			return -1;
		}
		
		try{
			sql = String.format(SQL_SELECT_ORGNIZATION_ID, orgnizationName);
			Log.debug("sql:" + sql);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				ResultSetMetaData metaData = rs.getMetaData();
				for (int j = 1; j <= metaData.getColumnCount(); ++j) {
					key = metaData.getColumnName(j).toLowerCase();
					if(key.equals("id")){
						id = rs.getInt(j);
					}
				}
			}
			pstmt.close();
		}catch(Exception e){
			Log.error("Exception:" + e);
			return -1;
		}
		
		return id;
	}
	
	public JSONObject initOrgnization(){
		int ret = -1;
		JSONObject json = null;
		
		try{
			Statement st=con.createStatement();
			ret = st.executeUpdate(SQL_DROP_ORGNIZATION);
			if(ret < 0){
				return failed;
			}
			
			json = new JSONObject();
			json = createOrgnization();
			if(json.getInt("ret") != 0){
				return failed;
			}
			json = insertOrgnization();
			if(json.getInt("ret") != 0){
				return failed;
			}
		}catch(Exception e){
			Log.error("Exception:" + e);
			return failed;
		}
		return success;
	}
	
	private JSONObject createUserOrgnization(){
		int ret = -1;
		
		try{
			Statement st=con.createStatement();
			ret =st.executeUpdate(SQL_CREATE_USER_ORGNIZATION);

			if(ret < 0){
				return failed;
			}
		}catch(Exception e){
			Log.error("Exception:" + e);
			return failed;
		}
		return success;
	}
	
	private JSONObject insertUserOrgnization(String studentNumber, String orgnizaiton) throws SQLException{
		if(studentNumber == null || "".equals(studentNumber)){
			return failed;
		}
		if(studentNumber == null || "".equals(studentNumber)){
			return failed;
		}
		
		int ret = -1;
		String sql = String.format(SQL_INSERT_USER_ORGNIZATION, studentNumber, orgnizaiton);
		try{
			Statement st=con.createStatement();
			ret = st.executeUpdate(sql);
			if(ret < 0){
				return failed;
			}
		}catch(Exception e){
			Log.error("Exception:" + e);
			return failed;
		}
		
		return success;
	}
	
	private JSONObject insertUserOrgnization(){
		long count = 0;
		int pos = 0;
		int len = 1000;
		ResultSet rs = null;
		JSONObject ret = null;
		String key = null;
		String studentNumber = null;
		String orgnization = null;
		int flag = 0;
		String orgName = null;
		
		try{
			count = super.selectCount("user_data");
			if(count <= 0){
				Log.error("select inner table error!");
				return failed;
			}
			
			while(len > 0){
				rs = super.selectData("user_data", pos, len);
				while(rs.next()){
					ResultSetMetaData metaData = rs.getMetaData();
					flag = 0;
					for (int j = 1; j <= metaData.getColumnCount(); ++j) {
						key = metaData.getColumnName(j).toLowerCase();
						if(key.equals("student_number")){
							studentNumber = rs.getString(key);
						}
						if(key.equals("1st_orgnizationname") && flag <= 1){
							orgName = rs.getString(key);
							if(orgName != null && !"".equals(orgName)){
								orgnization = orgName;
							}
							flag = 1;
						}
						if(key.equals("2nd_orgnizationname") && flag <= 2){
							orgName = rs.getString(key);
							if(orgName != null && !"".equals(orgName)){
								orgnization = orgName;
							}
							flag = 2;
						}
						if(key.equals("3rd_orgnizationname") && flag <= 3){
							orgName = rs.getString(key);
							if(orgName != null && !"".equals(orgName)){
								orgnization = orgName;
							}
							flag = 3;
						}
						if(key.equals("4th_orgnizationname") && flag <= 4){
							orgName = rs.getString(key);
							if(orgName != null && !"".equals(orgName)){
								orgnization = orgName;
							}
							flag = 4;
						}
						if(key.equals("5th_orgnizationname") && flag <= 5){
							orgName = rs.getString(key);
							if(orgName != null && !"".equals(orgName)){
								orgnization = orgName;
							}
							flag = 5;
						}
					}
					ret = insertUserOrgnization(studentNumber, orgnization);
					if(ret.getInt("ret") != 0){
						Log.error("insert to user_orgnization error,data:student_number:%s,orgnization:%s",studentNumber,orgnization);
						return failed;
					}
				}
				rs.close();
				rs = null;
				if(count < len)
					break;
				if(pos == count) 
					break;
				pos += len;
				if(pos > count && pos - count < len){
					pos = pos - len;
					len = (int)count - pos;
				}
			}
		}catch(Exception e){
			Log.error("Exception:" + e);
			return failed;
		}
		
		return success;
	}
	
	public String selectUserOrgnization(String studentNumber){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String key = null;
		String name = null;

		if(studentNumber == null || "".equals(studentNumber)){
			return null;
		}
		
		try{
			sql = String.format(SQL_SELECT_USER_ORGNIZATION, studentNumber);
			Log.debug("sql:" + sql);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				ResultSetMetaData metaData = rs.getMetaData();
				for (int j = 1; j <= metaData.getColumnCount(); ++j) {
					key = metaData.getColumnName(j).toLowerCase();
					if(key.equals("orgnization")){
						name = rs.getString(j);
					}
				}
			}
			pstmt.close();
		}catch(Exception e){
			Log.error("Exception:" + e);
			return null;
		}
		
		return name;
	}
	
	public JSONObject initUserOrgnization(){
		int ret = -1;
		JSONObject json = null;
		
		try{
			Statement st=con.createStatement();
			ret = st.executeUpdate(SQL_DROP_USER_ORGNIZATION);
			if(ret < 0){
				return failed;
			}
			
			json = new JSONObject();
			json = createUserOrgnization();
			if(json.getInt("ret") != 0){
				return failed;
			}
			json = insertUserOrgnization();
			if(json.getInt("ret") != 0){
				return failed;
			}
		}catch(Exception e){
			Log.error("Exception:" + e);
			return failed;
		}
		return success;
	}

	public JSONObject checkOrgnizationIsExist(String orgnizationName) throws JSONException{
		if(orgnizationName == null || "".equals(orgnizationName)){
			return failed;
		}
		if(selectOrgnizationId(orgnizationName) > 0){
			return success;
		}
		else{
			return failed;
		}
	}
	
	private int selectOrgnizationParent(String orgnizationName){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String key = null;
		int value = -1;

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
						value = rs.getInt(j);
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
	
	
	public JSONObject getValidationData() throws JSONException{
		return super.selectAllData("validation");
	}
}
