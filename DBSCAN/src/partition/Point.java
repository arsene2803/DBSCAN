package partition;

import util.Configuration.*;

public class Point  {
	
	private double x;
	private double y;
	private type type;
	private flag flag;
	private long cluster_id;
	
	public long getCluster_id() {
		return cluster_id;
	}

	public void setCluster_id(long cluster_id) {
		this.cluster_id = cluster_id;
	}

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
	public Point(String x, String y) {
		super();
		this.x = Double.parseDouble(x);
		this.y = Double.parseDouble(y);
		this.flag=flag.not_visited;
	}
	

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		Point p=(Point) obj;
		if(this.x==p.getX()&&this.y==p.getY())
			return true;
		return false;
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
