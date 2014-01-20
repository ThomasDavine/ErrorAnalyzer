package algorab.data.ml.weka;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import algorab.data.config.ConfigProperties;
import algorab.data.ml.ClassifierFactory;
import algorab.data.ml.InstanceWriter;
import algorab.data.ml.OfflineClassifier;

/**
 *
 * @author tommaso moretti
 */
public class WEKAClassifierFactory implements ClassifierFactory {
    private static WEKAClassifierFactory _factory;
    
    public static WEKAClassifierFactory getInstance() {
        if (_factory==null) {
            _factory=new WEKAClassifierFactory();
        }
        return _factory;
    }
    
    public InstanceWriter getSink(String model_name, String options, String learner)
            throws IOException
    {
        return new WEKAInstanceWriter(new FileWriter(
                        new File(ConfigProperties.getInstance().getModelDir(),
                            model_name+".arff")));
    }

    public void do_learning(String model_name, String options, String learner)
            throws IOException
    {
        WEKAInstanceClassifier.runLearner(model_name, learner, options);
    }

    public OfflineClassifier getClassifier(String model_name, String options,
            String learner) throws IOException
    {
        try {
            return new WEKAInstanceClassifier(
                            new File(ConfigProperties.getInstance().getModelDir(),
                                model_name+".obj").getCanonicalPath());
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("cannot load classifier",ex);
        }
    }

}
