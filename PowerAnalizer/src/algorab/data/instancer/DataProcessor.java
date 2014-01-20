package algorab.data.instancer;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.TreeMap;

import algorab.data.dbconnection.MSSQL;
import algorab.data.interpolation.SplineInterpolation;

/**
*
* @author Tommaso Moretti
*/

public class DataProcessor extends Thread {

	private String id_impianto;
	
	private TreeMap<Double,ArrayList<Double>> data;

	private ArrayList<String> csv;
	
	public DataProcessor(String id_impianto) {
		this.id_impianto = id_impianto;
	}
	
	public void run(){
		MSSQL db = MSSQL.getInstance();
		try {
			// Retrieve data form SQLdatabase 
			db.executeStatement(id_impianto);
			
			// Interpolate data and re-frame
			SplineInterpolation li = new SplineInterpolation(db.time(), db.values());
			li.interpolate();
			
			// Set data
			data = li.getData();
			csv = li.getCSVLine();
			
		} catch (SQLException | ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Getter for data
	public TreeMap<Double,ArrayList<Double>> data(){
		return data;
	}
	
	//Getter for csv line
	public ArrayList<String> csv(){
		return csv;
	}
	
	// Getter for id_impianto
	public String id_impianto(){
		return id_impianto;
	}
}
