package reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.ObjectUtils.Null;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import partition.Graph;
import partition.UndirectedGraphNode;

public class Merge extends Reducer<NullWritable, Text, Text, Text> {

	public void reduce(NullWritable _key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		// process values
		HashSet<String> vertices=new HashSet<String>();
		List<UndirectedGraphNode> nodes=new ArrayList<>();
		Map<String,UndirectedGraphNode> nmap=new HashMap<String, UndirectedGraphNode>();
		
		for (Text val : values) {
			String[] ids=val.toString().split("&");
			String id1=ids[0].replace("\"", "");
			String id2=ids[1].replace("\"", "");
			addVertex(vertices, nmap, id1,nodes);
			addVertex(vertices, nmap, id2,nodes);
			addEdge(nmap, id1, id2);
			addEdge(nmap, id2, id1);
		}
		List<List<String>> components=Graph.connectedSet(nodes);
		long globalID=0;
		for(int i=0;i<components.size();i++) {
			List<String> cc=components.get(i);
			for(int j=0;j<cc.size();j++) {
				String clusterID=cc.get(j);
				context.write(new Text(clusterID), new Text(globalID+""));
			}
			globalID+=1;
		}
	}

	public void addEdge(Map<String, UndirectedGraphNode> nmap, String id1, String id2) {
		UndirectedGraphNode node=nmap.get(id1);
		List<UndirectedGraphNode> neighbours=node.getNeighbors();
		if(!neighbours.contains(id2))
			neighbours.add(nmap.get(id2));
	}

	public void addVertex(HashSet<String> vertices, Map<String, UndirectedGraphNode> nmap, String id1,List<UndirectedGraphNode> nodes) {
		if(!vertices.contains(id1)){
			vertices.add(id1);
			UndirectedGraphNode node=new UndirectedGraphNode(id1);
			nmap.put(id1,node);
			nodes.add(node);
		}
	}

}
