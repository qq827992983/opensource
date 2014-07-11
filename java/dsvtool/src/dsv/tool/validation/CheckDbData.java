package dsv.tool.validation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.Mongo;
import com.mongodb.util.JSON;

import dsv.tool.db.Connector;
import dsv.tool.db.MongoOperation;
import dsv.tool.db.MysqlOperation;

public class CheckDbData implements Reports {
	Logger Log = LoggerFactory.getLogger(CheckEnvironment.class);
	JSONObject checkConnResult;
	JSONObject checkPersonalInfoResult;
	JSONObject checkOrgTreeResult;
	JSONObject checkOrgRelationshipResult;
	final String userData = "user_data";
	final String ofuser = "ofuser";
	final String orgnization = "orgnization";
	final String login = "login";
	final String orgnizationMember = "orgnization_member";
	final String validation = "validation";
	
	public CheckDbData(){
		try{
			checkConnResult = new JSONObject();
			checkPersonalInfoResult = new JSONObject();
			checkOrgTreeResult = new JSONObject();
			checkOrgRelationshipResult = new JSONObject();
		}catch(Exception e){
			Log.error("Exception:" + e);
		}
	}
	
	private JSONObject checkOuterFromInner(Connector inner, Connector outer) throws JSONException{
		long count = 0;
		int pos = 0;
		int len = 1000;
		ResultSet rsOuter = null;
		JSONObject rsInner = null;
		JSONObject retFailed = new JSONObject(failed);
		JSONObject retSuccess = new JSONObject(success);
		JSONObject ret = null;
		boolean isExist = false;
		
		if(inner == null || outer == null){
			return retFailed;
		}
		
		MysqlOperation mysql = (MysqlOperation)outer;
		
		try{
			count = outer.selectCount(userData);
			if(count <= 0){
				Log.error("select outer table error!");
				return retFailed;
			}
			Log.error("count:" + count );
			
			if(!mysql.tableIsExist(validation)){
				ret = mysql.createValidation();
				if(ret.getInt("ret") != 0){
					Log.error("create validation error!");
					return retFailed;
				}	
			}
			
			while(len > 0){
				rsOuter = outer.selectData(userData, pos, len);
				while(rsOuter.next()){
					rsInner = inner.selectDataByStudentNumber(ofuser, rsOuter.getString("student_number"));
					Log.error(rsInner.toString());
					JSONObject json = outer.resultToJson(rsOuter);
					Log.error(json.toString());
					ret = outer.compareResultSet(json, rsInner);
					Log.error(ret.toString());
					switch(ret.getInt("ret")){
					case -1:
						Log.error("compare rsOuter and rsInner error!");
						isExist = mysql.checkUserIsExist(validation, rsOuter.getString("student_number"));
						if(!isExist){
							ret = mysql.insertValidation(rsOuter.getString("student_number"), 3);
							if(ret != null && ret.getInt("ret") != 0){
								Log.error(String.format("insert into validation data error! student_number:%s,status:3",rsOuter.getString("student_number")));
							}
						}
						break;
					case 0:
						break;
					case 1:
						isExist = mysql.checkUserIsExist(validation, rsOuter.getString("student_number"));
						if(!isExist){
							ret = mysql.insertValidation(rsOuter.getString("student_number"), 1);
							if(ret != null && ret.getInt("ret") != 0){
								Log.error(String.format("insert into validation data error! student_number:%s,status:1",rsOuter.getString("student_number")));
							}
						}
						break;
					default:
						Log.error("compare rsOuter and rsInner error,return error value!");
						break;
					}
				}
				outer.SelectDataClose();
				
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
			return retFailed;
		}
		
		return retSuccess;
	}
	
	private JSONObject checkInnerFromOuter(Connector outer, Connector inner) throws JSONException{
		long count = 0;
		int pos = 0;
		int len = 1000;
		JSONObject rsOuter = null;
		ResultSet rsInner = null;
		JSONObject retFailed = new JSONObject(failed);
		JSONObject retSuccess = new JSONObject(success);
		JSONObject ret = null;
		boolean isExist = false;
		
		if(inner == null || outer == null){
			return retFailed;
		}
		
		MysqlOperation mysql = (MysqlOperation)outer;
		MongoOperation mongo = (MongoOperation)inner;
		
		try{
			count = inner.selectCount(ofuser);
			if(count <= 0){
				Log.error("select inner table error!");
				return retFailed;
			}
			Log.error("count:" + count );
			
			if(!mysql.tableIsExist(validation)){
				ret = mysql.createValidation();
				if(ret.getInt("ret") != 0){
					Log.error("create validation error!");
					return retFailed;
				}	
			}
			
			while(len > 0){
				rsInner = inner.selectData(ofuser, pos, len);
				while(rsInner.next()){
					rsOuter = outer.selectDataByStudentNumber(userData, rsInner.getString("student_number"));
					Log.error(rsInner.toString());
					JSONObject json = outer.resultToJson(rsInner);
					Log.error(json.toString());
					ret = inner.compareResultSet(json, rsOuter);
					Log.error(ret.toString());
					switch(ret.getInt("ret")){
					case -1:
						Log.error("compare rsOuter and rsInner error!");
						isExist = mysql.checkUserIsExist(validation, rsInner.getString("student_number"));
						if(!isExist){
							ret = mysql.insertValidation(rsInner.getString("student_number"), 1);
							if(ret != null && ret.getInt("ret") != 0){
								Log.error(String.format("insert into validation data error! student_number:%s,status:1",rsInner.getString("student_number")));
							}
						}
						break;
					case 0:
						break;
					case 1:
						isExist = mysql.checkUserIsExist(validation, rsInner.getString("student_number"));
						if(!isExist){
							ret = mysql.insertValidation(rsInner.getString("student_number"), 2);
							if(ret != null && ret.getInt("ret") != 0){
								Log.error(String.format("insert into validation data error! student_number:%s,status:2",rsInner.getString("student_number")));
							}
						}
						break;
					default:
						Log.error("compare rsOuter and rsInner error,return error value!");
						break;
					}
				}
				inner.SelectDataClose();
				
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
			return retFailed;
		}
		
		return retSuccess;
	}
	
	public JSONObject checkConnectOfDB(Connector con){
		JSONObject ret = con.connect();
		return ret;
	}
	
	public JSONObject checkPersonalInfo(Connector inner, Connector outer) throws JSONException{
		ResultSet rs = null;
		JSONObject ret = null;
		JSONObject retFailed = new JSONObject(failed);
		JSONObject retSuccess = new JSONObject(success);
		
		if(outer == null || inner == null){
			return retFailed;
		}
		
		MongoOperation mongo = (MongoOperation)inner;
		try{
			ret = checkOuterFromInner(inner, outer);
			Log.error(ret.toString());
			if(ret.getInt("ret") != 0){
				Log.error("call checkOuterFromInner error!");
				ret = retFailed;
			}else{
				ret = checkInnerFromOuter(outer, inner);
				if(ret.getInt("ret") != 0){
					Log.error("call checkInnerFromOuter error!");
					ret = retFailed;
				}
			}
		}catch(Exception e){
			Log.error("Exception:" + e);
			ret = retFailed;
		}
		ret = retSuccess;
		return ret;
	}
	
	public JSONObject checkOrgnizationTree(Connector inner, Connector outer) throws JSONException{
		String orgName = null;
		JSONObject rsOuter = null;
		JSONObject retFailed = new JSONObject(failed);
		JSONObject retSuccess = new JSONObject(success);
		JSONArray array = null;;
		JSONObject item = null;
		JSONArray result = new JSONArray();
		JSONObject ret = null;
		
		if(inner == null || outer == null){
			return retFailed;
		}
		
		MongoOperation mongo = (MongoOperation)inner;
		MysqlOperation mysql = (MysqlOperation)outer;
		
		try{
			
			if(!mysql.tableIsExist(validation)){
				ret = mysql.createValidation();
				if(ret.getInt("ret") != 0){
					Log.error("create validation error!");
					return retFailed;
				}	
			}
			
			ret = mysql.initOrgnization();
			if(ret.getInt("ret") != 0){
				Log.error("create orgnization error!");
				return retFailed;
			}	
			
			rsOuter = outer.selectAllData(orgnization);
			if(rsOuter.getString("result").equals("ok")){
				array = rsOuter.getJSONArray("data");
				for(int i=0; i<array.length(); i++){
					item = array.getJSONObject(i);
					orgName = item.getString("name");
					if(orgName.equals("root")){
						continue;
					}
					boolean isExistInner = mongo.checkOrgnizationIsExist(orgName);
					if(!isExistInner){
						JSONObject json = new JSONObject();
						json.put("name", orgName);
						result.put(json);
					}
				}
			}
		}catch(Exception e){
			Log.error("Exception:" + e);
			return retFailed;
		}
		ret = new JSONObject();
		ret.put("result", "ok");
		ret.put("data", result);
		retSuccess.put("result", ret);
		return retSuccess;
	}
	
	public JSONObject checkOrgnizationRelationship(Connector inner, Connector outer) throws JSONException{
		long count = 0;
		int pos = 0;
		int len = 1000;
		List<String> rsOuter = null;
		JSONObject retFailed = new JSONObject(failed);
		JSONObject retSuccess = new JSONObject(success);
		JSONObject ret = null;
		String studentNumber = null;
		String orgInnerName = null;
		String username = null;
		long orgId = -1;
		String orgOuterName = null;
		
		if(inner == null || outer == null){
			return retFailed;
		}
		
		MysqlOperation mysql = (MysqlOperation)outer;
		MongoOperation mongo = (MongoOperation)inner;
		
		try{
			count = outer.selectCount(userData);
			if(count <= 0){
				Log.error("select outer table error!");
				return retFailed;
			}
			
			if(!mysql.tableIsExist(validation)){
				ret = mysql.createValidation();
				if(ret.getInt("ret") != 0){
					Log.error("create validation error!");
					return retFailed;
				}	
			}
			
			ret = mysql.initUserOrgnization();
			if(ret.getInt("ret") != 0){
				Log.error("init user_orgnization error!");
				return retFailed;
			}	
			
			while(len > 0){
				rsOuter = outer.selectStudentNumber(userData, pos, len);
				for(int i=0; i<rsOuter.size(); i++){
					studentNumber = rsOuter.get(i);
					orgInnerName = mysql.selectUserOrgnization(studentNumber);
					if(orgInnerName != null && "".equals(orgInnerName)){
						username = mongo.selectUsername(studentNumber);	
					}else{
						boolean isExist = mysql.userIsExistValidataion(studentNumber);
						if(!isExist){
							ret = mysql.insertValidation(studentNumber, 4);//用户组织结构关系错误
							if(ret != null && ret.getInt("ret") != 0){
								Log.error(String.format("insert into validation data error! student_number:%s,status:4",studentNumber));
							}
						}
						continue;
					}
					if(username != null && "".equals(username)){
						orgId = mongo.selectUserOrgnization(username);
					}
					if(orgId > 0){
						orgOuterName = mongo.selectOrgnizationname(Integer.toString((int)orgId));
						if(!orgInnerName.equals(orgOuterName)){
							boolean isExistValidataon = mysql.userIsExistValidataion(studentNumber);
							if(!isExistValidataon){
								boolean isExist = mysql.userIsExistValidataion(studentNumber);
								if(!isExist){
									ret = mysql.insertValidation(studentNumber, 4);//用户组织结构关系错误
									if(ret != null && ret.getInt("ret") != 0){
										Log.error(String.format("insert into validation data error! student_number:%s,status:4",studentNumber));
									}
								}
							}
						}	
					}
				}
				
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
			return retFailed;
		}
		
		return retSuccess;
	}
	
	public JSONObject getCheckPersonalInfoResult(Connector outer) throws JSONException{
		MysqlOperation mysql = (MysqlOperation)outer;
		return mysql.getValidationData();
	}
}
