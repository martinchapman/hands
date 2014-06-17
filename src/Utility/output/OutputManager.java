package Utility.output;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;

import org.jfree.ui.RefineryUtilities;

import Utility.Utils;

/**
 * @author Martin
 *
 */
public class OutputManager {
	
	/**
	 * Multiple hiders per file, and multiple files.
	 */
	private ArrayList<ArrayList<HiderRecord>> fileHiderRecords;
	
	/**
	 * @return
	 */
	public ArrayList<ArrayList<HiderRecord>> getFileHiderRecords() {
		
		return fileHiderRecords;
		
	}
	
	/**
	 * 
	 */
	public void processOutput() {
		
		fileHiderRecords = new ArrayList<ArrayList<HiderRecord>>();
		
		ArrayList<Path> files = listFilesForFolder(new File(FILEPREFIX + "/Data"));
		
		if (files.size() == 0) {
			
			return;
			
		}

		// For each file in the directory
		for ( Path path : files ) {
			
			// If it is a .csv file (what we want):
			if (path.toString().substring(path.toString().length() - 3, path.toString().length()).equals("csv")) {
				
				ArrayList<HiderRecord> hiderRecords = new ArrayList<HiderRecord>();
				
				ArrayList<String> lines = Utils.readFromFile(path.toString());
				
				String parameters = lines.remove(0);
				
				for ( String line : lines ) {
				
					String lastHider = "";
					
					String lastSeeker = "";
					
					String lastTraverser = "";
					
					String lastAttribute = "";
					
					for ( String word : line.split(",")) {
						
						word = word.trim();
						
						try {
							
							Double value = Double.parseDouble(word);
							
							if ( lastTraverser.equals("hider") ) {
							
								hiderRecords.get(hiderRecords.indexOf(new HiderRecord(lastHider))).attribute(lastAttribute, value);
								
							} else if ( lastTraverser.equals("seeker") ) {
							
								hiderRecords.get(hiderRecords.indexOf(new HiderRecord(lastHider))).getSeeker(lastSeeker).attribute(lastAttribute, value);
								
							}

						} catch (NumberFormatException e) { 
						
							// If we come across an entry for a Hider
							if ( word.charAt(0) == 'h') {
							
								// If we do not yet have a record for this hider
								if (!hiderRecords.contains(new TraverserRecord(word))) {
								
									// Create it
									hiderRecords.add(new HiderRecord(word));
									
									hiderRecords.get(hiderRecords.size() - 1 ).setParameters(parameters);
								
								}
								
								lastHider = word;
								
								lastTraverser = "hider";
								
							// If we come across an entry for a Seeker
							} else if ( word.charAt(0) == 's') {
							
								// If the last hider doesn't have a record of this seeker, add it
								if (!hiderRecords.get(hiderRecords.indexOf(new HiderRecord(lastHider))).containsSeeker(new TraverserRecord(word))) {
									
									hiderRecords.get(hiderRecords.indexOf(new HiderRecord(lastHider))).addSeeker(new TraverserRecord(word));
									
								}
								
								lastSeeker = word;
								
								lastTraverser = "seeker";
							
							// If we come across an attribute entry
							} else {
							
								lastAttribute = word;
								
							}
						
						}
						
					}
					
				}
				
				fileHiderRecords.add(hiderRecords);
				
			} // End of CSV If statement
			 
		} // End of file loop
		
	}
	
	/**
	 * @param traversers
	 * @param title
	 * @param attribute
	 */
	public void showLineGraphForAttribute(ArrayList<TraverserRecord> traversers, String title, String attribute) {
		
		showGraphForAttribute(traversers, title, "Line", attribute);
		
	}

	/**
	 * @param traversers
	 * @param title
	 * @param attribute
	 */
	public void showBarGraphForAttribute(ArrayList<TraverserRecord> traversers, String title, String attribute) {
		
		showGraphForAttribute(traversers, title, "Bar", attribute);
		
	}
	
	/**
	 * @param traverserRecords
	 * @param title
	 * @param graphType
	 * @param attribute
	 */
	public void showGraphForAttribute(ArrayList<TraverserRecord> traverserRecords, String title, String graphType, String attribute) {
		
		TraverserGraph graph = null;
		
		String xLabel = "";
		
		String yLabel = "";
		
		if (graphType.equals("Line")) {
		
			graph = new LineGraph(title);
			
			for ( TraverserRecord traverser : traverserRecords ) {
				
				ArrayList<Double> attributeToValues = new ArrayList<Double>();
				
				for ( Entry<Integer, Hashtable<String,Double>> series : traverser.getSeries() ) {
					
					attributeToValues.add( series.getValue().get(attribute) );
					
				}
				
				((LineGraph) graph).addDataset(traverser.getTraverser(), attributeToValues);
				
			}
			
			xLabel = "Game Number";
			
			yLabel = attribute;
		
		} else if (graphType.equals("Bar")) {
			
			graph = new BarGraph(title);
			
			Hashtable<String, Double> traverserToAverageForAttribute = new Hashtable<String, Double>();
			
			for ( TraverserRecord traverser : traverserRecords ) {
				
				((BarGraph) graph).addBar(traverser.toString(), traverser.getAverageAttributeValue(attribute), attribute);
			
			}
			
		}
		
		graph.createChart(title, xLabel, yLabel);
		
		graph.explortChartAsEPS(Utils.FILEPREFIX + "Data/charts/" + title + ".eps");
		
		graph.pack();
		
		RefineryUtilities.centerFrameOnScreen(graph);
		 
		graph.setVisible(true);
		
	}
	
	
	/**
	 * @return
	 */
	public String printAllStats() {
		
		String returner = "";
		
		for ( ArrayList<HiderRecord> hiderRecords : fileHiderRecords ) {
			
			for ( HiderRecord hiderRecord : hiderRecords ) {
				
				returner += "\n" + hiderRecord.getParameters();
				
				returner += "\n" + hiderRecord.printStats();
				
			}
			
		}
		
		return returner;
		
	}
	
	/**
	 * 
	 */
	public void removeAllOutputFiles() {
		
		for ( Path path : listFilesForFolder(new File(FILEPREFIX + "/Data")) ) deleteFile(path);
		
		for ( Path path : listFilesForFolder(new File(FILEPREFIX + "/Data/js/Data")) ) deleteFile(path);
		
	}
	
	/**
	 * 
	 */
	private final static String FILEPREFIX = "Output/";
	
	/**
	 * @param path
	 */
	private void deleteFile(Path path) {
		
		try {
		    
			Files.delete(path);
		    
		} catch (NoSuchFileException x) {
		    
			System.err.format("%s: no such" + " file or directory%n", path);
		
		} catch (DirectoryNotEmptyException x) {
		
			System.err.format("%s not empty%n", path);
		
		} catch (IOException x) {
		
			// File permission problems are caught here.
		    System.err.println(x);
		}
	
	}
	
	/**
	 * @param folder
	 */
	private ArrayList<Path> listFilesForFolder(final File folder) {
		
		ArrayList<Path> files = new ArrayList<Path>();
		
	    for (final File fileEntry : folder.listFiles()) {
	    	
	        if (!fileEntry.isDirectory()) {
	        
	        	files.add(Paths.get(fileEntry.getAbsolutePath()));
	        
	        }
	        
	    }
	    
	    return files;
	    
	}

	
	
}