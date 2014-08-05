package y2javaee.xmal2.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import y2javaee.xmal2.operation.ArticleBo;
import y2javaee.xmal2.common.Validate;;

public class SearchServlet extends HttpServlet {

	
	
	private static final long serialVersionUID = 1L;


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//中文处理
		request.setCharacterEncoding("gb2312");
		response.setContentType("text/html;charset=gb2312");
		//获取用户输入数据
		String type=Validate.validStringNull(request.getParameter("type"));
		String title=Validate.validStringNull(request.getParameter("title"));
		String writer= Validate.validStringNull(request.getParameter("writer"));
		//创建文章业务类对象
		ArticleBo bo=new ArticleBo();
		//调用搜索方法，查找符合条件的文章列表
		List list=bo.searchArtcle(type, title, writer);
		//将列表保存到request范围内
		request.setAttribute("searchList", list);
		//转发到显示文章列表的页面
		request.getRequestDispatcher("showArticle.jsp").forward(request, response);
		
	}

}
