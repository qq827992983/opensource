package y2javaee.xmal2.entity;

public class Users {
	private int userId;

	private String userName;

	private String password;

	private String sex;

	private String realName;

	/**
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return realName
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * @return sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param realName
	 *            Ҫ���õ� realName
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * @param sex
	 *            Ҫ���õ� sex
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * @param userName
	 *            Ҫ���õ� userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param password
	 *            Ҫ���õ� password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param userId
	 *            Ҫ���õ� userId
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

}

