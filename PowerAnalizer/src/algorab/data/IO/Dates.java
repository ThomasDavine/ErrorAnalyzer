package algorab.data.IO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dates {
	
	final static SimpleDateFormat TIME = new SimpleDateFormat("HH:mm:ss");

	final static SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd");
	
	final static SimpleDateFormat DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private double[] vector;
	
	public Dates(double[] vector){this.vector=vector; }

	public double beginDate() throws ParseException{
		double beginDate = 0.0;
		Date day = DATE.parse(DATE.format(vector[0])); 
		int count=1;
		for(int i=0; i<vector.length; i++){
			{
				if(DATE.parse(DATE.format(vector[i])).equals(day)){
					count = count +1;
				}else{
					count = 1;
					day = DATE.parse(DATE.format(vector[i]));
			}
				if(count > 1400){
				beginDate = vector[i];
				break;
				}
			}
		}
		
		return beginDate;
	}
	
}
