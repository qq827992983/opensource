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
 * 处理根据文章ID查找文章信息的Servlet
 */
public class DetailArticleServlet extends HttpServlet {

	
	private static final long serialVersionUID = 4699358449436421995L;

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//中文处理
		response.setContentType("text/html;charset=gb2312");
		//解决字符非空
		String articleId = Validate.validStringNull(request
				.getParameter("articleId"));
		ArticleBo bo = new ArticleBo();//创建文章业务类对象
		//调用方法，通过文章ID得到文章信息
		Article article = bo.getArticleById(articleId);
		if (article == null) {
			//如果文章不存在
			PrintWriter out = response.getWriter();
			out
					.print("<script type='' language='javascript'>alert('查找失败，请重新操作。');history.go(-1);</script>");
			out.flush();
			out.close();
		} else {
			//查找到文章后，转发到显示文章详细页面
			request.setAttribute("detailArticle", article);
			request.getRequestDispatcher("detailArticle.jsp").forward(request, response);
		}

	}

}
