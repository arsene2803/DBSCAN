package partition;

import java.util.ArrayList;
import java.util.List;

public class testGraph {
	
	public static void main(String[] args) {
		UndirectedGraphNode n1=new UndirectedGraphNode("1");
		UndirectedGraphNode n2=new UndirectedGraphNode("2");
		UndirectedGraphNode n3=new UndirectedGraphNode("3");
		n1.addNeighbour(n2);
		n2.addNeighbour(n1);
		List<UndirectedGraphNode> nodelist=new ArrayList<>();
		nodelist.add(n1);
		nodelist.add(n2);
		nodelist.add(n3);
		List<List<String>> results=Graph.connectedSet(nodelist);
		results.get(0);
		
	}

}
