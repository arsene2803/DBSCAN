package partition;

import java.util.List;

public class Rectangle {
	private double lower_x,lower_y,top_x,top_y;
	private double cost;
	private List<Point> points;

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}
	/**
     * Get Number of points belonging to the rectangle
	 * @return
	 */
	public int getNumberOfPoints() {
		if(points!=null)
			return points.size();
		else 
			return 0;
	}

	public Rectangle(double lower_x, double lower_y, double top_x, double top_y) {
		super();
		this.lower_x = lower_x;
		this.lower_y = lower_y;
		this.top_x = top_x;
		this.top_y = top_y;
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
	
	
}
