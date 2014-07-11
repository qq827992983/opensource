package ruijie.whistle.lightapp.response;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import ruijie.whistle.lightapp.request.Request;

import org.dom4j.Element;

/**
* Note:抽象类,响应报文
*/
public abstract class Response {
	private Request request;
	private String fromUserName;
	private String type;
	private String toUserName;
	private long createTime;
	
	/**
	* Note:获取发送者学号 <br> <br>
	* 	public String getFromUserName();
	* 
	* @return 发送者学号
	*/ 
	public String getFromUserName() {
		return fromUserName;
	}

	/**
	* Note:获取响应类型 <br> <br>
	* 	public String getType();
	* 
	* @return 响应类型
	*/ 
	public String getType() {
		return type;
	}

	/**
	* Note:获取接收者学号<br> <br>
	* 	public String getToUserName();
	* 
	* @return 接收者学号
	*/ 
	public String getToUserName() {
		return toUserName;
	}

	/**
	* Note:获取创建时间 <br> <br>
	* 	public long getCreateTime();
	* 
	* @return 创建时间
	*/ 
	public long getCreateTime() {
		return createTime;
	}

	/**
	* Note:设置发送者学号<br> <br>
	* 	protected void setFromUserName(String fromUserName);
	* 
	* @param fromUserName 发送者学号
	* @return 无
	*/ 
	protected void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	/**
	* Note:设置响应类型 <br> <br>
	* 	protected void setType(String type);
	* 
	* @param type 标题内容
	* @return 无
	*/ 
	protected void setType(String type) {
		this.type = type;
	}

	/**
	* Note:设置接收者学号 <br> <br>
	* 	protected void setToUserName(String toUserName);
	* 
	* @param toUserName 接收者学号
	* @return 无
	*/ 
	protected void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	/**
	* Note:设置创建时间 <br> <br>
	* 	protected void setCreateTime(long createTime);
	* 
	* @param createTime 创建时间
	* @return 无
	*/ 
	protected void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	/**
	* Note:构造方法 <br> <br>
	* 	public Response(Request request);
	*/ 
	public Response(Request request) {
		this.request = request;
	}

	/**
	* Note:生成XML编码 <br> <br>
	* 	protected void encode(Element doc)
	* 
	* @param doc xml编码对象
	* @return 无
	*/ 
	protected void encode(Element doc) {
		appendNode(doc, "msgid", request.MsgId);
		appendNode(doc, "fromusername", request.FromUsername);
		appendNode(doc, "tousername", request.ToUsername);
		appendNode(doc, "createtime", request.CreateTime);

	}

	/**
	* Note:增加XML节点 ,并设置String类型值<br> <br>
	* 	protected Element appendNode(Element parent, String name, String cdata);
	* 
	* @param parent 父节点
	* @param name 新增节点名称
	* @param cdata cdata类型数据
	* @return 新的XML文档数据
	*/ 
	protected Element appendNode(Element parent, String name, String cdata) {
		Element node = parent.addElement(name).addCDATA(cdata);
		return node;
	}

	/**
	* Note:增加XML节点 <br> <br>
	* 	protected Element appendNode(Element parent, String name);
	* 
	* @param parent 父节点
	* @param name 新增节点名称
	* @return 新的XML文档数据
	*/ 
	protected Element appendNode(Element parent, String name) {
		Element node = parent.addElement(name);
		return node;
	}

	/**
	* Note:增加XML节点 ,并设置long类型值<br> <br>
	* 	protected Element appendNode(Element parent, String name, long cdata);
	* 
	* @param parent 父节点
	* @param name 新增节点名称
	* @param cdata cdata类型数据
	* @return 新的XML文档数据
	*/ 
	protected Element appendNode(Element parent, String name, long cdata) {
		parent.addElement(name).addCDATA(Long.toString(cdata));
		return parent;
	}
	
	/**
	* Note:将XML报文写入响应PrintWriter<br> <br>
	* 	public void writeTo(PrintWriter writer);
	* 
	* @param writer 响应输出对象
	* @return 无
	*/ 
	public void writeTo(PrintWriter writer){
		Element doc = org.dom4j.DocumentFactory.getInstance().createElement("xml");
		encode(doc);
		writer.println(doc.asXML());
	}
}
