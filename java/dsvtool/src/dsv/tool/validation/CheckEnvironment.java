package dsv.tool.validation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONException;
import org.json.JSONObject;

public class CheckEnvironment implements Reports {
	Logger Log = LoggerFactory.getLogger(CheckEnvironment.class);
	public CheckEnvironment(){
	}
	
	private int executeShellCmd(String shellCmd) throws IOException{
		int success = -1;
		StringBuffer stringBuffer = new StringBuffer();
		BufferedReader bufferedReader = null;
		try {
			Process pid = null;
			String[] cmd = {"/bin/sh", "-c", shellCmd};
			pid = Runtime.getRuntime().exec(cmd);
			if (pid != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(pid.getInputStream()), 1024);
				pid.waitFor();
			} else {
				String line = null;
				while (bufferedReader != null &&(line = bufferedReader.readLine()) != null) {
					stringBuffer.append(line).append("\r\n");
				}
			}
		} catch (Exception e) {
			Log.error("Exception:" + e);
		} finally {
			String line = "";
			while((line = bufferedReader.readLine()) != null){
				if(line != null && line.length() > 0){
					success = 0;
					break;
				}
			}
		}
		
		return success;
	}
	
	public JSONObject checkPluginEnv() throws JSONException{
		try{
			int ret = executeShellCmd("ls /usr/bin/lua");
			if(ret < 0){
				ret = executeShellCmd("ls /usr/local/bin/lua");
			}
			
			if(ret < 0){
				return new JSONObject(pluginEnvError);
			}
			
			ret = executeShellCmd("ls /usr/bin/isql");
			if(ret < 0){
				ret = executeShellCmd("ls /usr/local/bin/isql");
			}
			
			if(ret < 0){
				return new JSONObject(pluginEnvError);
			}
			
		}catch(Exception e){
			return new JSONObject(pluginEnvError);
		}
		
		return new JSONObject(success);
	}
	
	public JSONObject checkPluginPresence() throws JSONException{
		try{
			int ret = executeShellCmd("ls /usr/local/whistle/openfire/reources/auth/lua");

			if(ret < 0){
				return new JSONObject(pluginEnvError);
			}
			
			ret = executeShellCmd("ls /usr/local/whistle/openfire/reources/auth/lua/odi_include");
			if(ret < 0){
				return new JSONObject(pluginEnvError);
			}
		}catch(Exception e){
			return new JSONObject(pluginEnvError);
		}
		
		return new JSONObject(success);
	}
	
}
