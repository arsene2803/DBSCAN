package grid;

import partition.Grid;

public class GridTest {
	
	public static void testGrid() {
		Grid grid=new Grid();
		grid.createGrid(0, 0, 50, 50, 10);
	}
	public static void main(String[] args) {
		testGrid();
	}

}
