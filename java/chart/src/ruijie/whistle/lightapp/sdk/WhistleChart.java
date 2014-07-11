package ruijie.whistle.lightapp.sdk;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import ruijie.whistle.lightapp.request.EventRequest;
import ruijie.whistle.lightapp.request.HelloRequest;
import ruijie.whistle.lightapp.request.ImageRequest;
import ruijie.whistle.lightapp.request.LinkRequest;
import ruijie.whistle.lightapp.request.LocationRequest;
import ruijie.whistle.lightapp.request.Request;
import ruijie.whistle.lightapp.request.TextRequest;
import ruijie.whistle.lightapp.request.UnknowRequest;
import ruijie.whistle.lightapp.request.VoiceRequest;
import ruijie.whistle.lightapp.response.Response;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONException;
import org.json.JSONObject;

/**
* Note:抽象类,负责微哨开放平台处理<br> <br> 
* 开发每一个LightApp实例都需要继承该类
*/ 
public abstract class WhistleChart extends HttpServlet {

	private String appID;
	private String appSecret;
	private String appToken;
	protected static String accessToken;
	private ReentrantLock lock = new ReentrantLock();
	long accessTokenTime = 0;
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	* Note:WhistleChart构造方法 <br> <br>
	* 	public WhistleChart(String appid, String appsecret, String apptoken);
	* @param appid 开发者添加应用时生成的AppID
	* @param appsecret 开发者添加应用时生成的AppSecret
	* @param apptoken 开发者添加应用时填写的轻应用通讯凭证
	* @return 无
	* @exception 无
	*/ 
	public WhistleChart(String appid, String appsecret, String apptoken) {
		this.appID = appid;
		this.appSecret = appsecret;
		this.appToken = apptoken;
	}

	/**
	* Note:处理Hello报文,用户开发LightApp需要实现此方法 <br> <br>
	* 	protected abstract Response onRecvHelloMessage(HttpServletResponse response, HelloRequest request);
	* @param response 用户可根据此设置响应内容
	* @param request 表示Hello请求报文
	* @return 响应对象
	*/ 
	protected abstract Response onRecvHelloMessage(HttpServletResponse response, HelloRequest request);
	/**
	* Note:处理文本请求报文,用户开发LightApp需要实现此方法 <br> <br>
	* 	protected abstract Response onRecvTextMessage(HttpServletResponse response, TextRequest request);
	* @param response 用户可根据此设置响应内容
	* @param request 表示文本请求报文
	* @return 响应对象
	*/ 
	protected abstract Response onRecvTextMessage(HttpServletResponse response, TextRequest request);
	/**
	* Note:处理图片请求报文,用户开发LightApp需要实现此方法 <br> <br>
	* 	protected abstract Response onRecvImageMessage(HttpServletResponse response, ImageRequest request);
	* @param response 用户可根据此设置响应内容
	* @param request 表示图片请求报文
	* @return 响应对象
	*/ 
	protected abstract Response onRecvImageMessage(HttpServletResponse response, ImageRequest request);
	/**
	* Note:处理音频请求报文,用户开发LightApp需要实现此方法 <br> <br>
	* 	protected abstract Response onRecvVoiceMessage(HttpServletResponse response, VoiceRequest request);
	* @param response 用户可根据此设置响应内容
	* @param request 表示音频请求报文
	* @return 响应对象
	*/ 
	protected abstract Response onRecvVoiceMessage(HttpServletResponse response, VoiceRequest request);
	/**
	* Note:处理事件请求报文,用户开发LightApp需要实现此方法 <br> <br>
	* 	protected abstract Response onRecvEventMessage(HttpServletResponse response, EventRequest request);
	* @param response 用户可根据此设置响应内容
	* @param request 表示事件请求报文
	* @return 响应对象
	*/ 
	protected abstract Response onRecvEventMessage(HttpServletResponse response, EventRequest request);
	/**
	* Note:处理位置请求报文,用户开发LightApp需要实现此方法 <br> <br>
	* 	protected abstract Response onRecvLocationMessage(HttpServletResponse response, LocationRequest request);
	* @param response 用户可根据此设置响应内容
	* @param request 表示位置请求报文
	* @return 响应对象
	*/ 
	protected abstract Response onRecvLocationMessage(HttpServletResponse response, LocationRequest request);
	/**
	* Note:处理链接请求报文,用户开发LightApp需要实现此方法 <br> <br>
	* 	protected abstract Response onRecvHelloMessage(HttpServletResponse response, HelloRequest request);
	* @param response 用户可根据此设置响应内容
	* @param request 表示链接请求报文
	* @return 响应对象
	*/ 
	protected abstract Response onRecvLinkMessage(HttpServletResponse response, LinkRequest request);
	/**
	* Note:处理未知请求报文,用户开发LightApp需要实现此方法 <br> <br>
	* 	protected abstract Response onRecvUnknowMessage(HttpServletResponse response, UnknowRequest request, String context);
	* @param response 用户可根据此设置响应内容
	* @param request 表示未知报文
	* @param context 错误提示信息内容
	* @return 响应对象
	*/ 
	protected abstract Response onRecvUnknowMessage(HttpServletResponse response, UnknowRequest request, String context);
	
