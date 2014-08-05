package y2javaee.xmal2.operation;

/**
 * Web服务的服务接口
 *
 */
public interface IBookService {
	/**
	 * BOOK表添加方法
	 * @param name
	 * @param sex
	 * @param phone
	 * @param address
	 * @param mobilePhone
	 * @param company
	 * @param comPhone
	 * @param comAddress
	 * @param relation
	 * @param userId
	 * @return int 执行SQL语句影响的行数
	 */
	public int addBookService(String name, String sex, String phone,
			String address, String mobilePhone, String company,String comPhone,
			String comAddress, String relation, int userId);

}
