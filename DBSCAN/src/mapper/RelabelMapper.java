package mapper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
import util.Configuration.type;

public class RelabelMapper extends Mapper<LongWritable, Text, Text, Text> {
	
	private static BufferedReader reader;
	private static Map<String, String> mergeMap = new HashMap<>();

	public void map(LongWritable ikey, Text ivalue, Context context) throws IOException, InterruptedException {
		double[] coord=new double[2];
		String[] line=ivalue.toString().split("\t");
		StringTokenizer st=new StringTokenizer(line[0],",");
		int i=0;
		while(st.hasMoreTokens()) {
			coord[i++]=Double.parseDouble(st.nextToken().replace("\"", ""));
		}
		Point p=new Point(coord[0],coord[1]);
		String[] info=line[1].split("|");
		type ptype=type.valueOf(info[0]);
		String cluster_id=info[1];
		if(mergeMap.containsKey(cluster_id))
			context.write(new Text(p.toString()), new Text(ptype+"|"+mergeMap.get(cluster_id)));
		else 
			context.write(new Text(p.toString()), new Text(ptype+"|"+cluster_id));

	}

	@Override
	protected void setup(Mapper<LongWritable, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		Path[] cacheFilesLocal = DistributedCache.getLocalCacheFiles(context.getConfiguration());
		for (Path eachPath : cacheFilesLocal) {
			
			loadMergeMapping(eachPath, context);
			
		}
		
	}

	private void loadMergeMapping(Path p, Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException {
		// TODO Auto-generated method stub
		reader = new BufferedReader(new FileReader(p.toString()));
		String inputline = reader.readLine();
		String[] input;
		while (inputline != null) {
			input = inputline.split("\t");
			String local_cluster_ID=input[0];
			String global_cluster_ID=input[1];
			mergeMap.put(local_cluster_ID, global_cluster_ID);
			inputline = reader.readLine();
		}
		
	}

}