	/**
	* Note:实现HttpServlet的doGet方法 <br> <br>
	* 	protected void doGet(HttpServletRequest request, HttpServletResponse response);
	* @param response Http响应对象
	* @param request Http请求对象
	* @return 无
	* @exception IOException
	*/ 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> params = getParams(request);
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);

		try {
			if (!valid(params)) {
				try {
					response.getWriter().println("wrong signature");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
			String echoStr = params.get("echostr");
			response.getWriter().println(echoStr);
		} catch (IOException e) {
			System.out.println("doGet Exception:" + e.getMessage());
		}
	}

	/**
	* Note:实现HttpServlet的doPost方法 <br> <br>
	* 	protected void doPost(HttpServletRequest request, HttpServletResponse response);
	* @param response Http响应对象
	* @param request Http请求对象
	* @return 无
	* @exception IOException
	*/ 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, String> params = getParams(request);
		String data = getPostData(request);

		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);

		if (!valid(params)) {
			try {
				response.getWriter().println("wrong signature");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		Element dataElement = null;
		try {
			SAXReader xmlReader = new SAXReader();
			dataElement = xmlReader.read(new StringReader(data)).getRootElement();
		} catch (Exception e) {
			System.out.println("Error retrieving lightapp message: " + e);
		}
		
		//TODO:构造Request类对象
		Element content = dataElement.element("content");

		if (content == null) {
			dataElement.addElement("content");
		}
		String msgType = dataElement.elementText("msgtype");
		switch (msgType) {
		case "hello":
			HelloRequest helloRequest = new HelloRequest(dataElement);
			Response helloResponse = onRecvHelloMessage(response, helloRequest);
			helloResponse.writeTo(response.getWriter());
			break;
		case "text":
			TextRequest textRequest = new TextRequest(dataElement);
			Response textResponse = onRecvTextMessage(response, textRequest);
			textResponse.writeTo(response.getWriter());
			break;
		case "music":
			VoiceRequest voiceRequest = new VoiceRequest(dataElement);
			Response voiceResponse = onRecvVoiceMessage(response, voiceRequest);
			voiceResponse.writeTo(response.getWriter());
			break;
		case "image":
			ImageRequest imageRequest = new ImageRequest(dataElement);
			Response imageResponse = onRecvImageMessage(response, imageRequest);
			imageResponse.writeTo(response.getWriter());
			break;
		case "location":
			LocationRequest locationRequest = new LocationRequest(dataElement);
			Response locationResponse = onRecvLocationMessage(response, locationRequest);
			locationResponse.writeTo(response.getWriter());
			break;
		case "link":
			LinkRequest urlRequest = new LinkRequest(dataElement);
			Response urlResponse = onRecvLinkMessage(response, urlRequest);
			urlResponse.writeTo(response.getWriter());
			break;
		case "event":
			EventRequest eventRequest = new EventRequest(dataElement);
			Response eventResponse = onRecvEventMessage(response, eventRequest);
			eventResponse.writeTo(response.getWriter());
			break;
		default:
			UnknowRequest unknownRequest = new UnknowRequest(dataElement);
			Response unknownResponse = onRecvUnknowMessage(response, unknownRequest, "");
			unknownResponse.writeTo(response.getWriter());
			break;
		}
	}

	private static boolean isEmptyString(String str) {
		return str == null || str.isEmpty();
	}

	private String getPostData(HttpServletRequest request) {
		try {
			StringBuilder sb = new StringBuilder();
			char[] buf = new char[1024];
			request.setCharacterEncoding("utf-8");
			BufferedReader in = request.getReader();
			for (int count = in.read(buf); count >= 0; count = in.read(buf)) {
				sb.append(buf, 0, count);
			}

			return sb.toString();
		} catch (Exception e) {
			System.out.println("getPostData Exception:" + e.getMessage());
			return null;
		}
	}

	private static String getFormattedText(byte[] bytes) {
		int len = bytes.length;
		StringBuilder buf = new StringBuilder(len * 2);

		for (int j = 0; j < len; j++) {
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}
		return buf.toString();
	}

	private static String sha1Encrypt(String str) {
		if (str == null) {
			return null;
		}
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			messageDigest.update(str.getBytes());
			return getFormattedText(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private boolean valid(Map<String, String> params) {
		try {
			String timestamp = params.get("timestamp");
			String nonce = params.get("nonce");
			String requestSignature = params.get("signature");

			ArrayList<String> list = new ArrayList<String>();
			list.add(String.valueOf(timestamp));
			list.add(String.valueOf(nonce));
			list.add(appToken);
			Collections.sort(list);
			String signature = sha1Encrypt(list.get(0) + list.get(1)
					+ list.get(2));
			System.out.println("sig=" + signature);
			System.out.println("sigReq=" + requestSignature);
			return signature.equals(requestSignature.toLowerCase());
		} catch (Exception e) {
			return false;
		}
	}

	private boolean doAuth(String url, long tokenTime) {
		lock.lock();
		try {
			if (accessTokenTime > tokenTime){
				lock.unlock();
                return true;
            }
			
			if (!url.startsWith("https://")){
                throw new IllegalArgumentException("非https请求");
            }
			
			int end = url.indexOf("/", 8);
			if(end < 0){
				throw new IllegalArgumentException("非法的URL");
			}
			
			String host = url.substring(0, end);
            if (host.length() == 0){
                throw new IllegalArgumentException("非法的主机名");
            }
            
            String authURL = host + "/cgi-bin/oauth2/access_token?grant_type=client_credentials&appid=" + appID + "&appsecret=" + appSecret;

			JSONObject jsonObj = doUrl0("GET", authURL, null);
			accessToken = jsonObj.getString("access_token");
			accessTokenTime = System.currentTimeMillis();
			return true;
		} catch (Exception e) {
			System.out.println("Exception :" + e);
		} finally {
			lock.unlock();
		}
		return false;

	}

	private static String ArrayToString(String[] arr) {
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			bf.append(arr[i]);
		}
		return bf.toString();
	}

	private Map<String, String> getParams(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		String query = request.getQueryString();

		if (isEmptyString(query)) {
			return params;
		}

		try {
			String pairs[] = query.split("&");
			for (String pair : pairs) {
				String para[] = pair.split("=");
				if (para.length == 2) {
					params.put(para[0], para[1]);
				}
			}
		} catch (Exception e) {
			System.out.println("getParams Exception:" + query + ","
					+ e.getMessage());
		}

		return params;
	}

	/**
	* Note:此方法用于调用Whistle平台HttpAPI <br> <br>
	* 	public JSONObject doUrl(String method, String url, String data);
	* @param method GET 或 POST
	* @param url HttpApi的URL,参考《微哨平台第三方系统接入开发文档.docx》
	* @return JSON格式的结果
	* @exception Exception
	*/ 
	public JSONObject doUrl(String method, String url, String data) {
		lock.lock();
		long oldTime = accessTokenTime;
		String token = accessToken;
		lock.unlock();

		if (oldTime == 0 && !doAuth(url, oldTime)) {
			return null;
		}else{
			token = accessToken;
		}
		
		if (!url.startsWith("https://")){
            throw new IllegalArgumentException("非https请求");
        }
		
		if(url.indexOf("?") < 0){
			if(url.endsWith("/")){
				url = url.substring(0,url.length() - 1);
			}
		}
		url = url + "&access_token=" + token;
		url = url + "&appid=" + appID;
		
		method = method.toUpperCase();
		JSONObject jsonObj = doUrl0(method, url, data);
		try {
			if (Integer.parseInt(jsonObj.getString("errcode")) == 403) {
				lock.lock();
				oldTime = accessTokenTime;
				token = accessToken;
				lock.unlock();

				if (!doAuth(url, oldTime)) {
					return null;
				}
				jsonObj = doUrl0(method, url, data);
			}
		} catch (Exception e) {
			return null;
		}
		return jsonObj;
	}

	private JSONObject doUrl0(String method, String url, String data) {
		InputStream in = null;
		OutputStream out = null;
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
					new java.security.SecureRandom());
			URL console = new URL(url);
			HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
			conn.setRequestMethod(method);
			if(method.equals("post") && data.length() != 0){
				conn.getOutputStream().write(data.getBytes("utf-8"));
				conn.getOutputStream().flush();
				conn.getOutputStream().close();
			}
			conn.connect();
			InputStream is = conn.getInputStream();
			DataInputStream indata = new DataInputStream(is);
			StringBuilder sb = new StringBuilder();
			byte[] buf = new byte[1024];
			for (int count = indata.read(buf); count >= 0; count = indata.read(buf)) {
				sb.append(new String(buf, "UTF-8"), 0, count);
			}
			conn.disconnect();
			System.out.println("buf:" + sb);
			return new JSONObject(sb.toString());
		} catch (Exception e) {
			System.out.println("doUrl Exception:" + e.getMessage());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	private static class TrustAnyTrustManager implements X509TrustManager {
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	}

}
