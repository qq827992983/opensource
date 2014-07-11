package ruijie.whistle.lightapp.response;

import java.util.ArrayList;
import java.util.List;
import ruijie.whistle.lightapp.request.Request;

import org.dom4j.Element;

/**
* Note:图文响应报文<br>
*/
public class ArticleResponse extends Response {
	private List<Article> Articles;
	
	/**
	* Note:获取图文列表 <br> <br>
	* 	public List<Article> getArticles();
	* 
	* @return 无
	*/ 
	public List<Article> getArticles() {
		return Articles;
	}

	/**
	* Note:设置图片列表 <br> <br>
	* 	public void setArticles(List<Article> articles);
	* 
	* @param articles 图文列表
	* @return 无
	*/ 
	public void setArticles(List<Article> articles) {
		Articles = articles;
	}

	/**
	* Note:构造方法 <br> <br>
	* 	public ArticleResponse(Request request);
	* 
	* @param request 请求对象
	*/ 
	public ArticleResponse(Request request) {
		super(request);
		Articles = new ArrayList<Article>();
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
		appendNode(doc, "msgtype", "news");
		appendNode(doc, "articlecount", Articles.size());

		Element node = appendNode(doc, "articles");

		for (int i = 0; i < Articles.size(); ++i) {
			Element item = appendNode(node, "item");
			appendNode(item, "title", Articles.get(i).getTitle());
			appendNode(item, "description", Articles.get(i).getDescription());
			appendNode(item, "picurl", Articles.get(i).getPicUrl());
			appendNode(item, "url", Articles.get(i).getUrl());
		}
	}

	
}
