package util;

import org.apache.hadoop.io.Text;

public final class Configuration {
	private static Configuration instance;
	
	private Configuration() {}
	
	public static Configuration getInstance(){
		if(instance==null)
			instance =new Configuration();
		
		return instance;
		
	}
	
	public static enum type{
		BORDER,NOISE,CORE;
	}
	
	public static enum flag{
		visited,not_visited
	}
	public static double[] getPointsData(Text val) {
		double[] coord=new double[2]; 
		String[] values=val.toString().split(",");
		coord[0]=Double.parseDouble(values[4]);
		coord[1]=Double.parseDouble(values[values.length-1]);
		return coord;
		
	}
	

}
