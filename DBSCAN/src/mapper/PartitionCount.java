package mapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.filecache.DistributedCache;

import partition.Point;
import partition.Rectangle;

public class PartitionCount extends Mapper<LongWritable, Text, LongWritable, Text> {
	private static Map<Long, Rectangle> partition = new HashMap<>();
	private static BufferedReader reader;
	private static double epsilon;
	@Override
	protected void setup(Mapper<LongWritable, Text, LongWritable, Text>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		epsilon=Double.parseDouble(context.getConfiguration().get("epsilon"));
		super.setup(context);
		Path[] cacheFilesLocal = DistributedCache.getLocalCacheFiles(context.getConfiguration());
		for (Path eachPath : cacheFilesLocal) {
			
			loadPartition(eachPath, context);
			
		}

	}

	private void loadPartition(Path p, Context context) throws IOException {
		// TODO Auto-generated method stub
		reader = new BufferedReader(new FileReader(p.toString()));
		String inputline = reader.readLine();
		String[] input;
		while (inputline != null) {
			input = inputline.split("\t");
			long key=Long.parseLong(input[0].replace("\"", ""));
			String[] corners=input[1].split(",");
			partition.put(key, new Rectangle(corners[0].replace("\"", ""),corners[1]
					.replace("\"", ""),
					corners[2].replace("\"", "")
					,corners[3].replace("\"", "")));
			inputline = reader.readLine();
		}
		
		
	}

	public void map(LongWritable ikey, Text ivalue, Context context)
			throws IOException, InterruptedException {
		double[] coord=new double[2];
		String line=ivalue.toString();
		StringTokenizer st=new StringTokenizer(line,",");
		int i=0;
		while(st.hasMoreTokens()) {
			coord[i++]=Double.parseDouble(st.nextToken().replace("\"", ""));
		}
		Point p=new Point(coord[0],coord[1]);
		for(Long partitionId:partition.keySet()) {
			Rectangle outerRectangle=partition.get(partitionId).getOuterRectangle(epsilon);
			if((p.getX()>=outerRectangle.getLower_x())&& (p.getX()<=outerRectangle.getTop_x())) {
				if((p.getY()>=outerRectangle.getLower_y())&& (p.getY()<=outerRectangle.getTop_y())) {
					context.write(new LongWritable(partitionId),new Text(p.toString()));
				}
			}
			
		}

	}

}
