package ruijie.whistle.lightapp.response;

import ruijie.whistle.lightapp.request.Request;

import org.dom4j.Element;

/**
* Note:文本响应报文<br>
*/
public class TextResponse extends Response {
	private String Content;
	
	/**
	* Note:构造方法 <br> <br>
	* 	public TextResponse(Request request);
	* 
	* @param request 请求对象
	*/ 
	public TextResponse(Request request) {
		super(request);
	}

	/**
	* Note:获取文本 <br> <br>
	* 	public String getContent();
	* 
	* @return 文本内容
	*/ 
	public String getContent() {
		return Content;
	}

	/**
	* Note:设置文本 <br> <br>
	* 	public void setTitle(String title);
	* 
	* @param title 文本内容
	* @return 无
	*/ 
	public void setContent(String content) {
		Content = content;
	}

	/**
	* Note:生成XML编码 <br> <br>
	* 	protected void encode(Element doc)
	* 
	* @param doc xml编码对象
	* @return 无
	*/ 
	protected void encode(Element doc) {
		super.encode(doc);
		appendNode(doc, "msgtype", "text");
		appendNode(doc, "content", Content);

	}
}