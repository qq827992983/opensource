package y2javaee.xmal2.entity;

public class Article {
	private int articleId;
	private String title;
	private int type;
	private String content;
	private String writer;
	private String writeDate;
	/**
	 * @return articleId
	 */
	public int getArticleId() {
		return articleId;
	}
	/**
	 * @return content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @return writer
	 */
	public String getWriter() {
		return writer;
	}
	/**
	 * @param writer Ҫ���õ� writer
	 */
	public void setWriter(String writer) {
		this.writer = writer;
	}
	/**
	 * @return title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @return type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @return writeDate
	 */
	public String getWriteDate() {
		return writeDate;
	}
	/**
	 * @param articleId Ҫ���õ� articleId
	 */
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
	/**
	 * @param content Ҫ���õ� content
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @param title Ҫ���õ� title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @param type Ҫ���õ� type
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @param writeDate Ҫ���õ� writeDate
	 */
	public void setWriteDate(String writeDate) {
		this.writeDate = writeDate;
	}
	
}
