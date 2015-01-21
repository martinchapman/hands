package Utility.output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.jfree.ui.RefineryUtilities;

import Utility.Runner;
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
		
		ArrayList<Path> files = listFilesForFolder(new File(FILEPREFIX + "data"));
		
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
				
				String topology = "";
				
				for ( String parameter : parameters.split(" ") ) {
					
					String[] keyAndValue = parameter.split(",");
					
					if ( keyAndValue[0].replace("{", "").equals("Topology") ) {
						
						topology = keyAndValue[1].replace("}", "");
						
					}
					
				}
				
				for ( String line : lines ) {
				
					String lastHider = "";
					
					String lastSeeker = "";
					
					String lastTraverser = "";
					
					String lastAttribute = "";
					
					String gameOrRound = "";
					
					for ( String word : line.split(",")) {
						
						word = word.trim();
						
						try {
							
							Double value = Double.parseDouble(word);
							
							if ( lastTraverser.equals("hider") ) {
							
								hiderRecords.get(hiderRecords.indexOf(new HiderRecord(lastHider))).attribute(gameOrRound, lastAttribute, value);
								
							} else if ( lastTraverser.equals("seeker") ) {
							
								hiderRecords.get(hiderRecords.indexOf(new HiderRecord(lastHider))).getSeeker(lastSeeker).attribute(gameOrRound, lastAttribute, value);
								
							}

						} catch (NumberFormatException e) { 
						
							// If we come across an entry for a Hider
							if ( word.charAt(0) == 'h') {
							
								// Mixing, and first time round
								if ( parameters.contains("{MixHiders,true}") ) {
									
									// If mixing, only ever one hide record, for all hiders
									if ( (hiderRecords.size() == 0) ) {
									
										hiderRecords.add(new HiderRecord(path, "MixedHiderStrats"));
										
										hiderRecords.get(hiderRecords.size() - 1 ).setTopology(topology);
										
										hiderRecords.get(hiderRecords.size() - 1 ).setParameters(parameters);
										
									
									}
									
									lastHider = hiderRecords.get(0).getTraverser();
									
								} else {
									
									// If we do not yet have a record for this hider
									if (!hiderRecords.contains(new TraverserRecord(word))) {
										
										// Create it
										hiderRecords.add(new HiderRecord(path, word));
										
										hiderRecords.get(hiderRecords.size() - 1 ).setTopology(topology);
										
										hiderRecords.get(hiderRecords.size() - 1 ).setParameters(parameters);
										
									}
									
									lastHider = word;
									
								}
								
								lastTraverser = "hider";
								
							// If we come across an entry for a Seeker
							} else if ( word.charAt(0) == 's' ) {
								
								if ( parameters.contains("{MixSeekers,true}") ) { 
									
									if ( hiderRecords.get(hiderRecords.indexOf(new HiderRecord(lastHider))).getSeekersAndAttributes().size() == 0 ) {
										
										hiderRecords.get(hiderRecords.indexOf(new HiderRecord(lastHider))).addSeeker(new TraverserRecord("MixedSeekerStrats"));
										
										hiderRecords.get(hiderRecords.indexOf(new HiderRecord(lastHider))).getSeeker("MixedSeekerStrats").setTopology(topology);
									
									}
									
									lastSeeker = hiderRecords.get(hiderRecords.indexOf(new HiderRecord(lastHider))).getSeeker("MixedSeekerStrats").getTraverser();
									
								} else {
									
									// If the last hider doesn't have a record of this seeker, add it
									if (!hiderRecords.get(hiderRecords.indexOf(new HiderRecord(lastHider))).containsSeeker(new TraverserRecord(word))) {
											
										hiderRecords.get(hiderRecords.indexOf(new HiderRecord(lastHider))).addSeeker(new TraverserRecord(word));
										
										hiderRecords.get(hiderRecords.indexOf(new HiderRecord(lastHider))).getSeeker(word).setTopology(topology);
									
									}
									
									lastSeeker = word;
									
								}
								
								lastTraverser = "seeker";
							
							} else if ( word.equals("G") ) {
							
								gameOrRound = "Game";
								
							} else if ( word.equals("R") ) {
								
								gameOrRound = "Round";
								
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
	public void showLineGraphForAttribute(ArrayList<TraverserRecord> traversers, String gameOrRound, String title, String attribute) {
		
		if ( gameOrRound.equals("Game") ) {
			
			showGraphForAttribute(traversers, gameOrRound, title, "Line", "Game Number", attribute, "");
			
		} else if ( gameOrRound.equals("Round") ) {
			
			showGraphForAttribute(traversers, gameOrRound, title, "Line", "Round Number", attribute, "");
			
		}
		
	}

	/**
	 * @param traversers
	 * @param title
	 * @param attribute
	 */
	public void showBarGraphForAttribute(ArrayList<TraverserRecord> traversers, String gameOrRound, String title, String attribute, String category) {
		
		showGraphForAttribute(traversers, gameOrRound, title, "Bar", "Game Number", attribute, category);
		
	}
	
	/**
	 * @param traverserRecords
	 * @param title
	 * @param graphType
	 * @param attribute
	 */
	public void showGraphForAttribute(ArrayList<TraverserRecord> traverserRecords, String gameOrRound, String title, String graphType, String xLabel, String yLabel, String category) {
		
		TraverserGraph graph = null;
		
		if ( title.length() > 200 ) title = title.substring(0, 200);
		
		if (graphType.equals("Line")) {
		
			graph = new LineGraph(title);
			
			System.out.println(traverserRecords);
			
			//int j = 0;
			
			for ( TraverserRecord traverser : traverserRecords ) {
				
				ArrayList<Double> attributeToValues = new ArrayList<Double>();
				
				if ( gameOrRound.equals("Game") ) {
				
					//~MDC TEMP!
					//double lastCost = 0;
					//double lastSeekerCost = 0;
					//int i = 0;
					
					for ( Entry<AttributeSetIdentifier, Hashtable<String,Double>> series : traverser.getGameSeries() ) {
						
						/*if ( lastCost == 0 ) {
							
							attributeToValues.add( 100.0 - 100.0 );
							
							lastCost = series.getValue().get("Cost");
							lastSeekerCost = Runner.selectedSeekers.get(j).getGameSeries().get(0).getValue().get("Cost");
							
						} else {
							
							System.out.println( traverser + 
											   " | Hider Cost: " + series.getValue().get("Cost") + " | As Percentage: " + ( ( series.getValue().get("Cost") / lastCost ) * 100 ) +
											   " | Seeker Cost: " +  Runner.selectedSeekers.get(j).getGameSeries().get(i).getValue().get("Cost")  + " | As Percentage: " + ( ( Runner.selectedSeekers.get(j).getGameSeries().get(i).getValue().get("Cost") / lastSeekerCost ) * 100 ) );
							
							attributeToValues.add( 
							    ( 
							    	( 
							    		Runner.selectedSeekers.get(j).getGameSeries().get(i).getValue().get("Cost") / lastSeekerCost 
							    	) * 100 
							    ) 
							    - 
							    ( 
							    	( 
							    		series.getValue().get("Cost") / lastCost 
							    	) * 100 
							    ) 
							);
							
						}
						
						i++;*/
						
						attributeToValues.add( series.getValue().get(yLabel) );
						
					}
					
					//j++;
				
				} else if ( gameOrRound.equals("Round") ) {
					
					for ( Entry<Integer, Hashtable<String,Double>> series : traverser.getRoundSeries() ) {
						
						attributeToValues.add( series.getValue().get(yLabel) );
						
					}
					
				}
				
				((LineGraph) graph).addDataset(traverser.getTraverser(), attributeToValues);
				
			}
			
		} else if (graphType.equals("Bar")) {
			
			graph = new BarGraph(title);
			
			Hashtable<String, Double> traverserToAverageForAttribute = new Hashtable<String, Double>();
			
			ArrayList<String> hiderNames = new ArrayList<String>();
			
			int j = 0;
			
			LinkedHashMap<Double, Double> hiderToSeeker = new LinkedHashMap<Double, Double>();
			
			double maxHider = Double.MIN_VALUE;
			double maxSeeker = Double.MIN_VALUE;
			
			String globalCategory = "";
			
			for ( TraverserRecord traverser : traverserRecords ) {
				
				hiderNames.add(traverser.toString());
				
				String localCategory = "";
				
				if ( category.equals("Topology") ) {
				
					localCategory = traverser.getTopology();
					
				} else if ( category.equals("Player") ) {
					
					localCategory = traverser.getCategory();
					
				}
				
				globalCategory = localCategory;
				
				if ( traverser.getAverageGameAttributeValue("Cost") > maxHider ) {
					
					maxHider = traverser.getAverageGameAttributeValue("Cost");
					
				}
				
				// ~MDC Will fail with multiple Seekers (hence temporary)
				if ( Runner.selectedSeekers.get(j).getAverageGameAttributeValue("Cost") > maxSeeker ) {
					
					maxSeeker = Runner.selectedSeekers.get(j).getAverageGameAttributeValue("Cost");
					
				}
				
				hiderToSeeker.put(traverser.getAverageGameAttributeValue("Cost"), Runner.selectedSeekers.get(j).getAverageGameAttributeValue("Cost"));
				
				/*int i = 0;
				
				Dataset cumulativeHiderCost = new Dataset();
				Dataset cumulativeSeekerCost = new Dataset();
					
				for ( Entry<AttributeSetIdentifier, Hashtable<String,Double>> series : traverser.getGameSeries() ) {
					
					cumulativeHiderCost.addItemToDataset( series.getValue().get("Cost") );
					cumulativeSeekerCost.addItemToDataset( Runner.selectedSeekers.get(j).getGameSeries().get(i).getValue().get("Cost") );
					
					i++;
					
				}
				
				//System.out.println("Hider mean: " + cumulativeHiderCost.getMean());
				//System.out.println("Seeker mean: " + cumulativeSeekerCost.getMean());
				
				//System.out.println("Hider std dev: " + cumulativeHiderCost.getStdDev());
				//System.out.println("Seeker std dev: " + cumulativeSeekerCost.getStdDev());
				
				
				
				double cumulativeStandardHiderCost = 0.0;
				
				for ( double hiderCost : cumulativeHiderCost.getDataset() ) {
					
					//System.out.println(hiderCost + " " + (( hiderCost - cumulativeHiderCost.getMean() ) / cumulativeHiderCost.getStdDev()));
					
					cumulativeStandardHiderCost += (( hiderCost - cumulativeHiderCost.getMean() ) / cumulativeHiderCost.getStdDev());
					
					//System.out.println("Cumulative Hider: " + cumulativeStandardHiderCost);
					
				}
				
				double cumulativeStandardSeekerCost = 0.0;
				
				for ( double seekerCost : cumulativeSeekerCost.getDataset() ) {
					
					cumulativeStandardSeekerCost += (( seekerCost - cumulativeSeekerCost.getMean() ) / cumulativeSeekerCost.getStdDev());
					
				}
				
				System.out.println((cumulativeStandardSeekerCost / cumulativeSeekerCost.getDataset().size()) - 
								   (cumulativeStandardHiderCost / cumulativeHiderCost.getDataset().size()));
				
				((BarGraph) graph).addBar(cumulativeStandardSeekerCost - cumulativeStandardHiderCost, traverser.toString(), localCategory);
				*/
				
				
				//((BarGraph) graph).addBar(traverser.getRelativisedAverageGameAttributeValue(yLabel), traverser.toString(), localCategory);
				
				((BarGraph) graph).addBar(traverser.getAverageGameAttributeValue(yLabel), traverser.toString(), localCategory);
				
				
				
				j++;
				
			}
			
			// ~MDC Following is temporary:
			int k = 0;
			
			System.out.println("Max Seeker value: " + maxSeeker);
			System.out.println("Max Hider value: " + maxHider);
			
			for ( Entry<Double, Double> entry : hiderToSeeker.entrySet()) {
				
				System.out.println(hiderNames.get(k) + " " + entry);
				
				System.out.println("New values ( Hider then seeker) : " + ((entry.getKey() / maxHider) * 100) + " " + ((entry.getValue() / maxSeeker) * 100));
				
				// ~MDC Uncomment for overall score
				//((BarGraph) graph).addBar(((entry.getValue() / maxSeeker) * 100) - ((entry.getKey() / maxHider) * 100), hiderNames.get(k), globalCategory);
				
				k++;
				
			}
			
		}
		
		String filename = "figure" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		
		graph.createChart(filename + " " + title, xLabel, yLabel);
		
		graph.exportChartAsEPS(Utils.FILEPREFIX + "data/charts/" + filename + ".eps");
		
		try {
		
			Utils.writeToFile(new FileWriter(Utils.FILEPREFIX + "/data/charts/figures.bib", true), "\n @FIG{" + filename + ", main = {}, add = { " + title + " }, file = {/Users/Martin/Dropbox/workspace/SearchGames/output/data/charts/" + filename + "}, source = {}}");
		
		} catch (IOException e) {
			
			e.printStackTrace();
		
		}
		
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
				
				returner += "\n" + hiderRecord.getTopology();
				
				returner += "\n" + hiderRecord.printStats();
				
			}
			
		}
		
		return returner;
		
	}
	
	/**
	 * 
	 */
	public void removeAllOutputFiles() {
		
		for ( Path path : listFilesForFolder(new File(FILEPREFIX + "/data")) ) deleteFile(path);
		
		for ( Path path : listFilesForFolder(new File(FILEPREFIX + "/data/js/data")) ) deleteFile(path);
		
	}
	
	/**
	 * Remove relating .js and .html files if .csv have been removed
	 */
	public void removeOrphaned() {
		
		ArrayList<String> CSVIDs = new ArrayList<String>();
		
		for ( Path path : listFilesForFolder(new File(FILEPREFIX + "/data")) ) { 
			
			// If this is a .csv file, track its ID
			if ( path.toString().substring(path.toString().lastIndexOf('.'), path.toString().length()).equals(".csv")) {
				
				CSVIDs.add(path.toString().substring(path.toString().lastIndexOf('/') + 1, path.toString().lastIndexOf('.')));
			
			}
		    
		}
		
		// For all .html files, if no corresponding .csv ID recorded, remove.
		for ( Path path : listFilesForFolder(new File(FILEPREFIX + "/data")) ) { 
			
			if ( path.toString().contains("-") && !CSVIDs.contains( path.toString().substring(path.toString().lastIndexOf('/') + 1, path.toString().lastIndexOf('-')) )) {
				
				deleteFile(path);
				
			}
		
		}
		
		// Similar for .js files
		for ( Path path : listFilesForFolder(new File(FILEPREFIX + "/data/js/data")) ) {
			
			if ( path.toString().contains("-") && !CSVIDs.contains( path.toString().substring(path.toString().lastIndexOf('/') + 1, path.toString().lastIndexOf('-')) )) {
				
				deleteFile(path);
				
			}
			
		}
		
		
	}
	
	/**
	 * 
	 */
	private final static String FILEPREFIX = "Output/";
	
	/**
	 * @param path
	 */
	public void deleteFile(Path path) {
		
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