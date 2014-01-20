package algorab.data.main;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import algorab.data.config.Impianti;
import algorab.data.instancer.DataProcessor;
import algorab.data.utils.StatusBar;

public class Connection {

	/**
	 * @param args
	 * @throws Exception 
	 */
	private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final String path = "/home/black/Documents/Potenza/TemperaturaEsterna.mahout";
	
	public static void main(String[] args) throws Exception {
		
		
		//new Impianti();
		List<String> impianti = new Impianti().getImpianti();
		System.out.println(impianti.size());
		
		
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path), true));
		
		
		DataProcessor[] threads = new DataProcessor[impianti.size()];
		
		
		for(int i=0; i< threads.length; i++){
		try{
			
			// Print a percentage status bar
			int percent = (i*100)/impianti.size();
		    StatusBar.bar(percent);
			
			threads[i] = new DataProcessor(impianti.get(i));
			threads[i].start();
			threads[i].join();
			//System.err.println(threads[i].id_impianto());
			
			Entry entry;
			while((entry = threads[i].data().pollFirstEntry())!=null){
				
				String giorno = df.format(entry.getKey()); 
				//bw.write(String.format("%% %s %s", threads[i].id_impianto(), giorno)); 
				//bw.newLine();
				ArrayList<Double> values = (ArrayList<Double>) entry.getValue();
				for(int attr=0; attr< values.size() -2; attr++){
					bw.write(Double.toString(values.get(attr)));
					bw.write(" ");
				}
				bw.write(Double.toString(values.get(values.size()-1)));
				bw.newLine();
			}
			
			
		}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		bw.close();
	}
	

}
