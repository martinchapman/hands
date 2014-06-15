package Utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

	/**
	 * @param url
	 * @return
	 */
	public static ArrayList<String> readFromFile(String url) {
		
		ArrayList<String> lines = new ArrayList<String>();
		
		BufferedReader br = null;
	       
  		try {
   
  			String sCurrentLine;
   
  			br = new BufferedReader(new FileReader(url));
   
  			while ((sCurrentLine = br.readLine()) != null) {
  				
  				lines.add(sCurrentLine);
  				
  			}
   
  		} catch (IOException e) {
  			
  			e.printStackTrace();
  		
  		} finally {
  		
  			try {
  			
  				if (br != null) br.close();
  			
  			} catch (IOException ex) {
  			
  				ex.printStackTrace();
  			
  			}
  		
  		}
  		
  		return lines;
  		
	}
	
	/**
	 * @param url
	 * @param content
	 */
	public static void writeToFile(String url, String content) {
		
		FileWriter writer = null;
		
		try {
			
			writer = new FileWriter(url, false);
		
		} catch (IOException e) { 
			
			e.printStackTrace();
			
		}
		
		try {
	
			writer.append(content);
	
		    writer.flush();
		
		} catch(IOException e) {
			
			System.err.println(e.getMessage());
		
		}
		
	}
	
	/**
	 * @param writer
	 * @param content
	 */
	public static void writeToFile(FileWriter writer, String content) {
		
		try {
	
			writer.append(content);
	
		    writer.flush();
		
		} catch(IOException e) {
			
			System.err.println(e.getMessage());
			
		}
		
	}
	
	/**
	 * @param stringArrayFormat
	 * @param regexpStr
	 * @return
	 */
	public static ArrayList<Pair<String, String>> stringToArray(String stringArrayFormat, String regexpStr) {
    	
    	ArrayList<Pair<String, String>> seekers = new ArrayList<Pair<String, String>>();

        Pattern regexp = Pattern.compile(regexpStr);
        Matcher matcher = regexp.matcher(stringArrayFormat);
        
        while (matcher.find()) {
        
        	MatchResult result = matcher.toMatchResult();

            String type = result.group(2);
            String number = result.group(3);

            Pair<String, String> seeker = new Pair<String, String>(type, number);
            seekers.add(seeker);
        
        }
        
        return seekers;
    	
    }
	
	/**
	 * 
	 */
	public static boolean DEBUG = true;

	/**
	 * @param speaker
	 * @param message
	 */
	public static void talk(String speaker, String message) {
		
		if (DEBUG) System.out.println(speaker + ": " + message);
		
	}
	
	/**
	 * @param inputStr
	 * @param patternStr
	 * @return
	 */
	public static int startIndexOf(CharSequence inputStr, String patternStr) {
		
	    Pattern pattern = Pattern.compile(patternStr);
	    
	    Matcher matcher = pattern.matcher(inputStr);
	    
	    if( matcher.find() ){

	    	return matcher.start();
	    
	    }
	    
	    return -1;
		
	}
	
	/**
	 * @param inputStr
	 * @param patternStr
	 * @return
	 */
	public static int endIndexOf(CharSequence inputStr, String patternStr) {
		
	    Pattern pattern = Pattern.compile(patternStr);
	    
	    Matcher matcher = pattern.matcher(inputStr);
	    
	    if( matcher.find() ){

	    	return matcher.end();
	    
	    }
	    
	    return -1;
		
	}

}
