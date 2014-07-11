package ruijie.whistle.lightapp.request;

import org.dom4j.Element;

/**
* Note:链接请求报文<br>
*/
public class LinkRequest extends Request {
	/**
	* Note:获取链接标题 <br> <br>
	* 	public String getTitle();
	* @return title段内容
	*/ 
	public String getTitle() {
		return Doc.elementText("title");
	}

	/**
	* Note:获取链接描述信息 <br> <br>
	* 	public String getDescription();
	* @return description段内容
	*/ 
	public String getDescription() {
		return Doc.elementText("description");
	}

	/**
	* Note:获取链接URL <br> <br>
	* 	public String getUrl();
	* @return url段内容
	*/ 
	public String getUrl() {
		return Doc.elementText("url");
	}

	/**
	* Note:构造方法 <br> <br>
	* 	public LinkRequest(Element Doc);
	*/ 
	public LinkRequest(Element Doc) {
		super(Doc);
	}
}
