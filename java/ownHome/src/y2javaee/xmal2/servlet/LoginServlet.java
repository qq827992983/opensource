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
 * 处理用户登录的Serlvet
 */
public class LoginServlet extends HttpServlet {

	
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//中文处理
		response.setContentType("text/html;charset=gb2312");
		//获取用户输入数据
		String userName=Validate.validStringNull(request.getParameter("userName"));
		String password =Validate.validStringNull( request.getParameter("password"));
		//创建User业务类对象
		UserBo userBo=new UserBo();
		//调用方法，验证当前用户是否存在
		Users user=userBo.validUser(userName, password);
		if(user!=null){
			//用户存在
			request.getSession().setAttribute("user", user);		
			request.getRequestDispatcher("SelectArticleServlet").forward(request, response);
		}else{
			//用户不存在
			PrintWriter out=response.getWriter();
			out.print("<script type='' language='javascript'>alert('用户名或密码错误，请重新输入。');history.go(-1);</script>");
			out.flush();
			out.close();
		}
	}

}
