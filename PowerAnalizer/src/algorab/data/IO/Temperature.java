package algorab.data.IO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

public class Temperature {
	
	TreeMap<Integer,String[]> csv;
	
	TreeMap<Date, Double> data;
	
	
	public Temperature(TreeMap<Integer,String[]> csv) throws ParseException{
		this.csv=csv;
		convert();
		
	}
	
	private void convert() throws ParseException{
		data = new TreeMap<Date, Double>();
		SimpleDateFormat df =new SimpleDateFormat("HH:mm:ss");
		
		for(int i=1; i<csv.size(); i++){
			Date date = df.parse(csv.get(i)[0]); 
			double value = Double.parseDouble(csv.get(i)[1]);
			data.put(date, value);
		}
	}

	public TreeMap<Date, Double> data(){
		return data;
	}
}
