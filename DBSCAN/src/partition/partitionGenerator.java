package partition;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVWriter;

import util.CSV;

public class partitionGenerator { 
	
	public static CostSpatialPartitioning createPartitions(Grid smbr, double maxcost, double epsilon) {
		CostSpatialPartitioning partitioner=new CostSpatialPartitioning(smbr);
		partitioner.partition(maxcost,epsilon);
		partitioner.findIntersections(epsilon);
		return partitioner;
	}
	public static void main(String[] args) throws IOException {
		Grid grid=new Grid();
		double x_lb=Double.parseDouble(args[0]);
		double y_lb=Double.parseDouble(args[1]);
		double x_tr=Double.parseDouble(args[2]);
		double y_tr=Double.parseDouble(args[3]);
		double epsilon=Double.parseDouble(args[4]);
		String outputPath=args[5];
		grid.createGrid(x_lb, y_lb, x_tr, y_tr, epsilon);
		//read from the output from the first file
		Map<Long, Long> countmap=new HashMap<>();
		File[] output=CSV.getFiles(outputPath);
		for(int i=0;i<output.length;i++) {
			File file=output[i];
			List<String[]> lines=CSV.readCSV(file.getPath(), '\t');
			for(int j=0;j<lines.size();j++) {
				String[] line=lines.get(j);
				long key=Long.parseLong(line[0]);
				long value=Long.parseLong(line[1]);
				countmap.put(key, value);
			}
	}
		List<Cell> cells=grid.getCells();
		for(long i=0;i<cells.size();i++) {
			Cell cell=cells.get((int) i);
			if(countmap.containsKey(i+1))
				cell.setNumPoints(countmap.get(i+1));
			else
				cell.setNumPoints(0);
		}
		
		CostSpatialPartitioning partitioner= createPartitions(grid, 200, epsilon);
		writePartitions(partitioner.getPartitions(),"partitions.tsv");
		writeIntersectingPart(partitioner.getInstersectingPartitions(),"intersectPartition.tsv");
			
		}
	private static void writeIntersectingPart(Map<Rectangle, List<Rectangle>> instersectingPartitions,String fileName) throws IOException {
		CSVWriter writer=CSV.createCsvWrtier(fileName, '\t');
		// TODO Auto-generated method stub
		for(Rectangle r:instersectingPartitions.keySet()) {
			int key=r.getId();
			List<Rectangle> interSectR=instersectingPartitions.get(r);
			for(int i=0;i<interSectR.size();i++) {
				String value="";
				Rectangle ir=interSectR.get(i);
				value+=ir.getId();
				String line=key+" "+value;
				writer.writeNext(line.split(" "));
			}
			
			
		}
		writer.close();
		
	}
	private static void writePartitions(List<Rectangle> partitions,String fileName) throws IOException {
		// TODO Auto-generated method stub
	CSVWriter writer=CSV.createCsvWrtier(fileName, '\t');
	for(int i=0;i<partitions.size();i++) {
		Rectangle r=partitions.get(i);
		String line=i+" "+r.toString();
		writer.writeNext(line.split(" "));
		
	}
	writer.close();
		
	}

}
