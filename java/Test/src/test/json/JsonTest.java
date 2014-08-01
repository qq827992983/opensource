package test.json;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonTest {

	static void testKeys() throws JSONException{
		JSONObject json = new JSONObject("{\"key\": \"filename\",\"value\" :\"文件路径\"}");
		Iterator iter = json.keys();
		while(iter.hasNext()){
			String key = (String )iter.next();
			String value = json.getString(key);
			System.out.println(key+":"+value);
		}
	}
	public static void main(String[] args) throws JSONException {
		// TODO Auto-generated method stub
		testKeys();
	}

}
