package reducer;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class NumCellReducer extends Reducer<LongWritable, IntWritable, LongWritable, LongWritable> {

	public void reduce(LongWritable _key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		int count=0;
		// process values
		for (IntWritable val : values) {
			count+=1;

		}
		context.write(_key, new LongWritable(count));
	}

}
