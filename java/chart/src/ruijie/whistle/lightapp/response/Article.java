package ruijie.whistle.lightapp.response;

/**
* Note:图文类<br>
*/
public class Article {
	private String Title;
	private String Description;
	private String PicUrl;
	private String Url;

	/**
	* Note:获取标题 <br> <br>
	* 	public String getTitle() ;
	* @return Title内容
	*/ 
	public String getTitle() {
		return Title;
	}

	/**
	* Note:设置标题 <br> <br>
	* 	public void setTitle(String title) ;
	* 
	* @param title 标题内容
	* @return 无
	*/ 
	public void setTitle(String title) {
		Title = title;
	}

	/**
	* Note:获取描述信息 <br> <br>
	* 	public String getDescription();
	* @return Title内容
	*/ 
	public String getDescription() {
		return Description;
	}

	/**
	* Note:设置描述信息 <br> <br>
	* 	public void setDescription(String description);
	* 
	* @param description 描述信息
	* @return 无
	*/ 
	public void setDescription(String description) {
		Description = description;
	}

	/**
	* Note:获取图片URL <br> <br>
	* 	public String getPicUrl() ;
	* @return 图片URL
	*/ 
	public String getPicUrl() {
		return PicUrl;
	}

	/**
	* Note:设置图片链接 <br> <br>
	* 	public void setPicUrl(String picUrl)
	* 
	* @param picUrl 图片链接URL
	* @return 无
	*/ 
	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}

	/**
	* Note:获取跳转URL <br> <br>
	* 	public String getUrl() ;
	* @return 跳转url
	*/ 
	public String getUrl() {
		return Url;
	}

	/**
	* Note:设置图片跳转链接 <br> <br>
	* 	public void setUrl(String url)
	* 
	* @param url 跳转URL
	* @return 无
	*/ 
	public void setUrl(String url) {
		Url = url;
	}

	/**
	* Note:构造方法 <br> <br>
	* 	public Article() ;
	*/ 
	public Article() {
	}

	/**
	* Note:构造方法 <br> <br>
	* 	public Article(String title, String description, String picUrl, String url);
	*/ 
	public Article(String title, String description, String picUrl, String url) {
		Title = title;
		Description = description;
		PicUrl = picUrl;
		Url = url;
	}
}
