package algorab.data.utils;

public class StatusBar {
	
	public static void bar(int percent){
		StringBuilder bar = new StringBuilder("[");
		
		for(int k = 0; k < 50; k++){
	        if( k < (percent/2)){
	            bar.append("=");
	        }else if( k == (percent/2)){
	            bar.append(">");
	        }else{
	            bar.append(" ");
	        }
	     }
	    
	    bar.append("]   " + percent + "%     ");
	    System.out.print("\r" + bar.toString());
	}

}
