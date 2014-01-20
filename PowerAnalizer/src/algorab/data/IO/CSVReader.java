package algorab.data.IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class CSVReader {
	
	private String path;
	
	private File file;
	
	private double[] x;
	
	private double[] y;
	
	private ArrayList<Double> listTime;
	
	private ArrayList<Double> listTemp;
	
	private BufferedReader br;
	
	private SimpleDateFormat df;
	
	public CSVReader(String path) throws IOException{
		this.path=path;
		this.file=new File(path);
		read();
	}
	
	public void read() throws IOException{
		br = new BufferedReader(new FileReader(file));
		df = new SimpleDateFormat("HH:mm:ss");
		this.listTime = new ArrayList<Double>();
		this.listTemp = new ArrayList<Double>();
				
		
		String line="";
		while((line=br.readLine())!=null){
			if(!line.matches("Time,TE")){
				try {
					double millisec = toSeconds(line.split(",")[0]) * 1000; 
					listTime.add(millisec);
					listTemp.add(Double.parseDouble(line.split(",")[1]));
				}catch (Exception e) {e.printStackTrace();}
			}
		}
		br.close();
	}
	
	public double[] time(){
		x = new double[listTime.size()];
		for(int i=0;i<listTime.size();i++){
			x[i]=listTime.get(i);
		}
		return x;
	}
	
	public double[] te(){
		y = new double[listTemp.size()];
		for(int i=0;i<listTemp.size();i++){
			y[i]=listTemp.get(i);
		}
		return y;
	}
	
	private double toSeconds(String date){
		double sec = 0.0;
		String[] time = date.split(":");
		
		double hours = Double.parseDouble(time[0]);
		double minutes = Double.parseDouble(time[1]);
		double seconds = Double.parseDouble(time[2]);
		sec = (hours*3600) + (minutes*60) + seconds;
		return sec;
	}
}
