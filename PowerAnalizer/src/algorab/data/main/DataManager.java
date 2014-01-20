package algorab.data.main;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import algorab.data.IO.Dates;
import algorab.data.dbconnection.MSSQL;




public class DataManager {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		List<String> impianti = new ArrayList<String>();
		impianti.add("AL0267");
		
		MSSQL db = MSSQL.getInstance();
		try {
			// Retrieve data form SQLdatabase 
			db.executeStatement(impianti.get(0));
			
			// Calulate the starting date for interpolation
			Dates dates = new Dates(db.time());
			System.out.println(new SimpleDateFormat("yyyy-MM-dd")
			.format(dates.beginDate()));
		
		
		} catch (SQLException | ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
