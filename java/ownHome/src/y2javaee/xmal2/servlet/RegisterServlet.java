package y2javaee.xmal2.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import y2javaee.xmal2.common.Validate;
import y2javaee.xmal2.entity.Users;
import y2javaee.xmal2.operation.UserBo;

public class RegisterServlet extends HttpServlet {

	
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//���Ĵ���
		response.setContentType("text/html;charset=gb2312");
		PrintWriter out = response.getWriter();
		//��ȡ�ͻ�����������
		String userName = Validate.validStringNull(request
				.getParameter("userName"));
		String password = Validate.validStringNull(request
				.getParameter("password"));
		String realName = Validate.validStringNull(request
				.getParameter("realName"));
		String sex=Validate.validStringNull(request.getParameter("sex"));
		//�����û�����󣬲���װ����
		Users user = new Users();
		user.setUserName(userName);
		user.setPassword(password);
		user.setRealName(realName);
		user.setSex(sex);
		//�����û���ҵ���࣬��������ӷ���
		UserBo bo = new UserBo();
		if (bo.insertUser(user)>0) {			
			out
					.print("<script type='' language='javascript'>alert('ע��ɹ������¼��');location.href='index.jsp';</script>");		
		}else
			out
			.print("<script type='' language='javascript'>alert('ע��ʧ�ܣ�������ע�ᡣ');history.go(-1);</script>");
		out.flush();
		out.close();

	}

}
