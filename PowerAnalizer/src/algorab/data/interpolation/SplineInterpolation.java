package algorab.data.interpolation;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;

import algorab.data.config.ConfigProperties;

/**
*
* @author Tommaso Moretti
*/

public class SplineInterpolation {
	
	private double[] x;
	
	private double[] y;
	
	/** The frequency with which we want to interpolate data */
	private int frequencyInMinutes;
	
	/** The bounder class for interpolation */ 
	private InterpolationBounder bounds;
	
	final long DAY_IN_MILLIS = 86400000;
	
	/** The number of observations per day we want */
	private int temporalSequencesPerDay;
	
	/** A map to store the result key<Date in millisec> Values<Interpolated observations during the day> */
	private TreeMap<Double,ArrayList<Double>> output;

	private ArrayList<String> csv;
	
	/** The constructor for the interpolation */
	public SplineInterpolation(double[] time, double[] value) throws Exception{
		this.x=time;
		this.y=value;
		bounds = new InterpolationBounder(x);
		frequencyInMinutes = ConfigProperties.getInstance().getTemporalFrequency();
		temporalSequencesPerDay = 1440/frequencyInMinutes;
		
	}

	/** Interpolate data and get a fixed number of observations per day */
	public void interpolate() throws IOException, ParseException{
		
		// The x values should be strictly monotonically increasing 
		UnivariateInterpolator interpolator = new LinearInterpolator();
		
		// Build the spline function 
		UnivariateFunction function = interpolator.interpolate(x,y);
		

		
		
		// Set the date bounds in millisecs
		double date = bounds.getBeginDate();
		int numberOfDays = (int) ((bounds.getEndDate() - 
				bounds.getBeginDate())/ DAY_IN_MILLIS )+2;
		
		// Store the data in a TreeMap to preserve time order
		output = new TreeMap<Double,ArrayList<Double>>();
		csv = new ArrayList<String>();
		for(int n=1; n <numberOfDays; n++){
		
			ArrayList<Double> observations = new ArrayList<Double>();
			for(int j=0; j< temporalSequencesPerDay; j++){
			
				double interpolationX = date;
				if(interpolationX > bounds.getEndDate()){
					interpolationX = bounds.getEndDate();}
				
				double interpolatedY = function.value(interpolationX);
			
				// Add to the next value of x a fixed amount of minutes in millisec
				String line = String.format("%s,%s", 
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date), 
						Double.toString(interpolatedY));
				
				
				date = date + (60000*frequencyInMinutes);
				observations.add(interpolatedY);
				csv.add(line);
			}
		
			// Finally add the daily observations to the map
			output.put(date - DAY_IN_MILLIS, observations);
		}
		
	}
	
	// Getter for the data
	public TreeMap<Double, ArrayList<Double>> getData(){
		return output;
	}
	
	public ArrayList<String> getCSVLine(){
		return csv;
	}
	
	public static double trim(Double d){
		int index = Double.toString(d).indexOf(".");
		String string = Double.toString(d).substring(0, index +3);
		return Double.valueOf(string);
	}
	
}
