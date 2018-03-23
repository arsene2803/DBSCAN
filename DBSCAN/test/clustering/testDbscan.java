package clustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;

import util.CSV;
import util.Configuration.flag;
import util.Configuration.type;

public class testDbscan {
	
	public static void main(String[] args) {
		List<Point> pl=new ArrayList<>();
		/*pl.add(Geometries.point(2, 3));
		pl.add(Geometries.point(4, 3));
		pl.add(Geometries.point(1, 3));
		pl.add(Geometries.point(5, 3));
		pl.add(Geometries.point(7, 3));
		RTree<String, Point> rtree= RTree.create(); 
		//add the points to rtree
		for(int i=0;i<pl.size();i++) {
			Point p=pl.get(i);
			rtree=rtree.add("Point Number-"+i,p);
		}
		Iterator<Entry<String, Point>> nbhdP=rtree.search(pl.get(0), 2.00001).
				toBlocking().toIterable().iterator();
		List<Point> nbhd_list=new ArrayList<>();
		while(nbhdP.hasNext()) {
			nbhd_list.add(nbhdP.next().geometry());
		}
		Iterator<Entry<String, Point>> nbhdQ=rtree.search(pl.get(1), 10).
				toBlocking().toIterable().iterator();
		List<Point> nbhd_list1=new ArrayList<>();
		while(nbhdQ.hasNext()) {
			nbhd_list1.add(nbhdQ.next().geometry());
		}
		for(int i=0;i<nbhd_list1.size();i++) {
			Point p=nbhd_list1.get(i);
			if(!nbhd_list.contains(p))
				nbhd_list.add(p);
			
		}*/
		
		List<String[]> lines=CSV.readCSV("input.csv", ',');
		for(int i=0;i<lines.size();i++) {
			String[] line=lines.get(i);
			pl.add(Geometries.point(Double.parseDouble(line[0]), Double.parseDouble(line[1])));
		}
		
		
		Dbscan db=new Dbscan();
		Map<Point,type> type_map=new HashMap<>();
		Map<Point,flag> flag_map=new HashMap<>();
		Map<Point,Integer> cmap=new HashMap<>();
		db.createClusters(pl, 3, 10, type_map, flag_map, cmap);
		
		
	}

}
