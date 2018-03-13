package driver;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.filecache.DistributedCache;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import mapper.RelabelMapper;
import reducer.RelabelReducer;

public class RelabelData {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		//Checking for the number of argument
		if(args.length != 3) {
			throw new IllegalArgumentException("Arguments expected- merge_input input output "
					);
		}
		FileSystem fs = FileSystem.get(conf);
		addOutput(fs,new Path(args[0]) , conf);
		Job job = Job.getInstance(conf, "Relabel");
		job.setJarByClass(driver.RelabelData.class);
		// TODO: specify a mapper
		job.setMapperClass(RelabelMapper.class);
		// TODO: specify a reducer
		job.setReducerClass(RelabelReducer.class);

		// TODO: specify output types
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		// TODO: specify input and output DIRECTORIES (not files)
		FileInputFormat.setInputPaths(job, new Path(args[1]));
		FileOutputFormat.setOutputPath(job, new Path(args[2]));

		if (!job.waitForCompletion(true))
			return;
	}
	
	public static void addOutput(FileSystem fs, Path p, Configuration conf) throws IOException {
		FileStatus[] status = fs.listStatus(p);
		for (int i = 0; i < status.length; i++) {
				DistributedCache.addCacheFile(status[i].getPath().toUri(), conf);
				
			}
		}

}
