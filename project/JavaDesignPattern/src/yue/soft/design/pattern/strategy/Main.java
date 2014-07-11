package yue.soft.design.pattern.strategy;

public class Main {
	public static void main(String[] args) {
		Context context = new Context(new StrategyA());
		context.callStrategy();
		context = new Context(new StrategyB());
		context.callStrategy();

	}
}
