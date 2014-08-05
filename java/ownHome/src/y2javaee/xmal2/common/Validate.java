package y2javaee.xmal2.common;

public class Validate {
	/**
	 * 字符处理方法判断字符是否为null
	 * @param str
	 * @return str+""
	 */
	public static String validStringNull(String str) {
		return str == null ? "" : str;
	}
}
