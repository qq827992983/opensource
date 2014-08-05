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

		//中文处理
		response.setContentType("text/html;charset=gb2312");
		PrintWriter out = response.getWriter();
		//获取客户端输入数据
		String userName = Validate.validStringNull(request
				.getParameter("userName"));
		String password = Validate.validStringNull(request
				.getParameter("password"));
		String realName = Validate.validStringNull(request
				.getParameter("realName"));
		String sex=Validate.validStringNull(request.getParameter("sex"));
		//创建用户类对象，并封装数据
		Users user = new Users();
		user.setUserName(userName);
		user.setPassword(password);
		user.setRealName(realName);
		user.setSex(sex);
		//创建用户的业务类，并调用添加方法
		UserBo bo = new UserBo();
		if (bo.insertUser(user)>0) {			
			out
					.print("<script type='' language='javascript'>alert('注册成功，请登录。');location.href='index.jsp';</script>");		
		}else
			out
			.print("<script type='' language='javascript'>alert('注册失败，请重新注册。');history.go(-1);</script>");
		out.flush();
		out.close();

	}

}
