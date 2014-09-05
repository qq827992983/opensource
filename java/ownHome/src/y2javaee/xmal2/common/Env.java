package y2javaee.xmal2.common;

import java.io.InputStream;
import java.util.Properties;

public final class Env extends Properties {

	private static final long serialVersionUID = -3261703704807028328L;

	private static Env instance;

	/**
	 * 以单例模式创建Env实例
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
	 * 如果Env对象不存在，则访问构造方法（同步）
	 * 
	 */
	private static synchronized void makeInstance() {
		if (instance == null) {
			instance = new Env();
		}
	}

	/**
	 * 私有的构造方法
	 * 
	 */
	private Env() {
		InputStream is = getClass().getResourceAsStream("/db.properties");
		try {
			load(is);
		} catch (Exception e) {
			System.err.println("错误：没有读取属性文件，请确认db.property文件是否存在。");
			return;
		}
	}
}
