package algorab.data.ml;

import java.util.List;
import algorab.data.ml.Instance;


/**
 *
 * @author tommaso moretti
 */
public interface OfflineClassifier {
    /** used to match features to classifier features */
    void setHeader(List<FeatureDescription> fds);
    /** classify a bunch of instances */
    void classify(List<? extends Instance> problems, List output);
    void classify(List<? extends Instance> problems, List output,
			List<Double> confidence);    
}
