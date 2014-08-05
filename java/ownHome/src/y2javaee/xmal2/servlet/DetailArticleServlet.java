package y2javaee.xmal2.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import y2javaee.xmal2.common.Validate;
import y2javaee.xmal2.entity.Article;
import y2javaee.xmal2.operation.ArticleBo;
/**
 * �����������ID����������Ϣ��Servlet
 */
public class DetailArticleServlet extends HttpServlet {

	
	private static final long serialVersionUID = 4699358449436421995L;

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//���Ĵ���
		response.setContentType("text/html;charset=gb2312");
		//����ַ��ǿ�
		String articleId = Validate.validStringNull(request
				.getParameter("articleId"));
		ArticleBo bo = new ArticleBo();//��������ҵ�������
		//���÷�����ͨ������ID�õ�������Ϣ
		Article article = bo.getArticleById(articleId);
		if (article == null) {
			//������²�����
			PrintWriter out = response.getWriter();
			out
					.print("<script type='' language='javascript'>alert('����ʧ�ܣ������²�����');history.go(-1);</script>");
			out.flush();
			out.close();
		} else {
			//���ҵ����º�ת������ʾ������ϸҳ��
			request.setAttribute("detailArticle", article);
			request.getRequestDispatcher("detailArticle.jsp").forward(request, response);
		}

	}

}
