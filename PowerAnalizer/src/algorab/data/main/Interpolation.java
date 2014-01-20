package algorab.data.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import algorab.data.IO.CSVReader;
import algorab.data.interpolation.SplineInterpolation;


public class Interpolation {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		String path = "/home/black/Documents/days/2010-06-17.csv";
		String outputPath  = "/home/black/Documents/model.arff";
		File output = new File(outputPath);
		
		File dir = new File(path);
		
		
		
			
		CSVReader csvReader = new CSVReader(path);
		SplineInterpolation li = new SplineInterpolation(csvReader.time(), csvReader.te());
		li.interpolate();
		
		
		
	}

}
