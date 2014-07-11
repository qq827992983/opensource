package yue.soft.design.pattern.decorator;

//服饰类
public class Finery implements Show{
	protected Show show;
	
	public void show() {
		if(show != null){
			show.show();
		}
	}
	
	public void decorate(Show show){
		this.show = show;
	}
}
