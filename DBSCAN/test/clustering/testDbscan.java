package clustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;

import util.Configuration.flag;
import util.Configuration.type;

public class testDbscan {
	
	public static void main(String[] args) {
		List<Point> pl=new ArrayList<>();
		Point p1=Geometries.point(1, 4);
		Point p2=Geometries.point(2, 5);
		Point p3=Geometries.point(100, 100);
		Point p4=Geometries.point(5, 7);
		Point p5=Geometries.point(3,4);
		Point p6=Geometries.point(4,3);
		Point p7=Geometries.point(1,2);
		Point p8=Geometries.point(3,5);
		pl.add(p1);
		pl.add(p2);
		pl.add(p3);
		pl.add(p4);
		pl.add(p5);
		pl.add(p6);
		pl.add(p7);
		pl.add(p8);
		Dbscan db=new Dbscan();
		Map<Point,type> type_map=new HashMap<>();
		Map<Point,flag> flag_map=new HashMap<>();
		Map<Point,Integer> cmap=new HashMap<>();
		db.createClusters(pl, 3, 10, type_map, flag_map, cmap);
		
		
	}

}
