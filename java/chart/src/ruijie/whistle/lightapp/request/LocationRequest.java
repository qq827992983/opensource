package ruijie.whistle.lightapp.request;

import org.dom4j.Element;

/**
* Note:位置类型请求报文<br>
*/
public class LocationRequest extends Request {
	/**
	* Note:获取地理位置标签 <br> <br>
	* 	public String getLabel();
	* @return label段内容
	*/ 
	public String getLabel() {
		return Doc.elementText("label");
	}

	/**
	* Note:获取比例 <br> <br>
	* 	public String getScale() ;
	* @return scale段内容
	*/ 
	public String getScale() {
		return Doc.elementText("scale");

	}

	/**
	* Note:获取地理位置经度 <br> <br>
	* 	public String getLocation_X();
	* @return location_x段内容
	*/ 
	public String getLocation_X() {
		return Doc.elementText("location_x");
	}

	/**
	* Note:获取地理位置纬度 <br> <br>
	* 	public String getLocation_Y();
	* @return location_y段内容
	*/ 
	public String getLocation_Y() {
		return Doc.elementText("location_y");
	}

	/**
	* Note:构造方法 <br> <br>
	* 	public LocationRequest(Element Doc);
	*/ 
	public LocationRequest(Element Doc) {
		super(Doc);
	}
}