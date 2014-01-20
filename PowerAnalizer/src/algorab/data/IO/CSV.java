package algorab.data.IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;



public class CSV extends Thread{
	
	protected File file;
	
	protected String path;
	
	protected String[] columns;
	
	protected TreeMap<Integer, String[]> data;
	
	protected TreeMap<String,TreeMap<Integer,String[]>> allData;
	
	protected String separator = ",";
	
	/** default constructor - separator "," as default */
	public CSV(String path) throws IOException{
		this.path=path;
		this.file=new File(path);
	}
	
	/** specific separator value */
	public CSV(String path, String separator) throws IOException{
		this.path=path;
		this.file=new File(path);
		this.separator=separator;
	}
	
	public void run(){
		try {
			read();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Read the file as an TreeMap<line N°,line.split(<separator>)> */
	public void read() throws FileNotFoundException, IOException{
		
		// If file is directory suggest readAll() before catching exception
		if(!file.isDirectory()){
			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(file));
				String line;
				int i = 0;
				data = new TreeMap<Integer, String[]>();
				while((line = br.readLine())!=null){
					data.put(i,columns = line.split(separator));
					i++;
				}
				br.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/** Return data */
	public TreeMap<Integer, String[]> data() throws FileNotFoundException, IOException{
		read();
		return data;
	}
}
