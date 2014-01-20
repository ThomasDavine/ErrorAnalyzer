package algorab.data.ml.weka;

import algorab.data.config.ConfigProperties;
import algorab.data.ml.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import weka.classifiers.Classifier;
import weka.classifiers.SingleClassifierEnhancer;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.Utils;

/**
 *
 * @author brett.shwom
 */
public class WEKAInstanceClassifier implements OfflineClassifier {
    
    /** Creates a new instance of WEKAInstanceClassifier */

    private final Classifier classifier;
    private final Instances dataset;
    private List<FeatureDescription> _fds;
    
    
    public WEKAInstanceClassifier(String filename)
            throws IOException, ClassNotFoundException
    {
        ObjectInputStream f = new ObjectInputStream(new FileInputStream(filename));
        classifier = (Classifier) f.readObject();
        dataset = (Instances) f.readObject();
        f.close();
    }
     
    public WEKAInstanceClassifier(File file)
        throws IOException, ClassNotFoundException
    {
        ObjectInputStream f = new ObjectInputStream(new FileInputStream(file));
        classifier = (Classifier) f.readObject();
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
                double retVal = classifier.classifyInstance(instance);
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

    public static void runLearner(String modelName, String classifier,
            String options)
        throws IOException
    {
        
        Instances trainingSet=new Instances(new FileReader(
                new File(ConfigProperties.getInstance().getModelDir(),modelName+".arff")));
                
        trainingSet.setClassIndex(trainingSet.numAttributes()-1);
        Classifier cls;
        try {
            try {
                Class.forName(classifier);
            }
            catch(ClassNotFoundException e)
            {
                System.err.format("Class %s not found, using J48\n",classifier);
                classifier="weka.classifiers.trees.J48";
            }
            String[] args=Utils.splitOptions(options);
            cls = SingleClassifierEnhancer.forName(classifier, args);
            cls.buildClassifier(trainingSet);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Weka learner failed",e);
        }
        ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(
                new File(ConfigProperties.getInstance().getModelDir(),
                    modelName+".obj")));
        oos.writeObject(cls);
        trainingSet.delete();
        oos.writeObject(trainingSet);
        oos.close();
    }

    public void setHeader(List<FeatureDescription> fds) {
        _fds = fds;
    }
}
