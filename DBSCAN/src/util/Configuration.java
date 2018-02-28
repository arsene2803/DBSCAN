package util;

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
	

}
