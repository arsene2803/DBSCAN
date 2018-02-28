package util;

import java.io.IOException;

import com.opencsv.CSVWriter;

import partition.Grid;

public class SU {
	private Grid grid;
	/**
	 * parameters are the lower and top corners of the MBR
	 * @param x_lb
	 * @param y_lb
	 * @param x_tr
	 * @param y_tr
	 * @param epsilon
	 * @param fileName 
	 * @param delimiter 
	 * @throws IOException 
	 */
	public void createSU(double x_lb,double y_lb,double x_tr,double y_tr,double epsilon, String fileName, char delimiter) throws IOException {
		grid=new Grid();
		grid.createGrid(x_lb, y_lb, x_tr, y_tr, epsilon);
		writeCSV(fileName, delimiter);
	}
	public void writeCSV(String fileName,char delimiter) throws IOException {
		CSVWriter writer=CSV.createCsvWrtier(fileName, delimiter);
		String line=grid.getNum_rows()+","+grid.getNum_cols()+","+grid.getUnit_length();
		writer.writeNext(line.split(","));
		CSV.closeFile(writer);
	}

}
