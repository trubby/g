package Trubby.co.th.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeConverter {

	public static String getDate(Long l){
		long yourmilliseconds = 1119193190;
	    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");

	    Date resultdate = new Date(yourmilliseconds);
	    System.out.println(sdf.format(resultdate));
	    
	    return sdf.format(resultdate);
	}
	
	public static String milisToMin(long time){
		StringBuilder sb = new StringBuilder();
		if(time > 60000){ //min.
			sb.append(TimeUnit.MILLISECONDS.toMinutes(time) + " min. ");
		}
		
		if(time > 1){
			sb.append(TimeUnit.MILLISECONDS.toSeconds(time%60000) + " sec.");
		}else{
			sb.append("0 sec.");
		}
		return sb.toString();
	}
}
