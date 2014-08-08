package y2javaee.xmal2.operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.jstl.sql.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import y2javaee.xmal2.common.DBConnection;
import y2javaee.xmal2.common.SQLCommandBean;
import y2javaee.xmal2.common.Test;
import y2javaee.xmal2.entity.Article;

public class ArticleBo {
	private static final Logger Log = LoggerFactory.getLogger(ArticleBo.class);
	
	private Connection conn;//���ڱ������ݿ����Ӷ���

	private PreparedStatement ps;//����ִ��SQL���

	private ResultSet rs;//���������ѯ�����

	private final int TOP = 3;//����ҳ����ʾ������
	
/**
 * �����������Ͳ������¶���
 * @param type
 * @return ���¶����б�
 */
	public List selectArticleByType(int type) {
		List list = new ArrayList();
		String sql = "select * from article where typeId=? order by writeDate limit " + TOP;
		try {
			conn = DBConnection.getConnectionForProperty();//ʹ��JNDI������ȡ���ݿ����Ӷ���
			ps = conn.prepareStatement(sql);
			
			ps.setInt(1, type);

			rs = ps.executeQuery();
			while (rs.next()) {
				//��װ����
				Article article = new Article();
				article.setArticleId(rs.getInt("articleId"));
				article.setTitle(rs.getString("title"));
				article.setType(type);
				article.setContent(rs.getString("content"));
				article.setWriteDate(rs.getString("writeDate"));

				list.add(article);//�����¶��󱣴浽List�б���
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeResultSet(rs);
			DBConnection.closeStatement(ps);
			DBConnection.closeConnection();
		}
		return list;
	}
/**
 * �����������ͺ��û����Ʋ�ѯ���¶���
 * @param type
 * @param userName
 * @return ���¶����б�
 */
	public List selectArticleByType(int type, String userName) {
		List list = new ArrayList();
		Log.debug("type:"+type + ",userName:"+userName);
		String sql = "select * from article where typeId=? and writer = ? order by writeDate limit "+ TOP;
		try {
			conn = DBConnection.getConnectionForProperty();
			ps = conn.prepareStatement(sql);
			
			ps.setInt(1, type);
			ps.setString(2, userName);

			rs = ps.executeQuery();
			while (rs.next()) {
				Article article = new Article();
				article.setArticleId(rs.getInt("articleId"));
				article.setTitle(rs.getString("title"));
				article.setType(type);
				article.setContent(rs.getString("content"));
				article.setWriteDate(rs.getString("writeDate"));

				list.add(article);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeResultSet(rs);
			DBConnection.closeStatement(ps);
			DBConnection.closeConnection();
		}
		return list;
	}

	/**
	 * �������µ����͡������������Ϣ������ģ����ѯ���¶���
	 * @param type
	 * @param title
	 * @param writer
	 * @return ���¶����б�
	 */
	public List searchArtcle(String type, String title, String writer) {
		List list = new ArrayList(); 
		//���ݴ���Ĳ����Ĳ�ͬ������ƴдSQL���
		System.out.println("type:"+type+",title:"+title+",writer:"+writer);
		StringBuffer sql = new StringBuffer(
				"select * from article where 1=1 ");
		if (type != null && !"".equals(type) && type != "0")
			sql.append(" and typeId=" + type);
		if (title != null && !"".equals(title))
			sql.append(" and title like '%" + title + "%'");
		if (writer != null && !"".equals(writer))
			sql.append(" and writer like '%" + writer + "%'");
		sql.append(" order by writeDate desc");
		
		System.out.println("sql:"+sql);
		try {
			conn = DBConnection.getConnectionForProperty();
			ps = conn.prepareStatement(sql.toString());

			rs = ps.executeQuery();
			while (rs.next()) {
				Article article = new Article();
				article.setArticleId(rs.getInt("articleId"));
				article.setTitle(rs.getString("title"));
				article.setType(rs.getInt("type"));
				article.setContent(rs.getString("content"));
				article.setContent(rs.getString("writer"));
				article.setWriteDate(rs.getString("writeDate"));

				list.add(article);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeResultSet(rs);
			DBConnection.closeStatement(ps);
			DBConnection.closeConnection();
		}
		return list;
	}

//	public Article getArticleById(String articleId) {
//		Article article = null;
//		String sql = "select * from article where articleId = ? ";
//		try {
//			conn = DBConnection.getConnectionForProperty();
//			ps = conn.prepareStatement(sql);
//
//			ps.setString(1, articleId);
//			rs = ps.executeQuery();
//			while (rs.next()) {
//				article = new Article();
//				article.setArticleId(rs.getInt("articleId"));
//				article.setTitle(rs.getString("title"));
//				article.setType(rs.getInt("type"));
//				article.setContent(rs.getString("content"));
//				article.setWriter(rs.getString("writer"));
//				article.setWriteDate(rs.getString("writeDate"));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			DBConnection.closeResultSet(rs);
//			DBConnection.closeStatement(ps);
//			DBConnection.closeConnection();
//		}
//		return article;
//	}

	
	/**
	 * ʹ��ͨ��DAO�����ʽ�������ݿ��������������ID����ѯ������Ϣ
	 * @param articleId ����ID
	 * @return Article ������Ϣ
	 */
	public Article getArticleById(String articleId) {

		Article article = null;
		String sql = "select * from article where articleId = ? ";
		try {
			SQLCommandBean bean = new SQLCommandBean();

			conn = DBConnection.getConnectionForProperty();
			bean.setConnection(conn);
			bean.setSql(sql);
			List param = new ArrayList();
			param.add(articleId);
			bean.setParamList(param);
			Result result = bean.executeQuery();

			if (result == null || result.getRowCount() == 0) {
				return null;
			} else {
				Map row = result.getRows()[0];
				article = new Article();
				article.setArticleId(((Integer) row.get("articleId"))
						.intValue());
				article.setTitle((String) row.get("title"));
				article.setType(((Integer) row.get("type")).intValue());
				article.setContent((String) row.get("content"));
				article.setWriter((String) row.get("writer"));
				article.setWriteDate((String) row.get("writeDate"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return article;
	}
}
