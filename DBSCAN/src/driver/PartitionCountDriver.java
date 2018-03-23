package driver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.filecache.DistributedCache;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import mapper.MergeMapper;
import mapper.PartitionCount;
import reducer.Cluster;
import reducer.Merge;

public class PartitionCountDriver {

	public static void main(String[] args) throws Exception {
		//Checking for the number of argument
		if(args.length != 8) {
			System.out.println(args.length);
			throw new IllegalArgumentException("Arguments expected- input output partition intersecting_partition merge_output epsilon minPnts dataset"
					);
		}
		Configuration conf = new Configuration();
		DistributedCache.addCacheFile(new Path(args[2]).toUri(), conf);
		conf.set("epsilon",args[5]);
		conf.set("minPnts",args[6]);
		conf.set("dataset", args[7]);
		//int partitionCount=getPartititionCount(args[2], conf);
		int partitionCount=1599;
		Job job = Job.getInstance(conf, "Partition");
		job.setJarByClass(driver.PartitionCountDriver.class);
		// TODO: specify a mapper
		job.setMapperClass(PartitionCount.class);
		// TODO: specify a reducer
		job.setReducerClass(Cluster.class);
		// TODO: specify output types
		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		//set the Multiple outputs
		for(int i=0;i<partitionCount;i++) {
			MultipleOutputs.addNamedOutput(job, "output"+i, TextOutputFormat.class, Text.class, Text.class);
			MultipleOutputs.addNamedOutput(job, "AP"+i, TextOutputFormat.class, Text.class, Text.class);
			MultipleOutputs.addNamedOutput(job, "BP"+i, TextOutputFormat.class, Text.class, Text.class);
		}
		// TODO: specify input and output DIRECTORIES (not files)
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		if (job.waitForCompletion(true)) {
			Configuration conf2 = new Configuration();
			//FileSystem fs = FileSystem.get(conf2);
			//addOutput(fs,new Path(args[1]) , conf2);
			conf2.set("outputPath", args[1]);
			Job job2 = Job.getInstance(conf2, "Merging");
			job2.setJarByClass(driver.PartitionCountDriver.class);
			// TODO: specify a mapper
			job2.setMapperClass(MergeMapper.class);
			// TODO: specify a reducer
			job2.setReducerClass(Merge.class);
			// TODO: specify output types
			job2.setMapOutputKeyClass(NullWritable.class);
			job2.setMapOutputValueClass(Text.class);
			job2.setOutputKeyClass(Text.class);
			job2.setOutputValueClass(Text.class);
			job2.setOutputFormatClass(TextOutputFormat.class);
			
			// TODO: specify input and output DIRECTORIES (not files)
			FileInputFormat.setInputPaths(job2, new Path(args[3]));
			FileOutputFormat.setOutputPath(job2, new Path(args[4]));
			if (!job2.waitForCompletion(true)) {
				return;
			}
				
		}
		
		

		
	}

	public static int getPartititionCount(String path, Configuration conf) throws IOException {
		//get the number of partitions
		FileSystem fs = FileSystem.get(conf);
		int count=0;
		BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(new Path(path))));
		String line;
		line = br.readLine();
		while (line != null) {
			count++;
			line = br.readLine();
		}
		return count;
	}
	public static void addOutput(FileSystem fs, Path p, Configuration conf) throws IOException {
		FileStatus[] status = fs.listStatus(p);
		for (int i = 0; i < status.length; i++) {
			if (status[i].getPath().getName().startsWith("AP") || status[i].getPath().getName().startsWith("BP")) {
				DistributedCache.addCacheFile(status[i].getPath().toUri(), conf);
			}
				
			}
		}
}
