package driver;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import mapper.SUMapper;
import partition.Grid;
import reducer.NumCellReducer;
import util.SU;

public class CellCountDriver {


	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		//Checking for the number of argument
		if(args.length != 7) {
			System.out.println(args.length);
			throw new IllegalArgumentException("Arguments expected- x_lb y_lb x_tr y_tr epsilon input output"
					);
		}
		Grid grid=new Grid();
		double x_lb=Double.parseDouble(args[0]);
		double y_lb=Double.parseDouble(args[1]);
		double x_tr=Double.parseDouble(args[2]);
		double y_tr=Double.parseDouble(args[3]);
		double epsilon=Double.parseDouble(args[4]);
		grid.createGrid(x_lb, y_lb, x_tr, y_tr, epsilon);
		conf.set("num_cols",grid.getNum_cols()+"");
		conf.set("num_rows", grid.getNum_rows()+"");
		conf.set("unit_length",grid.getUnit_length()+"");
		Job job = Job.getInstance(conf, "Partitioning");
		job.setJarByClass(driver.CellCountDriver.class);
	
		
		// TODO: specify a mapper
		job.setMapperClass(SUMapper.class);
		// TODO: specify a reducer
		job.setReducerClass(NumCellReducer.class);

		// TODO: specify output types
		job.setMapOutputKeyClass(LongWritable.class);
	    job.setMapOutputValueClass(IntWritable.class);
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(LongWritable.class);

		// TODO: specify input and output DIRECTORIES (not files)
		FileInputFormat.setInputPaths(job, new Path(args[5]));
		FileOutputFormat.setOutputPath(job, new Path(args[6]));

		if (!job.waitForCompletion(true))
			return;
	}

}
