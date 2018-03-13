package partition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Graph {
	 public static List<List<String>> connectedSet(List<UndirectedGraphNode> nodes) {
	        List<List<String>> rst = new ArrayList<>();
	        if (nodes == null || nodes.size() == 0) {
	            return rst;
	        }
	        //Init:
	        Set<UndirectedGraphNode> checked = new HashSet();
	        Queue<UndirectedGraphNode> queue = new LinkedList();
	        ArrayList<String> component = new ArrayList<String>();
	        
	        queue.offer(nodes.get(0));

	        while (!nodes.isEmpty()) {
	            if (queue.isEmpty()) {
	                Collections.sort(component);
	                rst.add(component);
	                queue.offer(nodes.get(0));
	                component = new ArrayList<String>();
	            } else {
	                UndirectedGraphNode curr = queue.poll();
	                if (!checked.contains(curr)) {
	                    checked.add(curr);
	                    component.add(curr.cluster_id);
	                    nodes.remove(curr);
	                    for (UndirectedGraphNode node : curr.neighbors) {
	                            queue.add(node);    
	                    }
	                }
	            }
	        }
	        if (!component.isEmpty()) {
	            rst.add(component);
	        }
	        return rst;
	}

}
