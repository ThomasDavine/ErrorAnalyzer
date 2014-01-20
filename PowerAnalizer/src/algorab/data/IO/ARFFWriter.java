package algorab.data.IO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import algorab.data.config.ConfigProperties;
import algorab.data.config.Impianti;
import algorab.data.instancer.DataProcessor;


public class ARFFWriter {
	
	static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static final SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd");
	static BufferedWriter bw;
	static String path;
	static DataProcessor[] threads;
	static int frequencyInMinutes;
	
	public static void main(String[] args0) throws IOException{
		
		Impianti list = new Impianti();
		frequencyInMinutes = ConfigProperties.getInstance().getTemporalFrequency();
		List<String> impianti = Impianti.impianti;
		
		path = "/home/black/Documents/Impianti/p/PotenzaAttiva.arff";
		//writeHeader();
		
		
		
		threads = new DataProcessor[impianti.size()];
		
		
		
		for(int i=0; i< threads.length; i++){
		try{

			// Build the status bar
			int percent = (i*100)/impianti.size();
		    statusbar(percent);
	
		    // Get the data from the MSSQLdb and interpolate
			threads[i] = new DataProcessor(impianti.get(i));
			threads[i].start();
			threads[i].join();
			//System.err.println(threads[i].id_impianto());
			
			
			
			Entry entry;
			while((entry = threads[i].data().pollFirstEntry())!=null){
				bw = new BufferedWriter(new FileWriter(new File(path), true));
				double giorno = (double) entry.getKey(); 
				String giorno_string = DATE.format(entry.getKey());
				ArrayList<Double> values = (ArrayList<Double>) entry.getValue();
				bw.write(String.format("%% %s %s", threads[i].id_impianto(), giorno_string));
				bw.newLine();
				
				for(int attr=0; attr< values.size()-1; attr++){
					double value = trim(values.get(attr));
					bw.write(Double.toString(value));
					//bw.write(Integer.toString(attr));
					bw.write(",");
					giorno = giorno + (60000*frequencyInMinutes);
				}
				
				bw.write(Double.toString(trim(values.get(values.size()-1))));
				bw.newLine();
				bw.close();
				
			}
				
			
		}catch(Exception e){
			System.err.println(impianti.get(i));
				e.printStackTrace();
			}
		
		
		}
		
		
	}
	
	private static void writeHeader() throws IOException {
		bw = new BufferedWriter(new FileWriter(new File(path)));
		bw.write("@RELATION something");
		bw.newLine();
		bw.newLine();
		int temporalSequencesPerDay = 1440/frequencyInMinutes;
		for(int i=0; i<temporalSequencesPerDay; i++){
			bw.write(String.format("@ATTRIBUTE attr%s NUMERIC", i+1));
			bw.newLine();
		}
		bw.newLine();
		bw.write("@DATA");
		bw.newLine();
		bw.close();
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
	
	public static double trim(Double d){
		String value;
		int index = Double.toString(d).indexOf(".");
		if(index + 2 < Double.toString(d).length()){
			value = Double.toString(d).substring(0, index +3);
		}else{
			value = Double.toString(d).substring(0, index +2)+"0";
		}	
		return Double.valueOf(value);
	}
}
