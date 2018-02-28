package partition;

public class Cell extends Rectangle {
	
	private long numPoints;

	public long getNumPoints() {
		return numPoints;
	}

	public void setNumPoints(long numPoints) {
		this.numPoints = numPoints;
	}

	public Cell(double lower_x, double lower_y, double top_x, double top_y) {
		super(lower_x, lower_y, top_x, top_y);
		// TODO Auto-generated constructor stub
	}

}
