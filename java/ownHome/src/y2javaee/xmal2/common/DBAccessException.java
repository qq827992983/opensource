package y2javaee.xmal2.common;

public class DBAccessException extends Exception {
	
	private static final long serialVersionUID = 8592427083086675651L;
	protected Throwable throwable;

	/**
	 * 构造方法
	 * 
	 * @param message
	 */
	public DBAccessException(String message) {
		super(message);
		
	}

	/**
	 * 构造方法
	 * 
	 * @param message
	 * @param throwable
	 */
	public DBAccessException(String message, Throwable throwable) {
		super(message);
		this.throwable = throwable;
	}

	/**
	 * 返回底层异常原因
	 * 
	 * @return Throwable
	 */
	public Throwable getCause() {
		return throwable;
	}

}
