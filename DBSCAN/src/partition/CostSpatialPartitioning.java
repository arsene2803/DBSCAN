package partition;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;

import util.SplitLine;
import util.SplitLine.splitType;

public class CostSpatialPartitioning {
	private Grid smbr;
	private List<Rectangle> partitions;
	private Map<Rectangle,List<Rectangle>> instersectingPartitions;

	public CostSpatialPartitioning(Grid smbr) {
		super();
		this.smbr = smbr;
		partitions=new ArrayList<>();
	}
	
	public void partition(double maxcost) {
		Deque<Grid> taskqueue=new LinkedList<>();
		taskqueue.addLast(smbr);
		while(!taskqueue.isEmpty()) {
			Grid grid=taskqueue.removeFirst();
			if(estimateCost(grid)>maxcost) {
				List<Grid> splits=costBasedBinarySplit(grid);
				int index=0;
				while(index!=2) {
					taskqueue.add(splits.get(index++));
				}
			}
			else
				partitions.add(grid.getS());
		}
		
		
	}

	private List<Grid> costBasedBinarySplit(Grid grid) {
		// TODO Auto-generated method stub 
		List<Grid> splits=new ArrayList<>();
		Grid S1 = null,S2 = null;
		List<SplitLine> splitlines=new ArrayList<>();
		double minCost=Double.MAX_VALUE;
		//get x splits
		for(double x_split=grid.getLowerBottomX()+grid.getUnit_length();x_split<grid.getTopRightX();x_split+=grid.getUnit_length()) {
			splitlines.add(new SplitLine(x_split,splitType.X));
		}
		//get Y splits
		for(double y_split=grid.getLowerBottomY()+grid.getUnit_length();y_split<grid.getTopRightY();y_split+=grid.getUnit_length()) {
			splitlines.add(new SplitLine(y_split,splitType.Y));
		}
		for(int i=0;i<splitlines.size();i++) {
			Grid split_1,split_2;
			SplitLine line=splitlines.get(i);
			double x_low_1,y_low_1,x_top_1,y_top_1;
			double x_low_2,y_low_2,x_top_2,y_top_2;
			
			if(line.getType()==splitType.X) {
				//get the sub rectangles corresponding to the split
				x_low_1=grid.getLowerBottomX();
				y_low_1=grid.getLowerBottomY();
				y_top_1=grid.getTopRightY();
				x_top_1=line.getSplit();
			    x_low_2=line.getSplit();
				y_low_2=grid.getLowerBottomY();
				y_top_2=grid.getTopRightY();
				x_top_2=grid.getTopRightX();
				
			}
			else {
				//get the sub rectangles corresponding to the split
				x_low_1=grid.getLowerBottomX();
				y_low_1=grid.getLowerBottomY();
				y_top_1=line.getSplit();
				x_top_1=grid.getTopRightX();
				x_low_2=grid.getLowerBottomX();
				y_low_2=line.getSplit();
				y_top_2=grid.getTopRightY();
				x_top_2=grid.getTopRightX();
				
				
			}
			split_1=new Grid(x_low_1,y_low_1,x_top_1,y_top_1);
			split_2=new Grid(x_low_2,y_low_2,x_top_2,y_top_2);
			//assigning cells now
			List<Cell> cells1=new ArrayList<>();
			List<Cell> cells2=new ArrayList<>();
			List<Cell> gridcells=grid.getCells();
			for(int j=0;j<gridcells.size();j++) {
				Cell c=gridcells.get(j);
				if(line.getType()==splitType.X) {
					if(c.getTop_x()<=line.getSplit()) {
						cells1.add(c);
					}
					else
						cells2.add(c);
				}
				else {
					if(c.getTop_y()<=line.getSplit()) {
						cells1.add(c);
					}
					else
						cells2.add(c);
				}
			}
			split_1.setCells(cells1);
			split_2.setCells(cells2);
			
			//get the costs
			double split_1_cost=estimateCost(split_1);
			double split_2_cost=estimateCost(split_2);
			double diff=Math.abs(split_1_cost-split_2_cost);
			if(diff<minCost) {
				S1=split_1;
				S2=split_2;
				minCost=diff;
			}
				
			}
		splits.add(S2);
		splits.add(S1);
		return splits;
	}

	private double estimateCost(Grid grid) {
		// TODO Auto-generated method stub
		List<Cell> cells=grid.getCells();
		double cost=0;
		long totalNumPoints=0;
		for(int i=0;i<cells.size();i++) {
			totalNumPoints+=cells.get(i).getNumPoints();
		}
		for(int i=0;i<cells.size();i++) {
			double costCell=calCost(cells.get(i).getNumPoints(),totalNumPoints);
			cost+=costCell;
		}
		return cost;
	}
	public void findIntersections(double searchDistance) {
		if(instersectingPartitions==null)
			instersectingPartitions=new HashMap<>();
		//create RTREE
		RTree<String, com.github.davidmoten.rtree.geometry.Rectangle> rtree= RTree.create();
		//add rectangles to the rtree
		for(int i=0;i<partitions.size();i++) {
			Rectangle r=partitions.get(i);
			rtree=rtree.add(i+"",Geometries.rectangle(r.getLower_x(), r.getLower_y(), r.getTop_x(),r.getTop_y()));
		}
		//search for neighbours
		for(int i=0;i<partitions.size();i++) {
			Rectangle r=partitions.get(i);
			Iterator<Entry<String,com.github.davidmoten.rtree.geometry.Rectangle >> nbhdP=rtree.search(Geometries.rectangle(r.getLower_x(), r.getLower_y(), r.getTop_x(),r.getTop_y()), searchDistance).
					toBlocking().toIterable().iterator();
			List<String> nbhd_list=new ArrayList<>();
			while(nbhdP.hasNext()) {
				nbhd_list.add(nbhdP.next().value());
			}
			
			for(int j=0;j<nbhd_list.size();j++) {
				int id=Integer.parseInt(nbhd_list.get(j));
				if(id!=i) {
					Rectangle idr=partitions.get(id);
					if((!instersectingPartitions.containsKey(idr)||
							(!instersectingPartitions.get(idr).contains(r)))) {
						if(instersectingPartitions.containsKey(r)) {
							List<Rectangle> interpartitions=instersectingPartitions.get(r);
							interpartitions.add(partitions.get(id));
						}
						else {
							List<Rectangle> interpartitions=new ArrayList<>();
							interpartitions.add(partitions.get(id));
							instersectingPartitions.put(r, interpartitions);
							
						}	
					}
						
				}
				
					
			}
			
		}
	}

	private double calCost(long numberOfPoints,long totalNumPoints) {
		// TODO Auto-generated method stub
		double f=100;
		double h=1+Math.ceil(Math.log10((totalNumPoints/f))/Math.log10(f));
		double cost=1+h+Math.sqrt(numberOfPoints)*(2/(Math.sqrt(f)-1))+numberOfPoints*(1/(f-1));
		return cost;
	}
	

}