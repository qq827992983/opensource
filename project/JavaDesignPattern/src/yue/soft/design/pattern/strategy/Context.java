package yue.soft.design.pattern.strategy;

public class Context {
	AbstractStrategy strategy;
	
	public Context(AbstractStrategy strategy){
		this.strategy = strategy;
	}
	
	public void callStrategy(){
		strategy.AlgorithmInterface();
	}
}
