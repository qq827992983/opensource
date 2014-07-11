package ruijie.whistle.lightapp.request;

import org.dom4j.Element;

/**
* Note:图片请求报文<br>
*/
public class ImageRequest extends Request {
	/**
	* Note:获取图片的URL <br> <br>
	* 	public String getPicUrl();
	* @return picurl段内容
	*/ 
	public String getPicUrl() {
		return Doc.elementText("picurl");
	}

	/**
	* Note:构造方法 <br> <br>
	* 	public ImageRequest(Element Doc);
	*/ 
	public ImageRequest(Element Doc) {
		super(Doc);
	}
}