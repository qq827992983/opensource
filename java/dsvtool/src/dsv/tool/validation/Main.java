package dsv.tool.validation;

public class Main {
	public static void main(String[] args) {
		String file = "./conf/conf.xml";
		Validation valid = new Validation(file);
		valid.start();
		valid.stop();
	}
}
