package algorab.data.ml;

import java.io.IOException;

/** A ClassifierFactory represents a certain way to learn and
 *  execute a classifier.
 * 
 * @author tommaso moretti
 */
public interface ClassifierFactory {
    InstanceWriter getSink(String model_name, String options, String learner)
            throws IOException;
    void do_learning(String model_name, String options, String learner)
            throws IOException;
    OfflineClassifier getClassifier(String model_name, String options,
            String learner)
            throws IOException;
}
