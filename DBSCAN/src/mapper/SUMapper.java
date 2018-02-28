package mapper;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SUMapper extends Mapper<LongWritable, Text, LongWritable, IntWritable> {
	private double num_cols,num_rows,unit_length;
	@Override
	protected void setup(Mapper.Context context) throws IOException,InterruptedException{
		
		num_cols=Double.parseDouble(context.getConfiguration().get("num_cols"));
		num_rows=Double.parseDouble(context.getConfiguration().get("num_rows"));
		unit_length=Double.parseDouble(context.getConfiguration().get("unit_length"));
		
	}

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		double[] coord=new double[2];
		String line=value.toString();
		StringTokenizer st=new StringTokenizer(line,",");
		int i=0;
		while(st.hasMoreTokens()) {
			coord[i++]=Double.parseDouble(st.nextToken().replace("\"", ""));
		}
		//get the cell number
		long col=(long) Math.ceil(coord[0]/unit_length);
		col=(col==0)?1:col;
		long row=(long) Math.ceil(coord[1]/unit_length);
		row=(row==0)?1:row;
		long cellNumber=(long) (col+(row-1)*num_cols);
		context.write(new LongWritable(cellNumber), new IntWritable(1));
		
	}

}
