package mapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.filecache.DistributedCache;

import partition.Pair;
import partition.Point;

public class MergeMapper extends Mapper<LongWritable, Text, LongWritable, LongWritable> {
	private static Map<Long,List<Point>> APMap=new HashedMap();
	private static Map<Long,List<Point>> BPMap=new HashedMap();
	private BufferedReader reader;
	

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String[] keys=value.toString().split("\t");
		long key1=Long.parseLong(keys[0]);
		long key2=Long.parseLong(keys[1]);
		List<Point> ap1=APMap.get(key1);
		List<Point> ap2=APMap.get(key2);
		List<Point> bp1=APMap.get(key1);
		List<Point> bp2=APMap.get(key2);
		//get ap1 intersection bp1
		List<Pair> intersection_1=new ArrayList<>();
		List<Pair> intersection_2=new ArrayList<>();
		getIntersectionPoints(ap1, bp1, intersection_1);
		getIntersectionPoints(ap2, bp2, intersection_2);
		writeClusterId(context, intersection_1);
		writeClusterId(context, intersection_2);
		
	}

	public void writeClusterId(Context context, List<Pair> intersection_1) throws IOException, InterruptedException {
		for(int i=0;i<intersection_1.size();i++) {
			Pair p=intersection_1.get(i);
			Point p1=p.getP1();
			Point p2=p.getP2();
			context.write(new LongWritable(p1.getCluster_id()),new LongWritable(p2.getCluster_id()));
			
		}
	}

	public void getIntersectionPoints(List<Point> ap1, List<Point> bp1, List<Pair> intersection_1) {
		if(ap1!=null && bp1!=null) {
			for(int i=0;i<ap1.size();i++) {
				Point p1=ap1.get(i);
				for(int j=0;j<bp1.size();j++) {
					Point p2=bp1.get(j);
					if(p1==p2)
						intersection_1.add(new Pair(p1,p2));
				}
			}
		}
	}

	@Override
	protected void setup(Mapper<LongWritable, Text, LongWritable, LongWritable>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		super.setup(context);
		Path[] cacheFilesLocal = DistributedCache.getLocalCacheFiles(context.getConfiguration());
		for (Path eachPath : cacheFilesLocal) {
			System.out.println(eachPath.toString());
			if (eachPath.getName().startsWith("part")) {
				loadSeedHashMap(eachPath, context);
			}
}
		
	}

	private void loadSeedHashMap(Path eachPath, Context context) throws IOException {
		// TODO Auto-generated method stub
		String fileName=eachPath.getName();
		String id=fileName.split("-")[0];
		String s=id.substring(2, id.length());
		long partition_id=Long.parseLong(s);
		reader = new BufferedReader(new FileReader(eachPath.toString()));
		String inputline = reader.readLine();
		String[] input;
		List<Point> pl=new ArrayList<>();
		while (inputline != null) {
			input = inputline.split("\t");
			String[] coord=input[0].split(",");
			long clusterid=Long.parseLong(input[1].split("|")[1]);
			Point p=new Point(coord[0],coord[1]);
			p.setCluster_id(clusterid);
			pl.add(p);
			
		}
		if(fileName.startsWith("AP"))
			APMap.put(partition_id, pl);
		else
			BPMap.put(partition_id, pl);
		
			
			
	}

}
