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
import y2javaee.xmal2.operation.IBookService;
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
		request.setCharacterEncoding("gb2312");
		response.setContentType("text/html;charset=gb2312");
		//��ȡ�û����������
		String name=Validate.validStringNull(request.getParameter("name"));
		String sex=Validate.validStringNull(request.getParameter("sex"));
		String phone=Validate.validStringNull(request.getParameter("phone"));
		String address=Validate.validStringNull(request.getParameter("address"));
		String mobilePhone=Validate.validStringNull(request.getParameter("mobilePhone"));
		String company=Validate.validStringNull(request.getParameter("company"));
		String comPhone=Validate.validStringNull(request.getParameter("comPhone"));
		String comAddress=Validate.validStringNull(request.getParameter("comAddress"));
		String relation=Validate.validStringNull(request.getParameter("relation"));
		
		
		//���������Ԫ����
		Service serviceModel = new ObjectServiceFactory().create(IBookService.class);
		System.out.println("�����˷����ģ��.");

		// ��������Ĵ���
		XFire xfire = XFireFactory.newInstance().getXFire();
		XFireProxyFactory factory = new XFireProxyFactory(xfire);

		String serviceUrl = "http://localhost:8080/addressBook/services/AddBookService";// ����ĵ�ַ

		IBookService client = null;
		try {
			client = (IBookService) factory.create(serviceModel, serviceUrl);
		} catch (MalformedURLException e) {
			System.out.println("�ͻ��˵����쳣: " + e.toString());
		}

		// ���÷���
		int serviceResponse = 0;
		try {
			serviceResponse = client.addBookService(name,sex,phone,address,mobilePhone,company,comPhone,comAddress,relation, 1);
			System.out.println("WsClient.callWebService(): status="
					+ serviceResponse);
		} catch (Exception e) {
			System.out.println("WsClient.callWebService(): EXCEPTION: "
					+ e.toString());
		}
		PrintWriter out=response.getWriter();
		if (serviceResponse == 1) {
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
