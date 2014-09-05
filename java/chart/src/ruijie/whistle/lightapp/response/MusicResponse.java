package ruijie.whistle.lightapp.response;

import ruijie.whistle.lightapp.request.Request;

import org.dom4j.Element;

/**
* Note:音频响应报文<br>
*/
public class MusicResponse extends Response {

	private String Title;
	private String Description;
	private String MusicUrl;
	private String HQMusicUrl;
	
	/**
	* Note:构造方法 <br> <br>
	* 	public MusicResponse(Request request);
	* 
	* @param request 请求对象
	*/ 
	public MusicResponse(Request request) {
		super(request);
	}

	/**
	* Note:获取标题 <br> <br>
	* 	public String getTitle();
	* 
	* @return 标题内容
	*/ 
	public String getTitle() {
		return Title;
	}

	/**
	* Note:设置标题 <br> <br>
	* 	public void setTitle(String title);
	* 
	* @param title 标题内容
	* @return 无
	*/ 
	public void setTitle(String title) {
		Title = title;
	}

	/**
	* Note:获取描述信息 <br> <br>
	* 	public void setArticles(List<Article> articles);
	* 
	* @return 描述信息
	*/ 
	public String getDescription() {
		return Description;
	}

	/**
	* Note:设置描述信息 <br> <br>
	* 	public void setDescription(String description);
	* 
	* @param description 描述信息内容
	* @return 无
	*/ 
	public void setDescription(String description) {
		Description = description;
	}

	/**
	* Note:获取音频链接 <br> <br>
	* 	public String getMusicUrl();
	* 
	* @return 音频链接
	*/ 
	public String getMusicUrl() {
		return MusicUrl;
	}

	/**
	* Note:设置音频链接 <br> <br>
	* 	public void setMusicUrl(String musicUrl) ;
	* 
	* @param musicUrl 音频链接
	* @return 无
	*/ 
	public void setMusicUrl(String musicUrl) {
		MusicUrl = musicUrl;
	}

	/**
	* Note:获取高品质音频链接 <br> <br>
	* 	public String getHQMusicUrl();
	* 
	* @return 高品质音频URL
	*/ 
	public String getHQMusicUrl() {
		return HQMusicUrl;
	}

	/**
	* Note:设置音频链接 <br> <br>
	* 	public void setHQMusicUrl(String hQMusicUrl);
	* 
	* @param hQMusicUrl 音频链接
	* @return 无
	*/ 
	public void setHQMusicUrl(String hQMusicUrl) {
		HQMusicUrl = hQMusicUrl;
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
		appendNode(doc, "msgtype", "music");

		Element node = appendNode(doc, "music");
		appendNode(node, "title", Title);
		appendNode(node, "description", Description);
		appendNode(node, "musicurl", MusicUrl);
		appendNode(node, "hqmusicurl", HQMusicUrl);

	}
}