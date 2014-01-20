package algorab.data.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Impianti {
	
	public static List<String> impianti;
	
	private static String impiantiList;
	
	public Impianti(){
		impiantiList = ConfigProperties.getInstance().getImpiantiList();
		setImpianti();
	}

	private static void setImpianti() {
		impianti = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(impiantiList)));
			String id_Impianto ="";
			while((id_Impianto = br.readLine())!=null){
				impianti.add(id_Impianto);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getImpianti(){
		return impianti;
	}
}
