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
		//���Ĵ���
		request.setCharacterEncoding("gb2312");
		response.setContentType("text/html;charset=gb2312");
		//��ȡ�û���������
		String type=Validate.validStringNull(request.getParameter("type"));
		String title=Validate.validStringNull(request.getParameter("title"));
		String writer= Validate.validStringNull(request.getParameter("writer"));
		//��������ҵ�������
		ArticleBo bo=new ArticleBo();
		//�����������������ҷ��������������б�
		List list=bo.searchArtcle(type, title, writer);
		//���б��浽request��Χ��
		request.setAttribute("searchList", list);
		//ת������ʾ�����б��ҳ��
		request.getRequestDispatcher("showArticle.jsp").forward(request, response);
		
	}

}
