package partition;

import java.util.List;

public class Rectangle {
	private double lower_x,lower_y,top_x,top_y;
	protected double cost;
	private int id;


	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public Rectangle(double lower_x, double lower_y, double top_x, double top_y) {
		super();
		this.lower_x = lower_x;
		this.lower_y = lower_y;
		this.top_x = top_x;
		this.top_y = top_y;
	}

	public Rectangle(String lower_x, String lower_y, String top_x, String top_y) {
		// TODO Auto-generated constructor stub
		super();
		this.lower_x = Double.parseDouble(lower_x);
		this.lower_y = Double.parseDouble(lower_y);
		this.top_x = Double.parseDouble(top_x);
		this.top_y = Double.parseDouble(top_y);
	}
	
	public Rectangle getOuterRectangle(double epsilon) {
		
		return new Rectangle(this.lower_x-epsilon, this.lower_y-epsilon,
				this.top_x+epsilon, this.top_y+epsilon);
		
	}
	
	public Rectangle getInnerRectangle(double epsilon) {
		
		return new Rectangle(this.lower_x+epsilon, this.lower_y+epsilon,
				this.top_x-epsilon, this.top_y-epsilon);
		
	}
	public boolean containsPoint(com.github.davidmoten.rtree.geometry.Point p) {
		if(p.x()>=this.getLower_x()&&p.x()<=this.getTop_x()) {
			if(p.y()>=this.getLower_y()&&p.y()<=this.getTop_y())
				return true;
		}
		return false;
		
	}

	public double getLower_x() {
		return lower_x;
	}

	public void setLower_x(double lower_x) {
		this.lower_x = lower_x;
	}

	public double getLower_y() {
		return lower_y;
	}

	public void setLower_y(double lower_y) {
		this.lower_y = lower_y;
	}

	public double getTop_x() {
		return top_x;
	}

	public void setTop_x(double top_x) {
		this.top_x = top_x;
	}

	public double getTop_y() {
		return top_y;
	}

	public void setTop_y(double top_y) {
		this.top_y = top_y;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.lower_x+","+this.lower_y+","+this.top_x+","+this.top_y;
	}
	
	
	
	
}
