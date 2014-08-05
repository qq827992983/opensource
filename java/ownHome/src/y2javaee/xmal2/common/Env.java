package y2javaee.xmal2.common;

import java.io.InputStream;
import java.util.Properties;

public final class Env extends Properties {

	private static final long serialVersionUID = -3261703704807028328L;

	private static Env instance;

	/**
	 * �Ե���ģʽ����Envʵ��
	 * 
	 * @return
	 */
	public static Env getInstance() {
		if (instance != null) {
			return instance;
		} else {
			makeInstance();
			return instance;
		}
	}

	/**
	 * ���Env���󲻴��ڣ�����ʹ��췽����ͬ����
	 * 
	 */
	private static synchronized void makeInstance() {
		if (instance == null) {
			instance = new Env();
		}
	}

	/**
	 * ˽�еĹ��췽��
	 * 
	 */
	private Env() {
		InputStream is = getClass().getResourceAsStream("/db.properties");
		try {
			load(is);
		} catch (Exception e) {
			System.err.println("����û�ж�ȡ�����ļ�����ȷ��db.property�ļ��Ƿ���ڡ�");
			return;
		}
	}
}
