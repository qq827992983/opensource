package ruijie.whistle.lightapp.request;

import org.dom4j.Element;

/**
* Note:抽象类,HTTP请求报文<br>
*/ 
public abstract class Request {
	public String FromUsername = null;
	public String MsgId = null;
	public String ToUsername;
	public String CreateTime;
	public Element Doc;

	/**
	* Note:获取发送者学号 <br> <br>
	* 	public String getFromStudentNumber();
	* @return 返回发送者学号
	*/ 
	public String getMsgId() {
		return Doc.elementText("msgid");
	}

	/**
	* Note:获取消息类型 <br> <br>
	* 	public String getType();
	* @return 消息类型
	*/ 
	public String getType() {
		return Doc.elementText("type");
	}

	/**
	* Note:获取发送者学号 <br> <br>
	* 	public String getFromStudentNumber();
	* @return 返回发送者学号
	*/ 
	public String getFromStudentNumber() {
		return Doc.elementText("fromstudentnumber");
	}

	/**
	* Note:获取发送者用户名 <br> <br>
	* 	public String getFromUsername();
	* @return 返回发送者用户名
	*/ 
	public String getFromUsername() {
		return Doc.elementText("fromusername");
	}

	/**
	* Note:获取接收者学号 <br> <br>
	* 	public String getToUsername();
	* @return 返回接收者学号
	*/ 
	public String getToUsername() {
		return Doc.elementText("tousername");
	}

	/**
	* Note:获取创建时间 <br> <br>
	* 	public long getCreateTime();
	* @return 创建时间 
	*/ 
	public long getCreateTime() {
		return Long.parseLong(Doc.elementText("createtime"));
	}

	/**
	* Note:获取XML报文 <br> <br>
	* 	protected Element getDoc()
	* @return 返回XML报文
	*/ 
	protected Element getDoc() {
		return Doc;
	}

	/**
	* Note:构造方法 <br> <br>
	* 	public Request(Element Doc);
	*/ 
	public Request(Element Doc) {
		this.Doc = Doc;
	}
}
