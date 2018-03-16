package partition;

import java.util.ArrayList;
import java.util.List;

public class UndirectedGraphNode {
	 public String  cluster_id;
	 List<UndirectedGraphNode> neighbors;
	 public UndirectedGraphNode(String x) { cluster_id = x; neighbors = new ArrayList<UndirectedGraphNode>(); }
	 
	 public void addNeighbour(UndirectedGraphNode n) {
		 neighbors.add(n);
		 
	 }

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		UndirectedGraphNode n=(UndirectedGraphNode)obj;
		if(n.cluster_id.equals(n.cluster_id))
			return true;
		return false;
	}
	public List<UndirectedGraphNode> getNeighbors(){
		return neighbors;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return cluster_id.hashCode();
	}
	
}
