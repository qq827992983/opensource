package y2javaee.xmal2.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.xfire.XFire;
import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;


import y2javaee.xmal2.common.Validate;
import y2javaee.xmal2.entity.Book;
import y2javaee.xmal2.operation.BookBo;
import y2javaee.xmal2.operation.UserBo;
/**
 * �������ͨѶ¼��Ϣ��Servlet
 */
public class AddBookServlet extends HttpServlet {

	
	private static final long serialVersionUID = 1L;


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//�������ĵ�����
		System.out.println("��������鼮��Post������");
		request.setCharacterEncoding("gb2312");
		response.setContentType("text/html;charset=gb2312");
		//��ȡ�û����������
		Book book = new Book();
		String name=Validate.validStringNull(request.getParameter("name"));
		book.setName(name);
		String sex=Validate.validStringNull(request.getParameter("sex"));
		book.setSex(sex);
		String phone=Validate.validStringNull(request.getParameter("phone"));
		book.setPhone(phone);
		String address=Validate.validStringNull(request.getParameter("address"));
		book.setAddress(address);
		String mobilePhone=Validate.validStringNull(request.getParameter("mobilePhone"));
		book.setMobilePhone(mobilePhone);
		String company=Validate.validStringNull(request.getParameter("company"));
		book.setCompany(company);
		String comPhone=Validate.validStringNull(request.getParameter("comPhone"));
		book.setComPhone(comPhone);
		String comAddress=Validate.validStringNull(request.getParameter("comAddress"));
		book.setComAddress(comAddress);
		String relation=Validate.validStringNull(request.getParameter("relation"));
		book.setRelation(relation);
		int ret = 0;
		
		BookBo bo = new BookBo();
		ret = bo.insertBook(book);
		PrintWriter out=response.getWriter();
		if (ret == 1) {
			//��ӳɹ�
			out.print("<script type='' language='javascript'>alert('��ӳɹ���');location.href='addBook.jsp';</script>");
			
		} else {
			//���ʧ��
			out.print("<script type='' language='javascript'>alert('���ʧ�ܡ�');history.go(-1);</script>");
		}
		out.flush();
		out.close();
		
	}

}
