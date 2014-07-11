package yue.soft.design.pattern.decorator;

public class Main {

	public static void main(String[] args) {
		Finery finery = new Finery();
		
		Shirt shirt = new Shirt();
		Suit suit = new Suit();
		Shoes shoes = new Shoes();
		
		//装饰过程
		shirt.decorate(finery);
		suit.decorate(shirt);
		shoes.decorate(suit);
		
		shoes.show();
	}

}
