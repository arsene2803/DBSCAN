package grid;

import partition.Grid;

public class GridTest {
	
	public static void testGrid() {
		Grid grid=new Grid();
		grid.createGrid(0, 0, 10, 10, 2);
	}
	public static void main(String[] args) {
		testGrid();
		String name="AP199-";
		Long id=Long.parseLong(name.split("-")[0]);
		id+=1;
	}

}
