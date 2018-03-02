package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class CSV {
	public static CSVWriter createCsvWrtier(String finalOutputPath,char delimiter) throws IOException {
		CSVWriter writer=new CSVWriter(new FileWriter(finalOutputPath),delimiter);
		return writer;
	}
	public static void closeFile(CSVWriter writer) throws IOException {
		writer.close();
	}
	public static List<String[]> readCSV(String fileName,char delimiter){
		try {
			CSVReader reader=new CSVReader(new FileReader(fileName),delimiter);
			List<String[]> values=reader.readAll();
			reader.close();
			return values;
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static File[] getFiles(String outputPath) {
		File folder = new File(outputPath);
		File[] listOfFiles = folder.listFiles();
		return listOfFiles;
	}
}
