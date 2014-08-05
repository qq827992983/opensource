package y2javaee.xmal2.common;

public class DBMysqlException extends Exception {
	
	private static final long serialVersionUID = 8592427083086675651L;
	protected Throwable throwable;

	/**
	 * ���췽��
	 * 
	 * @param message
	 */
	public DBMysqlException(String message) {
		super(message);
		
	}

	/**
	 * ���췽��
	 * 
	 * @param message
	 * @param throwable
	 */
	public DBMysqlException(String message, Throwable throwable) {
		super(message);
		this.throwable = throwable;
	}

	/**
	 * ���صײ��쳣ԭ��
	 * 
	 * @return Throwable
	 */
	public Throwable getCause() {
		return throwable;
	}

}
