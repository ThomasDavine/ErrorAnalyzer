package algorab.data.ml.weka;

import algorab.data.config.ConfigProperties;
import algorab.data.ml.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import weka.clusterers.*;
import weka.core.Instances;
import weka.core.Utils;

/**
 *
 * @author tommaso moretti
 */
public class WEKAInstanceClusterer implements OfflineClassifier {
    
    /** Creates a new instance of WEKAInstanceClassifier */

    private final Clusterer clusterer;
    private static Instances dataset;
    private List<FeatureDescription> _fds;
    
    
    public WEKAInstanceClusterer(String filename)
            throws IOException, ClassNotFoundException
    {
        ObjectInputStream f = new ObjectInputStream(new FileInputStream(filename));
        clusterer = (Clusterer) f.readObject();
        dataset = (Instances) f.readObject();
        f.close();
    }
     
    public void classify(List<? extends algorab.data.ml.Instance> problems, List output) {
                
        for (Instance i : problems) {
            
            weka.core.Instance instance = new weka.core.DenseInstance(dataset.numAttributes());
            instance.setDataset(dataset);
        
            for (int c = 0; c < _fds.size()-1; c++) {
                FeatureDescription fd = _fds.get(c);
                
                if (fd.type == FeatureType.FT_SCALAR)
                    instance.setValue(c,
                            ((Number) i.getFeature(fd)).doubleValue());
                else
                    instance.setValue(c, i.getFeature(fd).toString());
            }
            try {
                double retVal = clusterer.clusterInstance(instance);
                FeatureDescription des = _fds.get(_fds.size()-1);

                switch (des.type) {
                    case FT_BOOL: 
                        if (retVal < 0.5 )
                            output.add(Boolean.FALSE);
                        else
                            output.add(Boolean.TRUE);
                        break;
                    case FT_NOMINAL_ENUM:
                       output.add(des.cls.getEnumConstants()[(int)retVal]);
                       break;
                    case FT_SCALAR:
                        output.add(retVal);
                        break;
                    
                    default: throw new RuntimeException("Feature Type Unknown");

                }  
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error", e); }
        }
    }
    
    public void classify(List<? extends Instance> problems, List output,
            List<Double> confidence) {
        
        throw new RuntimeException("not implemented");
        
    }

    public static void runLearner(String clusterer,
            String options)
        throws IOException
    {
        
        Clusterer cls;
        try {
            try {
                Class.forName(clusterer);
            }
            catch(ClassNotFoundException e)
            {
                System.err.format("Class %s not found, using SimpleKMeans\n",clusterer);
                clusterer="weka.clusterer.SimpleKMeans";
            }
            
            String[] args=Utils.splitOptions(options);
            cls= SingleClustererEnhancer.forName(clusterer, args);
            cls.buildClusterer(dataset);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Weka learner failed",e);
        }
        ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(
                new File(ConfigProperties.getInstance().getModelDir(),
                    "clusterOutput.obj")));
        oos.writeObject(cls);
        oos.close();
    }

    public void setHeader(List<FeatureDescription> fds) {
        _fds = fds;
    }
}
