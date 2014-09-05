package ruijie.whistle.lightapp.request;

import org.dom4j.Element;

/**
* Note:文本类型请求报文<br>
*/
public class TextRequest extends Request {

	/**
	* Note:获取报文的context段 <br> <br>
	* 	public String getFromStudentNumber();
	* @return context段内容
	*/ 
	public String getContent() {
		return Doc.elementText("content");
	}

	/**
	* Note:构造方法 <br> <br>
	* 	public TextRequest(Element Doc);
	*/ 
	public TextRequest(Element Doc) {
		super(Doc);
	}
}