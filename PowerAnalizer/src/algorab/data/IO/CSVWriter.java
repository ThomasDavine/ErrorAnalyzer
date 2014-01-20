package algorab.data.IO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import algorab.data.config.ConfigProperties;
import algorab.data.config.Impianti;
import algorab.data.instancer.DataProcessor;

public class CSVWriter {

	/**
	 * @param args
	 */
	static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static final SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd");
	static BufferedWriter bw;
	static String path;
	static DataProcessor[] threads;
	static int frequencyInMinutes;
	private static String output;
	private static String modelDir;
	
	public static void main(String[] args0) throws IOException{
		
		Impianti list = new Impianti();
		frequencyInMinutes = ConfigProperties.getInstance().getTemporalFrequency();
		modelDir = "/home/black/Documents";
				//ConfigProperties.getInstance().getModelDir();
		List<String> impianti = Impianti.impianti;
		
		threads = new DataProcessor[impianti.size()];
		
		for(int i=0; i< threads.length; i++){
		try{

			// Build the status bar
			int percent = (i*100)/impianti.size();
			statusbar(percent);
			
			output = String.format("%s/Impianti/p/%s.csv",
					modelDir,
					impianti.get(i));
			
		    
	
		    // Get the data from the MSSQLdb and interpolate
			threads[i] = new DataProcessor(impianti.get(i));
			threads[i].start();
			threads[i].join();
			//System.err.println(threads[i].id_impianto());
			
			bw = new BufferedWriter(new FileWriter(new File(output),true));
			for(String line : threads[i].csv()){
				bw.write(String.format("%s,%s,Potenza attiva trifase equivalente " +
						"", line, impianti.get(i)));
				bw.newLine();
			}
			
			bw.close();
			
			}catch(Exception e){
				e.printStackTrace();}
			}
		
		
		
		
	}
	
	private static void statusbar(int percent){
		StringBuilder bar = new StringBuilder("[");
		for(int k = 0; k < 50; k++){
	        if( k < (percent/2)){
	            bar.append("=");
	        }else if( k == (percent/2)){
	            bar.append(">");
	        }else{
	            bar.append(" ");
	        }
	     }
	    
	    bar.append("]   " + percent + "%     ");
	    System.out.print("\r" + bar.toString());
	}


}
