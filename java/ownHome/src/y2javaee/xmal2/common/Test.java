package y2javaee.xmal2.common;

import java.sql.Connection;
import java.sql.DriverManager;

public class Test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ownhome", "root", "talk2her");
	}

}
