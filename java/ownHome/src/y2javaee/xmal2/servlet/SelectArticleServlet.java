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
 * �����ѯ���߰������Ͳ�ѯ������Ϣ��Servlet
 */
public class SelectArticleServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// ���Ĵ���
		response.setContentType("text/html;charset=gb2312");
		// ��ȡsession���󣬲���session�еõ�user����
		HttpSession session = request.getSession();
		Users user = (Users) session.getAttribute("user");
		// ��������ҵ�������
		ArticleBo bo = new ArticleBo();
		List list1 = null;// ���ڱ���JAVA�������
		List list2 = null;// ���ڱ���#.NET�������
		List list3 = null;// ���ڱ��������ռ��������
		List list4 = null;// ���ڱ�����־�����������

		if (session == null || user == null || "".equals(user.getUserName())) {
			// ���÷�����ȡ�����͵�������Ϣ
			list1 = bo.selectArticleByType(1);
			list2 = bo.selectArticleByType(2);
			list3 = bo.selectArticleByType(3);
			list4 = bo.selectArticleByType(4);
			// ����Ϣ�б�����session��Χ��
			session.setAttribute("list1", list1);
			session.setAttribute("list2", list2);
			session.setAttribute("list3", list3);
			session.setAttribute("list4", list4);
			// �ض�����ҳ
			response.sendRedirect("index.jsp");
		} else {
			// ���÷�����ȡ�����͵�������Ϣ
			list1 = bo.selectArticleByType(1, user.getUserName());
			list2 = bo.selectArticleByType(2, user.getUserName());
			list3 = bo.selectArticleByType(3, user.getUserName());
			list4 = bo.selectArticleByType(4, user.getUserName());
			// ����Ϣ�б�����session��Χ��
			session.setAttribute("list1", list1);
			session.setAttribute("list2", list2);
			session.setAttribute("list3", list3);
			session.setAttribute("list4", list4);
			// �ض�����ҳ
			response.sendRedirect("index.jsp");
		}

	}

}
