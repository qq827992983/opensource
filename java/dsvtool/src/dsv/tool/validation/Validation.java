package dsv.tool.validation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dsv.tool.db.Connector;
import dsv.tool.db.DbFactory;
import dsv.tool.db.MysqlOperation;

public class Validation {
	Connector inner;
	Connector outer;
	CheckEnvironment checkEnv;
	CheckDbData checkData;
	JSONObject result;
	private String confFile;
	private String dbType;
	private String dbName;
	private String host;
	private String port;
	private String userName;
	private String password;
	Logger Log = LoggerFactory.getLogger(Validation.class);
	final String filePath = "validation_repost.txt";
	public Validation(String file){
		confFile = file;
		getDbConfig("InnerDB");
		inner = DbFactory.getInstance().createDbOperation(dbType, dbName, host, port, userName, password);
		getDbConfig("OuterDB");
		outer = DbFactory.getInstance().createDbOperation(dbType, dbName, host, port, userName, password);
		checkEnv = new CheckEnvironment();
		checkData = new CheckDbData();
		result = new JSONObject();
	}
	
	 public Document loadXmlFile(String fileName) throws MalformedURLException, DocumentException {
		 SAXReader reader = new SAXReader();
		 Document document = reader.read(new File(fileName));
		 return document;
	 }
	 
	private void getDbConfig(String db){
		try{
			Document doc = loadXmlFile(confFile);
			Element root = doc.getRootElement();
			Element element = root.element(db);
			dbType = element.elementText("type").toLowerCase().trim();
			dbName = element.elementText("dbname").toLowerCase().trim();
			host = element.elementText("host").toLowerCase().trim();
			port = element.elementText("port").toLowerCase().trim();
			userName = element.elementText("username").toLowerCase().trim();
			password = element.elementText("password").toLowerCase().trim();
		}catch(Exception e){
			Log.error("Exception:" + e);
		}
	}
	
	public void start(){
		try{
			JSONObject ret = null;
//			ret = checkEnv.checkPluginEnv();
//			if(ret.getInt("ret") == 0){
//				result.put("check_plug_env", "ok");
//			}else{
//				result.put("check_plug_env", "error");
//			}
//			
//			ret = checkEnv.checkPluginPresence();
//			if(ret.getInt("ret") == 0){
//				result.put("check_plug_presence", "ok");
//			}else{
//				result.put("check_plug_presence", "error");
//			}
			
			ret = checkData.checkConnectOfDB(inner);
			if(ret.getInt("ret") == 0){
				result.put("check_connect_innerdb", "ok");
			}else{
				result.put("check_connect_innerdb", "error");
			}

			ret = checkData.checkConnectOfDB(outer);
			if(ret.getInt("ret") == 0){
				result.put("check_connect_outerdb", "ok");
			}else{
				result.put("check_connect_outerdb", "error");
			}

			ret = checkData.checkPersonalInfo(inner, outer);
			if(ret.getInt("ret") == 0){
				result.put("check_personal_info", checkData.getCheckPersonalInfoResult(outer));
			}else{
				result.put("check_personal_info", "error");
			}
			
			ret = checkData.checkOrgnizationTree(inner, outer);
			if(ret.getInt("ret") == 0){
				result.put("check_orgnization_tree", ret.getJSONObject("result"));	
			}else{
				result.put("check_orgnization_tree", "error");	
			}
			
			ret = checkData.checkOrgnizationRelationship(inner, outer);
			if(ret.getInt("ret") == 0){
				result.put("check_orgnization_relationship", checkData.getCheckPersonalInfoResult(outer));
			}else{
				result.put("check_orgnization_relationship", "error");
			}
			
			save(result.toString());
		}catch(Exception e){
			Log.error("Exception:" + e);
		}
	}
	
	public void stop(){
		inner.close();
		outer.close();
	}
	
	public void save(String context) throws IOException{
		if(context == null || "".equals(context)){
			Log.error("context is null!");
			return;
		}
		File file = new File(filePath);
		FileWriter writer = new FileWriter(file);
		writer.write(context);
		writer.flush();
		writer.close();
	}
}
