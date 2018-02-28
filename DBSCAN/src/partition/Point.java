package partition;

import util.Configuration.*;

public class Point  {
	
	private double x;
	private double y;
	private type type;
	private flag flag;
	
	public flag getFlag() {
		return flag;
	}

	public void setFlag(flag flag) {
		this.flag = flag;
	}

	public type getType() {
		return type;
	}

	public void setType(type type) {
		this.type = type;
	}

	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
		this.flag=flag.not_visited;
	}
	

	public Point() {
		// TODO Auto-generated constructor stub
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.x+","+this.y;
	}
	
}
