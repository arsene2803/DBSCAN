package util;

public class SplitLine {
	double split;
	splitType type;
	
	public double getSplit() {
		return split;
	}
	public void setSplit(double split) {
		this.split = split;
	}
	public splitType getType() {
		return type;
	}
	public void setType(splitType type) {
		this.type = type;
	}
	public SplitLine(double split, splitType type) {
		super();
		this.split = split;
		this.type = type;
	}
	public enum splitType{
		X,Y
	}

}
