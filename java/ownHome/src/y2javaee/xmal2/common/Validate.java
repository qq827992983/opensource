package y2javaee.xmal2.common;

public class Validate {
	/**
	 * �ַ��������ж��ַ��Ƿ�Ϊnull
	 * @param str
	 * @return str+""
	 */
	public static String validStringNull(String str) {
		return str == null ? "" : str;
	}
}
