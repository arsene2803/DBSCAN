package clustering;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;

import rx.Observable;
import util.Configuration.*;


public class Dbscan {
	
	public static List<Point> createClusters(List<Point> pl,long minPnts,double epsilon,
			Map<Point,type> type_map,Map<Point,flag> flag_map,Map<Point,Integer> cmap){
		int clusterID=0;
		
		//creating 
		//creating RTREE
		RTree<String, Point> rtree= RTree.create(); 
		//add the points to rtree
		for(int i=0;i<pl.size();i++) {
			Point p=pl.get(i);
			flag_map.put(p, flag.not_visited);
			rtree=rtree.add("Point Number-"+i,p);
		}
		System.out.println("number of points:"+pl.size());
		for(int i=0;i<pl.size();i++) {
			Point p=pl.get(i);
			System.out.println("Point Number:"+i);
			if(flag_map.get(p)==flag.not_visited) {
				//change the status of the point
				flag_map.put(p, flag.visited);
				System.out.println("get the neighbours");
				//get the neighborhood of p
				Iterator<Entry<String, Point>> nbhdP=rtree.search(p, epsilon).
						toBlocking().toIterable().iterator();
				List<Point> nbhd_list=new ArrayList<>();
				while(nbhdP.hasNext()) {
					nbhd_list.add(nbhdP.next().geometry());
				}
				nbhd_list.remove(p);
				System.out.println("got the neigbhours");
				//check whether noise point
				if(nbhd_list.size()<minPnts) {
					type_map.put(p, type.NOISE);
					cmap.put(p, -1);
					
				}
				else
					
				{
					type_map.put(p, type.CORE);
					cmap.put(p, clusterID);
					Set<Point> nset=new HashSet<>();
					for(int j=0;j<nbhd_list.size();j++) {
						nset.add(nbhd_list.get(j));
					}
					System.out.println("size of neighbours:"+nbhd_list.size());
					for(int j=0;j<nbhd_list.size();j++) {
						System.out.println("size of neighbours:"+nbhd_list.size());
						System.out.println("iteration j:"+j);
						Point q=nbhd_list.get(j);
						if(flag_map.get(q)==flag.not_visited) {
							flag_map.put(q, flag.visited);
							cmap.put(q, clusterID);
							//get neighbours of Q
							Iterator<Entry<String, Point>> nbhdQ=rtree.search(q, epsilon).
									toBlocking().toIterable().iterator();
							int count=0;
							List<Point> temp=new ArrayList<>();
							while(nbhdQ.hasNext()) {
								count++;
								temp.add(nbhdQ.next().geometry());
							}
							temp.remove(q);
							count--;
							//check if core point
							if(count >=minPnts) {
								type_map.put(q, type.CORE);
								for(int m=0;m<temp.size();m++) {
									Point d=temp.get(m);
									if(!nset.contains(d)) {
										nbhd_list.add(nbhd_list.size(),d);
										nset.add(d);
									}
										
								}
							}
							else {
								type_map.put(q, type.BORDER);
							}
							
							
						}
						else if(type_map.get(q)==type.NOISE) {
							cmap.put(q, clusterID);
							type_map.put(q, type.BORDER);
							
						}
							
						
					}
					clusterID+=1;
					
				}
				
				
			}
		}
		
		return pl;
		
	}

}
