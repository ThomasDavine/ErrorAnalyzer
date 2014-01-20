package algorab.data.interpolation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

/**
*
* @author Tommaso Moretti
*/

public class InterpolationBounder {
	
	/** The time vector */
	private double[] time;
	
	/** The lower boundary */
	private double beginDate = 0.0;
	
	/** The upper boundary  */
	private double endDate = 0.0;
	
	/** DateFormat */
	private final static SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd");
	
	/** DatetimeFormat */
	private final static SimpleDateFormat DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/** Constructor with time vector as argument */
	public InterpolationBounder(double[] timeVector) throws ParseException{
		this.time=timeVector;
		reframe();
	}
	
	private void reframe() throws ParseException{
		setBeginDate();
		setEndDate();
	}
	
	public double getBeginDate() {
		return beginDate;
	}

	/** Set up the lower bound for the interpolation 
	 *  to avoid noise data, the bound is set to the 00:00:00 
	 *  of first day with at least 1400 observations
	 * */
	private void setBeginDate() throws ParseException{
		Date day = DATE.parse(DATE.format(time[0])); 
		int count=1;
		for(int i=0; i<time.length; i++){
			{
				if(DATE.parse(DATE.format(time[i])).equals(day)){
					count = count +1;
				}else{
					count = 1;
					day = DATE.parse(DATE.format(time[i]));
			}
				if(count > 1400){
					double x = DATE.parse(DATE.format(time[i])).getTime() +0.0;
				beginDate = x;
				break;
				}
			}
		}
	}

	public double getEndDate() {
		return endDate;
	}

	/** Set up the upper bound for the interpolation */
	private void setEndDate() throws ParseException {
		
		// If last observation from data is after 23:59:00, it is kept as the interpolation limit
		java.util.Date end = DATETIME.parse(DATETIME.format(time[time.length-1]));
		if(DateUtils.round(end, Calendar.HOUR_OF_DAY)
				.compareTo(DateUtils.addDays(DateUtils.
						truncate(end, Calendar.DAY_OF_MONTH),1))>=0){
			endDate = end.getTime() + 0.0;
			
		// else the upper interpolation bound is 23:59:59 of the previous day	
		}else{
			endDate = DateUtils.truncate(end, Calendar.DAY_OF_MONTH).getTime();
		}
		
	}
}
