package driver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.filecache.DistributedCache;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import mapper.PartitionCount;

public class PartitionCountDriver {

	public static void main(String[] args) throws Exception {
		//Checking for the number of argument
		if(args.length != 5) {
			System.out.println(args.length);
			throw new IllegalArgumentException("Arguments expected- input output partition epsilon minPnts"
					);
		}
		Configuration conf = new Configuration();
		DistributedCache.addCacheFile(new Path(args[2]).toUri(), conf);
		conf.set("epsilon",args[3]);
		conf.set("minPnts",args[4]);
		int partitionCount=getPartititionCount(args, conf);
		Job job = Job.getInstance(conf, "Partition");
		job.setJarByClass(driver.PartitionCountDriver.class);
		// TODO: specify a mapper
		job.setMapperClass(PartitionCount.class);
		// TODO: specify a reducer
		//job.setReducerClass(Reducer.class);
		job.setNumReduceTasks(0);
		// TODO: specify output types
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(Text.class);
		//set the Multiple outputs
		for(int i=0;i<partitionCount;i++) {
			MultipleOutputs.addNamedOutput(job, "output-"+i, TextOutputFormat.class, Text.class, Text.class);
			MultipleOutputs.addNamedOutput(job, "AP-"+i, TextOutputFormat.class, Text.class, Text.class);
			MultipleOutputs.addNamedOutput(job, "BP-"+i, TextOutputFormat.class, Text.class, Text.class);
		}
		// TODO: specify input and output DIRECTORIES (not files)
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		if (!job.waitForCompletion(true))
			return;
	}

	public static int getPartititionCount(String[] args, Configuration conf) throws IOException {
		//get the number of partitions
		FileSystem fs = FileSystem.get(conf);
		int count=0;
		BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(new Path(args[2]))));
		String line;
		line = br.readLine();
		while (line != null) {
			count++;
			line = br.readLine();
		}
		return count;
	}

}
