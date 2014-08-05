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
	 *            要设置的 realName
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * @param sex
	 *            要设置的 sex
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * @param userName
	 *            要设置的 userName
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
	 *            要设置的 password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param userId
	 *            要设置的 userId
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

}

