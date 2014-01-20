package algorab.data.instancer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import algorab.data.config.ConfigProperties;
import algorab.data.ml.InstanceWriter;
import algorab.data.ml.weka.WEKAInstanceWriter;

public class DataTrainer {
	
	private Object _trainer;
	private Object data;

	public DataTrainer(){
		data=data;
        _trainer=createTrainer(data);
	}
	
	
	private Object createTrainer(Object data2) {
		
		return null;
	}


	public static List<InstanceWriter> createClusters() throws IOException
	        
	    {
	        List<InstanceWriter> iws=new ArrayList<InstanceWriter>();
	        iws.add(
	        		new WEKAInstanceWriter(new FileWriter(new File(""))));
	        
	        
			return iws;
	    }
	
	public void run() throws IOException
    {
        //DataProcessor proc=new DataProcessor(_trainer);
        
        //proc.createTrainingData(ConfigProperties.getInstance());
    }

}
