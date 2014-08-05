package y2javaee.xmal2.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import y2javaee.xmal2.operation.UserBo;
import y2javaee.xmal2.common.Validate;
import y2javaee.xmal2.entity.Users;
/**
 * �����û���¼��Serlvet
 */
public class LoginServlet extends HttpServlet {

	
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//���Ĵ���
		response.setContentType("text/html;charset=gb2312");
		//��ȡ�û���������
		String userName=Validate.validStringNull(request.getParameter("userName"));
		String password =Validate.validStringNull( request.getParameter("password"));
		//����Userҵ�������
		UserBo userBo=new UserBo();
		//���÷�������֤��ǰ�û��Ƿ����
		Users user=userBo.validUser(userName, password);
		if(user!=null){
			//�û�����
			request.getSession().setAttribute("user", user);		
			request.getRequestDispatcher("SelectArticleServlet").forward(request, response);
		}else{
			//�û�������
			PrintWriter out=response.getWriter();
			out.print("<script type='' language='javascript'>alert('�û���������������������롣');history.go(-1);</script>");
			out.flush();
			out.close();
		}
	}

}
