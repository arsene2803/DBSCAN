package clustering;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.Text;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;
import com.github.davidmoten.rtree.geometry.Rectangle;

import mapper.PartitionCount;

public class testInnerOuter {
	
	public static void main(String[] args) {
		partition.Rectangle r=new partition.Rectangle(0, 0, 50, 50);
		partition.Rectangle ir=r.getInnerRectangle(10);
		partition.Rectangle or=r.getOuterRectangle(10);
		Rectangle main=Geometries.rectangle(r.getLower_x(), r.getLower_y(), r.getTop_x(), r.getTop_y());
		Rectangle inner=Geometries.rectangle(ir.getLower_x(), ir.getLower_y(), ir.getTop_x(), ir.getTop_y());
		Rectangle outer=Geometries.rectangle(or.getLower_x(), or.getLower_y(), or.getTop_x(), or.getTop_y());
		RTree<String, Rectangle> rtree= RTree.create(); 
		rtree=rtree.add("S",main);
		rtree=rtree.add("inner",inner);
		rtree=rtree.add("outer",outer);
		Point p=Geometries.point(1, 9);
		//output.write("output-"+key.get(), new Text(""), new Text(p.x()+","+p.y()+"|"+type_map.get(p)+"|"+cmap.get(p)));
			
		Iterator<Entry<String, Rectangle>> nbhdP=rtree.search(p, 10).
			toBlocking().toIterable().iterator();
		List<String> nbhd_list=new ArrayList<>();
		while(nbhdP.hasNext()) {
				nbhd_list.add(nbhdP.next().value());
		}
			


}
}
