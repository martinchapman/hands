package Utility;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jfree.chart.JFreeChart;
import org.jgrapht.EdgeFactory;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jibble.epsgraphics.EpsGraphics2D;

import HideAndSeek.GraphTraverser;
import HideAndSeek.graph.HiddenObjectGraph;

/**
 * A set of static utility methods.
 * 
 * @author Martin
 *
 */
public class Utils {

	/**
	 * 
	 */
	public final static String FILEPREFIX = "output/";
	
	/**
	 * 
	 */
	public static boolean DEBUG = true;
	
	/**
	 * 
	 */
	public final static void runCommand(String command) {
		
		Process proc = null;
		
		try {
			
			proc = Runtime.getRuntime().exec(command);
		
		} catch (IOException e1) {
			
			e1.printStackTrace();
		
		}
		
		BufferedReader outputs = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		
		BufferedReader errors = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
		  
		String line = null;  
		 
		try {
			
			while ((line = outputs.readLine()) != null) {  
			
				System.out.println(line);  
			
			}
			
		} catch (IOException e1) {
			
			e1.printStackTrace();
		
		}
		
		try {
		
			while ((line = errors.readLine()) != null) {  
				
				System.out.println(line);  
			
			}
			
		} catch (IOException e1) {
			
			e1.printStackTrace();
		
		} 
		  
		try {
		
			proc.waitFor();
		
		} catch (InterruptedException e) { System.out.println(e); }
	    
	}
	
	/**
	 * @param traverser
	 * @return
	 */
	public static String shortenTraverserName(GraphTraverser traverser) {
		
		if (traverser.toString().contains(" ")) { 
			
			return traverser.toString().substring(0, traverser.toString().indexOf(" "));
		
		} else {
			
			return traverser.toString();
			
		}
		
	}
	
	/**
	 * @param map
	 * @return
	 */
	public static <A, B> LinkedHashMap<A,B> manualReverse(LinkedHashMap<A, B> map) {
	
		List<Entry<A,B>> list = new ArrayList<Entry<A, B>>(map.entrySet());
	
		map.clear();
		
		for ( int i = list.size() - 1; i >= 0; i-- ){
			
		    map.put(list.get(i).getKey(), list.get(i).getValue());
		
		}
		
		return map;
	
	}

	/**
	 * 
	 */
	public static void printSystemStats() {
		
		System.out.print("\r[");
		
		 /* Total number of processors or cores available to the JVM */
	    // System.out.println("Available processors (cores): " +  Runtime.getRuntime().availableProcessors());

	    /* Total amount of free memory available to the JVM */
	    System.out.print("Free memory (bytes): " + Runtime.getRuntime().freeMemory());

	    /* This will return Long.MAX_VALUE if there is no preset limit */
	    // long maxMemory = Runtime.getRuntime().maxMemory();
	    /* Maximum amount of memory the JVM will attempt to use */
	    // System.out.println("Maximum memory (bytes): " + (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));

	    /* Total memory currently available to the JVM */
	    // System.out.println("Total memory available to JVM (bytes): " + Runtime.getRuntime().totalMemory());

	    /* Get a list of all filesystem roots on this system */
	    // File[] roots = File.listRoots();

	    /* For each filesystem root, print some info */
	    /* for (File root : roots) {
	      System.out.println("File system root: " + root.getAbsolutePath());
	      System.out.println("Total space (bytes): " + root.getTotalSpace());
	      System.out.println("Free space (bytes): " + root.getFreeSpace());
	      System.out.println("Usable space (bytes): " + root.getUsableSpace());
	    }*/
	    
	    System.out.print("]");
		
	}
	/**
	 * /http://stackoverflow.com/questions/8119366/sorting-hashmap-by-values
	 * 
	 * @param unsortMap
	 * @param order
	 * @return
	 */
	public static <K> Map<K, Double> sortByValue(Map<K, Double> unsortMap, final boolean order) {

        List<Entry<K, Double>> list = new LinkedList<Entry<K, Double>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<K, Double>>() {
        	
            public int compare(Entry<K, Double> o1, Entry<K, Double> o2) {
            	
                if (order) {
                
                	return o1.getValue().compareTo(o2.getValue());
                
                } else {
                    
                	return o2.getValue().compareTo(o1.getValue());

                }
                
            }
            
        });

        // Maintaining insertion order with the help of LinkedList
        Map<K, Double> sortedMap = new LinkedHashMap<K, Double>();
        
        for (Entry<K, Double> entry : list) {
        	
            sortedMap.put(entry.getKey(), entry.getValue());
        
        }

