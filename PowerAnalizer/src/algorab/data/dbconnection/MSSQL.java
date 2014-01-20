package algorab.data.dbconnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import algorab.data.config.ConfigProperties;

/**
*
* @author Tommaso Moretti
*/
public class MSSQL {
   private static MSSQL _instance;
   
   /** The db connection */
   private transient Connection connection;
   
   /** The statement to the db */
   private transient Statement statement;
   
   /** Cached database results */
   private TreeMap<String, Double> dataCache;

   /** The database results */
   private ResultSet resultSet;
   
   private ArrayList<String> allImpianti;
   
   /** A SimpleDateFormat to parse SQLDate */
   private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   
   private double[] x;
	
   private double[] y;
	
   public static MSSQL getInstance() {
       {
           _instance=new MSSQL();
       }
       return _instance;
   }
   
   /** Creates a new instance of MSSQL */
   private MSSQL() {
       try {
           connection = ConfigProperties.getInstance().getDBConnection();
           statement = connection.createStatement();
       } catch (SQLException ex) {
           ex.printStackTrace();
           throw new RuntimeException("cannot connect to database",ex);
       }
       dataCache = new TreeMap<String, Double>();
   }
   
   /** Gets and store data  
    * @return 
   @throws ParseException */
   public ArrayList<String> impianti() throws SQLException, ParseException {
	   allImpianti = new ArrayList<String>();
	   resultSet =
    		   statement.executeQuery("SELECT [codice] FROM [H3G_SiteEnergySaving].[dbo].[impianti] ");
           
       while(resultSet.next()) {
    	   
    	   allImpianti.add(resultSet.getString("codice"));
       }
       
       return allImpianti;
       
   }
   
   /** Gets and store data  
   @throws ParseException */
   public void executeStatement(String id_impianto) throws SQLException, ParseException {
	   resultSet =
    		   statement.executeQuery("SELECT datax, [valore] FROM [H3G_SiteEnergySaving].[dbo].[BlackBox_analogiche]" +
            		   "left join [H3G_SiteEnergySaving].[dbo].[risorse] on BlackBox_analogiche.id_ris = risorse.id_risorsa "+
            		   "left join [H3G_SiteEnergySaving].[dbo].[tipi_risorse] on risorse.id_tipo_risorsa = tipi_risorse.id_tipo_risorsa "+
            		   "left join [H3G_SiteEnergySaving].[dbo].[impianti] on risorse.id_impianto = impianti.id_impianto "+
            		   "WHERE  tipi_risorse.nome_risorsa = 'Potenza attiva trifase equivalente' and impianti.codice = '"+id_impianto+"' "+
            		   "and valore != 0");
           
       while(resultSet.next()) {
    	   dataCache.put(resultSet.getString("datax"), resultSet.getDouble("valore"));
    	   //System.out.println(resultSet.getString("datax") +"; "+   resultSet.getDouble("valore"));
       }
       resultSet.close();
       setVectors();
       
       
   }
   
   private void setVectors() throws ParseException{
		x = new double[dataCache.keySet().size()];
		y = new double[dataCache.keySet().size()];
		int i=0;
		
		for(Map.Entry<String, Double> entry : dataCache.entrySet()){
			x[i] = df.parse(entry.getKey().substring(0, 19)).getTime();
			y[i] = entry.getValue();
			i++;
		}
   }
   
   public double[] time(){
	   return x;
   }
   
   public double[] values(){
	   return y;
   }
   
   
}