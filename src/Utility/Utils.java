package Utility;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jfree.chart.JFreeChart;
import org.jibble.epsgraphics.EpsGraphics2D;

public class Utils {

	public final static String FILEPREFIX = "output/";
	
	/**
	 * 
	 */
	public static boolean DEBUG = true;
	
	// http://stackoverflow.com/questions/8119366/sorting-hashmap-by-values
	public static <K> Map<K, Double> sortByComparator(Map<K, Double> unsortMap, final boolean order)
    {

        List<Entry<K, Double>> list = new LinkedList<Entry<K, Double>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<K, Double>>()
        {
            public int compare(Entry<K, Double> o1,
                    Entry<K, Double> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<K, Double> sortedMap = new LinkedHashMap<K, Double>();
        for (Entry<K, Double> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
	
	public static void uploadToFTP(String filePath, String ftpUrl, String user, String pass, String host, String uploadPath) {
		
		ftpUrl = String.format(ftpUrl, user, pass, host, uploadPath);
 
        try {
        	
            URL url = new URL(ftpUrl);
            
            URLConnection conn = url.openConnection();
            
            OutputStream outputStream = conn.getOutputStream();
            
            FileInputStream inputStream = new FileInputStream(filePath);
 
            byte[] buffer = new byte[4096];
            
            int bytesRead = -1;
            
            while ((bytesRead = inputStream.read(buffer)) != -1) {
            
            	outputStream.write(buffer, 0, bytesRead);
            
            }
 
            inputStream.close();
            
            outputStream.close();
            
        } catch (IOException ex) {
            
        	ex.printStackTrace();
        
        }
        
	}
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
	 * @param name
	 * @param chart
	 * @param x
	 * @param y
	 */
	public static void exportAsEPS(String url, JFreeChart chart, int x, int y) {
		
        Graphics2D g = new EpsGraphics2D();
        
        chart.setTitle("");
        
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        
        chart.draw(g, new Rectangle(x,y));
        
        FileWriter writer = null;
		
		try {
			
			writer = new FileWriter(url, false);
		
		} catch (IOException e) { 
			
			e.printStackTrace();
			
		}
		
		try {
	
			writer.write(g.toString());
	
		    writer.flush();
		    
		    writer.close();
		
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
