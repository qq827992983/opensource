package dsv.tool.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbFactory {
	public static DbFactory instance;
	private static final Logger Log = LoggerFactory.getLogger(DbFactory.class);
	static{
		if(instance == null){
			instance = new DbFactory();
		}
	}
	
	private DbFactory(){
	}
	
	public static DbFactory getInstance(){
		if(instance == null){
			instance = new DbFactory();
		}
		return instance;
	}
	
	public Connector createDbOperation(String type,String dbName, String host, String port, String userName, String password){
		Log.error("type:" + type + ",host:" + host + ",port:" + port + ",userName:" + userName + ",password:" + password);
		if(type.toLowerCase().equals("mysql")){
			return new MysqlOperation(dbName, host, port, userName, password);
		}else if(type.toLowerCase().equals("mongo")){
			return new MongoOperation(dbName, host, port, userName, password);
		}else{
			return null;
		}
	}
}
