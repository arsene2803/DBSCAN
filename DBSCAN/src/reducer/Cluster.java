package reducer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.filecache.DistributedCache;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;
import com.github.davidmoten.rtree.geometry.Rectangle;

import clustering.Dbscan;
import mapper.PartitionCount;
import util.Configuration.flag;
import util.Configuration.type;

public class Cluster extends Reducer<LongWritable, Text, Text, Text> {
	private static long minPnts;
	private static double epsilon;
	private static Map<Long, partition.Rectangle> partition = new HashMap<>();
	private static BufferedReader reader;
	private MultipleOutputs<Text, Text> output;
	
	public void reduce(LongWritable ikey, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		List<Point> pl=new ArrayList<>();
		// process values
		for (Text val : values) {
			int i=0;
			double[] coord=new double[2]; 
			String line=val.toString();
			StringTokenizer st=new StringTokenizer(line,",");
			while(st.hasMoreTokens()) {
				coord[i++]=Double.parseDouble(st.nextToken().replace("\"", ""));
			}
			pl.add(Geometries.point(coord[0], coord[1]));
		}
		Dbscan db=new Dbscan();
		Map<Point,type> type_map=new HashMap<>();
		Map<Point,flag> flag_map=new HashMap<>();
		Map<Point,Integer> cmap=new HashMap<>();
		db.createClusters(pl, minPnts, epsilon, type_map, flag_map, cmap);
		long key=ikey.get();
		partition.Rectangle r=partition.get(key);
		partition.Rectangle ir=r.getInnerRectangle(epsilon);
		for(int i=0;i<pl.size();i++) {
			Point p=pl.get(i);
			String cluster_id=null;
			if(type_map.get(p)==type.NOISE)
				cluster_id=cmap.get(p)+"";
			else
				cluster_id=cmap.get(p)+"-"+key;
			output.write("output"+key, new Text(p.x()+","+p.y()), new Text(type_map.get(p).toString()+"&"+cluster_id));
			if(!ir.containsPoint(p)) {
				if(type_map.get(p)==type.CORE) {
					if(r.containsPoint(p))
						output.write("AP"+key, new Text(p.x()+","+p.y()), new Text(type_map.get(p).toString()+"&"+cluster_id));
					
				}
				else if(type_map.get(p)!=type.NOISE) {
					if(!r.containsPoint(p))
						output.write("BP"+key,  new Text(p.x()+","+p.y()), new Text(type_map.get(p).toString()+"&"+cluster_id));
				}
			}
		}
	}

	@Override
	protected void cleanup(Reducer<LongWritable, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		super.cleanup(context);
		output.close();
	}

	@Override
	protected void setup(Reducer<LongWritable, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		super.setup(context);
		minPnts=Long.parseLong(context.getConfiguration().get("minPnts"));
		epsilon=Double.parseDouble(context.getConfiguration().get("epsilon"));
		output = new MultipleOutputs<Text,Text>(context);
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
			partition.put(key, new partition.Rectangle(corners[0].replace("\"", ""),corners[1]
					.replace("\"", ""),
					corners[2].replace("\"", "")
					,corners[3].replace("\"", "")));
			inputline = reader.readLine();
		}
		
		
	}

}
