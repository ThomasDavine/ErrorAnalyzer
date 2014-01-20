package algorab.data.ml.weka;

import java.io.IOException;

import algorab.data.ml.ClusterFactory;
import algorab.data.ml.OfflineClassifier;

public class WEKAClusterFactory implements ClusterFactory {
    private static WEKAClusterFactory _factory;
    
    public static WEKAClusterFactory getInstance() {
        if (_factory==null) {
            _factory=new WEKAClusterFactory();
        }
        return _factory;
    }
    
    public void do_cluster(String options, String learner)
            throws IOException
    {
        WEKAInstanceClusterer instance;
    }

    public OfflineClassifier getClusterer(String options,
            String clusterer) throws IOException
    {
        try {
            return new WEKAInstanceClusterer(clusterer);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("cannot load cluster",ex);
        }
    }

}
