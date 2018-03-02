package partition;

import java.util.ArrayList;
import java.util.List;

public class Grid {
	private Rectangle S;
	private List<Cell> cells;
	private double unit_length;
	private double num_rows;
	private double num_cols;
	
	public Grid() {
		
	}
	
	public List<Cell> getCells() {
		return cells;
	}


	public void setCells(List<Cell> cells) {
		this.cells = cells;
	}




	public Rectangle getS() {
		return S;
	}


	public void setS(Rectangle s) {
		S = s;
	}


	/**
	 * Create a grid of n * n cells,where each cell is of side 2 epsilon
	 * @param x_lb
	 * @param y_lb
	 * @param x_tr
	 * @param y_tr
	 * @param epsilon
	 */
	public void createGrid(double x_lb,double y_lb,double x_tr,double y_tr,double epsilon) {
		if(cells==null)
			cells =new ArrayList<>();
		//check if MBR has sides of %2 epsilon
		double side_length_x=x_tr-x_lb;
		unit_length=2*epsilon;
		
		int rem=(int)( (side_length_x)%(unit_length));
		
		if(rem!=0) {
			int quotient=(int) ( (side_length_x)/(unit_length));
			x_tr+=(quotient+1)*(unit_length)-side_length_x;
			side_length_x=x_tr-x_lb;
		}
		
		double side_length_y=y_tr-y_lb;
		rem=(int)( (side_length_y)%(unit_length));
		if(rem!=0) {
			int quotient=(int) ( (side_length_y)/(unit_length));
			y_tr+=(quotient+1)*(unit_length)-side_length_y;
			side_length_y=y_tr-y_lb;
		}
		//creating the mbr
		S=new Rectangle(x_lb, y_lb, x_lb+side_length_x, y_lb+side_length_y);
		/*
		 * determine the number of rows and columns of the grid 
		 */
		num_cols=(int) (side_length_x/(unit_length));
		num_rows=(int) (side_length_y/(unit_length));
		
		double cell_x_lb=x_lb;
		double cell_x_tr=x_lb+unit_length;
		double cell_y_lb=y_lb;
		double cell_y_tr=y_lb+unit_length;
		
		for(int i=0;i<num_rows;i++) {
			if(i!=0) {
				cell_y_lb+=unit_length;
				cell_y_tr+=unit_length;
			}
			for(int j=0;j<num_cols;j++) {
				
				cells.add(new Cell(cell_x_lb+j*unit_length, cell_y_lb, cell_x_tr+j*unit_length, cell_y_tr));
				
			}
		}
		
	}
	public double getLowerBottomX() {
		return S.getLower_x();
		
	}
	public double getLowerBottomY() {
		return S.getLower_y();
	}
	public double getTopRightX() {
		return S.getTop_x();
	}
	public double getTopRightY() {
		return S.getTop_y();
	}


	public double getUnit_length() {
		return unit_length;
	}


	public Grid(double lower_x, double lower_y, double top_x, double top_y,double epsilon) {
		unit_length=2*epsilon;
		S = new Rectangle(lower_x, lower_y, top_x, top_y);
	}


	public void setUnit_length(double unit_length) {
		this.unit_length = unit_length;
	}


	public double getNum_rows() {
		return num_rows;
	}


	public void setNum_rows(double num_rows) {
		this.num_rows = num_rows;
	}


	public double getNum_cols() {
		return num_cols;
	}


	public void setNum_cols(double num_cols) {
		this.num_cols = num_cols;
	}


}
