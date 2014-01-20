package algorab.data.ml;

/**An Instance represents a generic classification instance for ML learning.
 *
 */

public interface Instance {
   <T> T getFeature(FeatureDescription<T> descr);
   <T> void setFeature(FeatureDescription<T> descr, T value);
   String getDebugInfo();
}
