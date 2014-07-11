package ruijie.whistle.lightapp.request;

import org.dom4j.Element;

/**
* Note:Hello报文<br>
*/
public class HelloRequest extends Request {
	/**
	* Note:获取文本内容 <br> <br>
	* 	public String getContent();
	* @return content段内容
	*/ 
	public String getContent() {
		return Doc.elementText("content");
	}

	/**
	* Note:构造方法 <br> <br>
	* 	public HelloRequest(Element Doc);
	*/ 
	public HelloRequest(Element Doc) {
		super(Doc);
	}
}