package partition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;

public class TestCostSpatialPartitioning {
	public static void main(String args[]) {
		Grid grid=new Grid();
		grid.createGrid(0, 0, 50, 50, 5);
		List<Cell> cells=grid.getCells();
		cells.get(0).setNumPoints(0);
		cells.get(1).setNumPoints(0);
		cells.get(2).setNumPoints(0);
		cells.get(3).setNumPoints(0);
		cells.get(4).setNumPoints(1);
		cells.get(5).setNumPoints(0);
		cells.get(6).setNumPoints(0);
		cells.get(7).setNumPoints(0);
		cells.get(8).setNumPoints(0);
		cells.get(9).setNumPoints(1);
		cells.get(10).setNumPoints(0);
		cells.get(11).setNumPoints(2);
		cells.get(12).setNumPoints(1);
		cells.get(13).setNumPoints(0);
		cells.get(14).setNumPoints(1);
		cells.get(15).setNumPoints(1);
		cells.get(16).setNumPoints(5);
		cells.get(17).setNumPoints(0);
		cells.get(18).setNumPoints(0);
		cells.get(19).setNumPoints(1);
		cells.get(20).setNumPoints(0);
		cells.get(21).setNumPoints(0);
		cells.get(22).setNumPoints(0);
		cells.get(23).setNumPoints(0);
		cells.get(24).setNumPoints(1);
		CostSpatialPartitioning partioner=new CostSpatialPartitioning(grid);
		partioner.partition(20,5);
		partioner.findIntersections(5);
		partioner.hashCode();
		//creating RTREE
		RTree<String, Point> rtree= RTree.create();
		Point p1=Geometries.point(4, 5);
		Point p2=Geometries.point(7, 3);
		rtree=rtree.add("Point Number-",p1);
		rtree=rtree.add("Point Number-",p2);
		Iterator<Entry<String, Point>> nbhdP=rtree.search(p1, 5).
				toBlocking().toIterable().iterator();
		List<Point> nbhd_list=new ArrayList<>();
		while(nbhdP.hasNext()) {
			nbhd_list.add(nbhdP.next().geometry());
		}
		nbhd_list.remove(p1);
		p1.hashCode();
	}

}
