package y2javaee.xmal2.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * �����¼�û��˳���Servlet
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
		request.getSession().invalidate();//�����ǰ�û���ص�session����
		response.sendRedirect("SelectArticleServlet");
		
	}

}
