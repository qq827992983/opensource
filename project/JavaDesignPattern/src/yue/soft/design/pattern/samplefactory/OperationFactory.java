package yue.soft.design.pattern.samplefactory;

public class OperationFactory {
	public static Operation createOperation(String operation){
		Operation oper = null;
		if(operation.equals("+")){
			oper = new OperationAdd();
		}else if(operation.equals("-")){
			oper = new OperationSub();
		}else{
			return null;
		}
		return oper;
	}
}
