package ruijie.whistle.lightapp.demo;

import javax.servlet.http.HttpServletResponse;

import ruijie.whistle.lightapp.request.EventRequest;
import ruijie.whistle.lightapp.request.HelloRequest;
import ruijie.whistle.lightapp.request.ImageRequest;
import ruijie.whistle.lightapp.request.LinkRequest;
import ruijie.whistle.lightapp.request.LocationRequest;
import ruijie.whistle.lightapp.request.Request;
import ruijie.whistle.lightapp.request.TextRequest;
import ruijie.whistle.lightapp.request.UnknowRequest;
import ruijie.whistle.lightapp.request.VoiceRequest;
import ruijie.whistle.lightapp.response.Article;
import ruijie.whistle.lightapp.response.ArticleResponse;
import ruijie.whistle.lightapp.response.MusicResponse;
import ruijie.whistle.lightapp.response.Response;
import ruijie.whistle.lightapp.response.TextResponse;
import ruijie.whistle.lightapp.sdk.WhistleChart;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONException;
import org.json.JSONObject;

public class MyLightApp extends WhistleChart{

	public MyLightApp() {
		//三个参数分别为appID,appSecret,appToken,由用户自己填写
		super("w25dogobcpn3w9yqwxwb5a","mjljndfhodzizmm3ndu5odhiywflnzg2odq1zdm0zjhky2yxymvhzg","123456");
	}
	
	protected Response onRecvHelloMessage(HttpServletResponse response, HelloRequest request){
		TextResponse helloResponse = new TextResponse(request);
		String fromStudentNumber = request.getFromStudentNumber();
		JSONObject jGetUserInfo = doUrl("GET", "https://172.16.56.102:8443/api/user/get_info?student_number=" + fromStudentNumber, null);
		String userName = "";
		String studentNumber = "";
		String sex = "";
		String organization = "";
		
		try{
			JSONObject userInfo = jGetUserInfo.getJSONObject("data");
			userName = userInfo.getString("name");
			studentNumber = userInfo.getString("student_number");		
			if (userInfo.getString("sex").equals("boy"))
				sex = "男";
			else
				sex = "女";
			organization = userInfo.getString("organization");
		}catch(JSONException e){
        	System.out.println("Json exception:" + e);
        }finally{
        	String content = "尊敬的:" + userName +  ",您好!这是Hello报文,以下是您的基本信息:"+"姓名:"+userName+",性别:"+sex+",学号:" + studentNumber + ",组织结构:" + organization + "\n" + "输入1返回图文消息, 输入2返回音乐消息,输入3返回文本信息";
        	helloResponse.setContent(content);
        }
		
		return helloResponse;
	}
	
	protected Response onRecvTextMessage(HttpServletResponse response, TextRequest request){
		Response retResponse;
		String context = request.getContent();
		switch(context){
		case "1":
			ArticleResponse article = new ArticleResponse(request);
			article.getArticles().add(new Article("这是图文消息", "不要点我", "http://www.weishao.com.cn/upload/image/1385434253.png",""));
			article.getArticles().add(new Article("第一点", "点我跳到微哨", "http://www.weishao.com.cn/upload/image/1385434408.png", "http://www.weishao.com.cn"));
			article.getArticles().add(new Article("第二点", "点我跳到百度", "http://www.weishao.com.cn/upload/image/1385693782.png", "http://www.baidu.com"));
			retResponse = article;			
            break;
		case "2":
			 MusicResponse music = new MusicResponse(request);
			 music.setTitle("忐忑");
			 music.setDescription("这是一首奇妙的乐曲");
			 music.setMusicUrl("http://www.baidu.com/mp3/1231233.mp3");
			 music.setHQMusicUrl("http://www.baidu.com/mp3/hq1231233.mp3");
             retResponse = music;	
			break;
		case "3":
			TextResponse text = new TextResponse(request);
			String textContent = "您好,按键3的功能就留给您实现吧!";
			text.setContent(textContent);
            retResponse = text;	
			break;
		default:
			TextResponse help = new TextResponse(request);
			String helpContent = "这是文本消息, 输入1返回图文消息, 输入2返回音乐消息,输入3返回文本信息, 输入其他内容, 显示本消息";
			help.setContent(helpContent);
            retResponse = help;
			break;
		}
		
		return retResponse;
	}
	protected Response onRecvImageMessage(HttpServletResponse response, ImageRequest request){
		TextResponse textResponse = new TextResponse(request);
		String content = "这是图片消息";
		textResponse.setContent(content);
		return textResponse;
	}
	protected Response onRecvVoiceMessage(HttpServletResponse response, VoiceRequest request){
		TextResponse textResponse = new TextResponse(request);
		String content = "这是音乐消息";
		textResponse.setContent(content);
		return textResponse;
	}
	protected Response onRecvEventMessage(HttpServletResponse response, EventRequest request){
		TextResponse textResponse = new TextResponse(request);
		String content = "这是事件消息";
		textResponse.setContent(content);
		return textResponse;
	}
	protected Response onRecvLocationMessage(HttpServletResponse response, LocationRequest request){
		TextResponse textResponse = new TextResponse(request);
		String content = "这是位置消息";
		textResponse.setContent(content);
		return textResponse;
	}
	protected Response onRecvLinkMessage(HttpServletResponse response, LinkRequest request){
		TextResponse textResponse = new TextResponse(request);
		String content = "这是链接消息";
		textResponse.setContent(content);
		return textResponse;
	}
	protected Response onRecvUnknowMessage(HttpServletResponse response, UnknowRequest request, String context){
		TextResponse textResponse = new TextResponse(request);
		String content = "这是未知消息";
		textResponse.setContent(content);
		return textResponse;
	}
}