        return sortedMap;
        
    }
	
	/**
	 * @param filePath
	 * @param ftpUrl
	 * @param user
	 * @param pass
	 * @param host
	 * @param uploadPath
	 */
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
	
	/**
	 * Although an existing diameter value can be obtained from the
	 * FWSP, this is in terms of edge weights. This finds the number
	 * of vertices in the greatest path, as an idea of the number
	 * of hops to achieve the max diameter.
	 * 
	 * Could return a rough estimate, for slightly less clear information.
	 * @param <V>
	 * 
	 * @return
	 */
	public static <V, E extends DefaultWeightedEdge> int graphDiameter(final HiddenObjectGraph<V, E> graph) {
		
		// ~MDC 5/4 Over-complicated as an exercise
		HiddenObjectGraph<V, E> localGraph = new HiddenObjectGraph<V, E>(new EdgeFactory<V, E>() {

			@Override
			public E createEdge(V arg0, V arg1) {

				return graph.getEdgeFactory().createEdge(arg0, arg1);
			
			}
			
		});
		
		// Update the local graph from the current node as the Seeker moves
		for ( V sourceVertex : graph.vertexSet() ) {
			
			for ( V targetVertex : graph.vertexSet() ) {
				
				if ( graph.containsEdge(sourceVertex, targetVertex) ) {
					
					localGraph.addVertexIfNonExistent(sourceVertex);
					localGraph.addVertexIfNonExistent(targetVertex);
					
					localGraph.addEdgeWithWeight(sourceVertex, targetVertex, graph.getEdgeWeight(graph.getEdge(sourceVertex, targetVertex)));
					
				}
				
			}
			
		}
				
		FloydWarshallShortestPaths<V, E> FWSP = new FloydWarshallShortestPaths<V, E>(localGraph);
	
		for (GraphPath<V, E> GP : FWSP.getShortestPaths()) {
			
			// Return the length of the path with the greatest weight
			if (GP.getWeight() == FWSP.getDiameter()) return GP.getEdgeList().size();
			
		}
		
		return -1;
		
	}
	
	/**
	 * Given a map, where the value requires instantiation if
	 * currently empty, then this will instantiate, otherwise
	 * add.
	 * 
	 * @param table
	 * @param key
	 * @param value
	 * @param emptyInstance
	 */
	public static <K, V, E extends AbstractCollection<V>> void add( Hashtable<K, E> table, K key, V value, E emptyInstance, boolean unique ) {
		
		if ( table.containsKey(key) ) {
			
			if ( unique ) {
				
				if ( !table.get(key).contains(value) ) table.get(key).add(value);
				
			} else {
				
				table.get(key).add(value);
				
			}
			
		} else {
			
			emptyInstance.add(value);
			
			table.put(key, emptyInstance);
			
		}
		
	}
	
	/**
	 * All possible combinations from N lists.
	 * 
	 * @param lists
	 * @return
	 */
	public static <V> ArrayList<ArrayList<V>> combinations(ArrayList<ArrayList<V>> lists) {
		
		int[] indices = new int[lists.size()];
		
		int maxListSize = Integer.MIN_VALUE;
		
		for ( ArrayList<V> list : lists ) {
			
			if ( list.size() > maxListSize ) maxListSize = list.size();
			
		}
		
		ArrayList<ArrayList<V>> combinations = new ArrayList<ArrayList<V>>();
		
		do {
		    
			ArrayList<V> combination = new ArrayList<V>();
			
			for ( int i = 0; i < indices.length; i++ ) {
				
				int index = indices[i];
				
				if ( index >= lists.get(i).size() ) index = lists.get(i).size() - 1;
				
				combination.add(lists.get(i).get(index));
				
			}
			
			if ( !combinations.contains(combination) ) combinations.add(combination);
			
			Utils.advanceIndices( indices, lists.size(), maxListSize );
	        
		} while ( !Utils.allMaxed( indices, lists.size(), maxListSize  ) );
		
		return combinations;
		
	}
	
	/**
	 * Ancillary method for combinations
	 * 
	 * @param indices
	 * @param n
	 * @param max
	 */
	public static void advanceIndices( int[] indices, int n, int max ) {

        for ( int i = n - 1; i >= 0; i-- ) {
        	
            if ( indices[i] + 1 == max ) {
            
            	indices[i] = 0;
            	
            } else {
            
            	indices[i]++;
            	
            	break;
            
            }
            
        }

    }
	
	/**
	 * Ancillary method for combiantions
	 * 
	 * @param indices
	 * @param n
	 * @param max
	 * @return
	 */
	public static boolean allMaxed( int[] indices, int n, int max ) {

        for ( int i = n - 1; i >= 0; i-- ) {
        	
            if ( indices[i] != max - 1 ) {
            
            	return false;
            
            }
        
        }
        
        return true;

    }

}
