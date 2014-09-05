package ruijie.whistle.lightapp.request;

import org.dom4j.Element;

/**
* Note:事件请求报文<br>
*/
public class EventRequest extends Request {
	/**
	* Note:获取事件 <br> <br>
	* 	public String getEvent();
	* @return event段内容
	*/ 
	public String getEvent() {
		return Doc.elementText("event");

	}

	/**
	* Note:获取事件的Key <br> <br>
	* 	public String getEventKey();
	* @return eventKey段内容
	*/ 
	public String getEventKey() {
		return Doc.elementText("eventKey");
	}

	/**
	* Note:构造方法 <br> <br>
	* 	public EventRequest(Element Doc);
	*/ 
	public EventRequest(Element Doc) {
		super(Doc);
	}
}

