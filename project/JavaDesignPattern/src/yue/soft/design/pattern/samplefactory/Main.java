package yue.soft.design.pattern.samplefactory;

public class Main {

	public static void main(String[] args) {
		Operation op = OperationFactory.createOperation("+");
		op.setNum1(100.11);
		op.setNum2(78.12);
		System.out.println(op.getResult());
		op = OperationFactory.createOperation("-");
		op.setNum1(100.11);
		op.setNum2(78.12);
		System.out.println(op.getResult());
	}

}
