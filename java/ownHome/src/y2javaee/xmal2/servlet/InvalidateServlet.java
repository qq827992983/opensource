package y2javaee.xmal2.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 处理登录用户退出的Servlet
 */
public class InvalidateServlet extends HttpServlet {


	private static final long serialVersionUID = -7054879393227280483L;


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=gb2312");
		request.getSession().invalidate();//清除当前用户相关的session对象
		response.sendRedirect("SelectArticleServlet");
		
	}

}
