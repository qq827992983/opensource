package y2javaee.xmal2.operation;

/**
 * Web����ķ���ӿ�
 *
 */
public interface IBookService {
	/**
	 * BOOK����ӷ���
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
	 * @return int ִ��SQL���Ӱ�������
	 */
	public int addBookService(String name, String sex, String phone,
			String address, String mobilePhone, String company,String comPhone,
			String comAddress, String relation, int userId);

}
