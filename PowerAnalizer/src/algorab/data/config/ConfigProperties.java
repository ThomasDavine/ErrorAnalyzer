package algorab.data.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
*
* @author Tommaso Moretti
*/

public class ConfigProperties {
	
	/** IMPLEMENTATION DETAIL: the singleton instance */
    private static ConfigProperties singleton;

    /** Getter for instance */
    public static synchronized ConfigProperties getInstance()
    {
        if (singleton == null)
        { singleton = new ConfigProperties(); }
        return singleton;
    }
    
    private static final String PROP_FILE = "/config/config.properties";

	private static final String DATA_DIR = "DataDir";
	
	private static final String ID_IMPIANTO = "RM4150";

	private static final String MINUTE_FREQUENCY = "1";
    
    private File rootDir;

	private Properties prop;

	private String IMPIANTI_LIST = "/Documents/University/master/Algorab/ConfData/ImpiantiFunzionanti";

	private String MODEL_DIR;
    
    /** Creates a new instance of ConfigProperties */
    private ConfigProperties() {
        
        // loads the properties
        this.prop = new Properties();
        try {
            //String root=System.getProperty("black.rootDir",".");
            rootDir=getRoot();
            final FileInputStream in = new FileInputStream(
                    new File(rootDir,PROP_FILE));
            prop.load(in);
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /** Retrieve a specified property */
    public String getProperty(String p)
    { return prop.getProperty(p); }
    
    public File getRoot() 
    { return new File(System.getProperty("user.dir")); }
    
    /** Gets the data dir */
    public File getDataDir()
    { return new File(getRoot().getAbsolutePath(), prop.getProperty(DATA_DIR, "/Documents/")); }

    public String getModelDir() 
	{ return prop.getProperty(MODEL_DIR, "/home/black/Documents/"); }

    public int getTemporalFrequency()
	{ return Integer.valueOf(prop.getProperty(MINUTE_FREQUENCY, "1")); }

    public String getImpiantiList() 
	{ return prop.getProperty(IMPIANTI_LIST, 
			"/home/black/Documents/University/master/Algorab/ConfData/ImpiantiFunzionanti"); }

    
    /** Gets the system code */
    public String getSystemCode()
    { return prop.getProperty(ID_IMPIANTO, "RM4150"); }
    

    public Connection getDBConnection()
    {
        try {
            Class.forName(prop.getProperty("DB_driver",
                    "com.microsoft.sqlserver.jdbc.SQLServerDriver"));
            String userName=prop.getProperty("DB_user",
                    "sa");
            String password=prop.getProperty("DB_password",
                    "dbo");
            //String dbName=prop.getProperty("DB name", "H3G_SiteEnergySaving");
            
            return
            DriverManager.getConnection(prop.getProperty("dburl",
                    "jdbc:sqlserver://192.168.0.26"),
                    userName, 
                    password);
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Cannot connect to database",ex);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Cannot load DB driver",ex);
        }
    }
}
