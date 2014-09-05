package y2javaee.xmal2.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import y2javaee.xmal2.entity.Users;
import y2javaee.xmal2.operation.ArticleBo;

/**
 * 处理查询或者按照类型查询文章信息的Servlet
 */
public class SelectArticleServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 中文处理
		response.setContentType("text/html;charset=gb2312");
		// 获取session对象，并从session中得到user对象
		HttpSession session = request.getSession();
		Users user = (Users) session.getAttribute("user");
		// 创建文章业务类对象
		ArticleBo bo = new ArticleBo();
		List list1 = null;// 用于保存JAVA类的文章
		List list2 = null;// 用于保存#.NET类的文章
		List list3 = null;// 用于保存心情日记类的文章
		List list4 = null;// 用于保存励志文章类的文章

		if (session == null || user == null || "".equals(user.getUserName())) {
			// 调用方法获取各类型的文章信息
			list1 = bo.selectArticleByType(1);
			list2 = bo.selectArticleByType(2);
			list3 = bo.selectArticleByType(3);
			list4 = bo.selectArticleByType(4);
			// 把信息列表保存在session范围中
			session.setAttribute("list1", list1);
			session.setAttribute("list2", list2);
			session.setAttribute("list3", list3);
			session.setAttribute("list4", list4);
			// 重定向到首页
			response.sendRedirect("index.jsp");
		} else {
			// 调用方法获取各类型的文章信息
			list1 = bo.selectArticleByType(1, user.getUserName());
			list2 = bo.selectArticleByType(2, user.getUserName());
			list3 = bo.selectArticleByType(3, user.getUserName());
			list4 = bo.selectArticleByType(4, user.getUserName());
			// 把信息列表保存在session范围中
			session.setAttribute("list1", list1);
			session.setAttribute("list2", list2);
			session.setAttribute("list3", list3);
			session.setAttribute("list4", list4);
			// 重定向到首页
			response.sendRedirect("index.jsp");
		}

	}

}
