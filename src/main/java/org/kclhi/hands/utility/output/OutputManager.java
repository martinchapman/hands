package org.kclhi.hands.utility.output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.TreeMap;

import org.kclhi.hands.Success;
import org.kclhi.hands.seeker.Seeker;
import org.kclhi.hands.utility.ComparatorResult;
import org.kclhi.hands.utility.Metric;
import org.kclhi.hands.utility.Pair;
import org.kclhi.hands.utility.TraverserDataset;
import org.kclhi.hands.utility.Utils;
import org.kclhi.hands.utility.output.gnuplot.GNU3DCollection;
import org.kclhi.hands.utility.output.gnuplot.GNUBarGraph;
import org.kclhi.hands.utility.output.gnuplot.GNUGraph;
import org.kclhi.hands.utility.output.gnuplot.GNULineGraph;
import org.kclhi.hands.utility.output.gnuplot.GraphType;
import org.json.JSONException;
import org.json.JSONObject;

/**
* @author Martin
*/
public class OutputManager {
  
  /**
  * 
  */
  //private static final boolean OUTPUT_ENABLED = true;
  
  /**
  * 
  */
  protected boolean SHOW_OPPONENT = true;
  
  /**
  * 
  */
  protected boolean textBased = false;
  
  /**
  * Whether to regularly encourage garbage collection
  */
  protected static boolean GARBAGE = false;
  
  /**
  * @param textBased
  */
  public void setTextBased(boolean textBased) {
    
    this.textBased = textBased;
    
  }
  
  /**
  * Multiple hiders per file, and multiple files.
  * (was ArrayList<ArrayList<HiderRecord>>)
  */
  private ArrayList<ArrayList<HiderRecord>> cache;
  
  /**
  * @return
  */
  public ArrayList<ArrayList<HiderRecord>> getCache() {
    
    return cache;
    
  }
  
  /**
  * 
  */
  private String dataInput;

  /*
   * 
   */
  private JSONObject plugin = null;

  /**
  * 
  */
  public OutputManager(String dataInput, boolean recursiveSub) {
    
    this.dataInput = dataInput;
    
    this.recursiveSub = recursiveSub;
    
    cache = new ArrayList<ArrayList<HiderRecord>>();

    plugin = Utils.getPlugin();
    
  }
  
  private boolean recursiveSub = false;
  
  /**
  * @return
  */
  public ArrayList<Datafile> availableFiles() {
    
    ArrayList<Path> files = new ArrayList<Path>();
    
    if ( recursiveSub ) {
      
      files = Utils.listFilesForFolder(new File(FILEPREFIX + dataInput), new ArrayList<Path>());
      
    } else {
      
      files = Utils.listFilesForFolder(new File(FILEPREFIX + dataInput));
      
    }
    
    if ( files.size() == 0 ) files.addAll(Utils.listFilesForFolder(new File(FILEPREFIX + "data-sample")));
    
    ArrayList<Datafile> availableFiles = new ArrayList<Datafile>();
    
    if (files.size() == 0) {
      
      return new ArrayList<Datafile>();
      
    }
    
    // For each file in the directory
    for ( Path path : files ) {
      
      // If it is a .csv file (what we want):
      if (path.toString().substring(path.toString().length() - 3, path.toString().length()).equals("csv")) {
        
        // If it starts with a number + '-', it is part of a collection
        if ( path.toFile().getName().contains("-")) {
          
          // Find existing GroupedDatafile for this in list, and add on (could do this inside GroupedDatafile)
          if ( availableFiles.contains(new GroupedDatafiles(path.toFile().getName().substring(0, path.toFile().getName().indexOf("-")))) ) {
            
            ((GroupedDatafiles) availableFiles.get(availableFiles.indexOf(new GroupedDatafiles(path.toFile().getName().substring(0, path.toFile().getName().indexOf("-")))))).addDatafile(new Datafile(Utils.readFirstLineFromFile(path.toString()), path));
            
          } else {
            
            GroupedDatafiles groupedDatafiles = new GroupedDatafiles(Utils.readFirstLineFromFile(path.toString()), path);
            
            groupedDatafiles.setIdentifier(path.toFile().getName().substring(0, path.toFile().getName().indexOf("-")));
            
            availableFiles.add(groupedDatafiles);
            
          }
          
        } else {
          
          availableFiles.add(new Datafile(Utils.readFirstLineFromFile(path.toString()), path));
          
        }
        
      }
      
    }
    
    return availableFiles;
    
  }
  
  /**
  * 
  */
  public void processAllOutput() {
    
    for ( Datafile datafile : availableFiles() ) {
      
      processOutput(datafile);
      
    }
    
  }
  
  /**
  * @param path
  */
  public void processIndividualOutput(Datafile datafile) {
    
    processOutput(datafile);
    
  }
  
  public void processOutput(Datafile datafile) {
    
    if ( datafile instanceof GroupedDatafiles ) {
      
      ArrayList<HiderRecord> groupedHiderRecords = new ArrayList<HiderRecord>();
      
      ArrayList<ArrayList<HiderRecord>> toBeSplit = new ArrayList<ArrayList<HiderRecord>>();
      
      for ( Datafile file : ((GroupedDatafiles)datafile).getAllDatafiles() ) {
        
        toBeSplit.add(createHiderRecords(file.getPath()));
        
      }
      
      for ( int i = 0; i < toBeSplit.get(0).size(); i++ ) {
        
        groupedHiderRecords.add(new GroupedHiderRecords());
        
      }
      
      for ( ArrayList<HiderRecord> individualRecordList : toBeSplit ) {
        
        for ( HiderRecord individualRecord : individualRecordList ) {
          
          ((GroupedHiderRecords)groupedHiderRecords.get(individualRecordList.indexOf(individualRecord))).addHider(individualRecord);
          
        }
        
      }
      
      cache.add(groupedHiderRecords);
      
    } else {
      
      cache.add(createHiderRecords(datafile.getPath()));
      
    }
    
  }
  
  /**
  * 
  */
  private ArrayList<HiderRecord> createHiderRecords(Path path) {
    
    ArrayList<HiderRecord> hiderRecords = new ArrayList<HiderRecord>();
    
    ArrayList<String> lines = Utils.readFromFile(path.toString());
    
    String parameters = lines.remove(0);
    
    String topology = "";
    
    int rounds = -1;

    double additionalResourceImmunity = 0.0;
    
    for ( String parameter : parameters.split(" ") ) {
      
      String[] keyAndValue = parameter.split(",");
      
      if ( keyAndValue[0].replace("{", "").equals("Topology") ) {
        
        topology = keyAndValue[1].replace("}", "");
        
      } else if ( keyAndValue[0].replace("{", "").equals("Rounds") ) {
        
        rounds = Integer.parseInt(keyAndValue[1].replace("}", ""));
        
      } else if ( keyAndValue[0].replace("{", "").equals("AdditionalResourceImmunity") ) {

        additionalResourceImmunity = Double.parseDouble(keyAndValue[1].replace("}", ""));

      }
      
    }
    
    for ( String line : lines ) {
      
      Utils.printSystemStats("Hider records: " + hiderRecords.size() + " Lines: " + lines.size());
      
      String lastHider = "";
      
      String lastSeeker = "";
      
      String lastTraverser = "";
      
      String lastAttribute = "";
      
      String gameOrRound = "";
      
      for ( String word : line.split(",")) {
        
        Utils.printSystemStats("Hider records: " + hiderRecords.size() + " Lines: " + lines.size());
        
        word = word.trim();
        
        try {
          
          Double value = Double.parseDouble(word);
          
          if ( lastTraverser.equals("hider") ) {
            
            hiderRecords.get(hiderRecords.indexOf(new HiderRecord(lastHider))).attribute(gameOrRound, lastAttribute, value);
            
            if ( GARBAGE ) System.gc();
            
          } else if ( lastTraverser.equals("seeker") ) {
            
            hiderRecords.get(hiderRecords.indexOf(new HiderRecord(lastHider))).getSeeker(lastSeeker).attribute(gameOrRound, lastAttribute, value);
            
            if ( GARBAGE ) System.gc();
            
          }
          
        } catch (NumberFormatException e) { 
          
          // If we come across an entry for a Hider
          if ( word.charAt(0) == 'h') {
            
            // Mixing, and first time round
            if ( parameters.contains("{MixHiders,true}") ) {
              
              // If mixing, only ever one hide record, for all hiders
              if ( (hiderRecords.size() == 0) ) {
                
                hiderRecords.add(new HiderRecord(path, "MixedHiderStrats"));
                
                if ( GARBAGE ) System.gc();
                
                hiderRecords.get(hiderRecords.size() - 1 ).setTopology(topology);
                
                hiderRecords.get(hiderRecords.size() - 1 ).setRounds(rounds);

                hiderRecords.get(hiderRecords.size() - 1 ).setAdditionalResourceImmunity(additionalResourceImmunity);                
                
                hiderRecords.get(hiderRecords.size() - 1 ).setParameters(parameters);
                
                hiderRecords.get(hiderRecords.size() - 1 ).setDatafile(path);
                
              }
              
              lastHider = hiderRecords.get(0).getTraverser();
              
            } else {
              
              // If we do not yet have a record for this hider
              if (!hiderRecords.contains(new TraverserRecord(word))) {
                
                if ( GARBAGE ) System.gc();
                
                // Create it
                hiderRecords.add(new HiderRecord(path, word));
                
                if ( GARBAGE ) System.gc();
                
                hiderRecords.get(hiderRecords.size() - 1 ).setTopology(topology);
                
                hiderRecords.get(hiderRecords.size() - 1 ).setRounds(rounds);

                hiderRecords.get(hiderRecords.size() - 1 ).setAdditionalResourceImmunity(additionalResourceImmunity);
                
                hiderRecords.get(hiderRecords.size() - 1 ).setParameters(parameters);
                
                hiderRecords.get(hiderRecords.size() - 1 ).setDatafile(path);
                
              }
              
              lastHider = word;
              
            }
            
            lastTraverser = "hider";
            
            // If we come across an entry for a Seeker
          } else if ( word.charAt(0) == 's' ) {
            
            if ( parameters.contains("{MixSeekers,true}") ) { 
              
              if ( hiderRecords.get(hiderRecords.indexOf(new HiderRecord(lastHider))).getSeekersAndAttributes().size() == 0 ) {
                
                if ( GARBAGE ) System.gc();
                
                HiderRecord record = hiderRecords.get(hiderRecords.indexOf(new HiderRecord(lastHider)));
                
                if ( GARBAGE ) System.gc();
                
                record.addSeeker(new TraverserRecord("MixedSeekerStrats"));
                
                record.getSeeker("MixedSeekerStrats").setTopology(topology);
                
                record.getSeeker("MixedSeekerStrats").setRounds(rounds);

                record.getSeeker("MixedSeekerStrats").setAdditionalResourceImmunity(additionalResourceImmunity);
                
                record.getSeeker("MixedSeekerStrats").setDatafile(path);
                
              }
              
              lastSeeker = hiderRecords.get(hiderRecords.indexOf(new HiderRecord(lastHider))).getSeeker("MixedSeekerStrats").getTraverser();
              
              if ( GARBAGE ) System.gc();
              
            } else {
              
              // If the last hider doesn't have a record of this seeker, add it
              if (!hiderRecords.get(hiderRecords.indexOf(new HiderRecord(lastHider))).containsSeeker(new TraverserRecord(word))) {
                
                if ( GARBAGE ) System.gc();
                
                HiderRecord record = hiderRecords.get(hiderRecords.indexOf(new HiderRecord(lastHider)));
                
                if ( GARBAGE ) System.gc();
                
                record.addSeeker(new TraverserRecord(word));
                
                record.getSeeker(word).setTopology(topology);
                
                record.getSeeker(word).setRounds(rounds);

                record.getSeeker(word).setAdditionalResourceImmunity(additionalResourceImmunity);

                record.getSeeker(word).setDatafile(path);
                
              }
              
              lastSeeker = word;
              
            }
            
            lastTraverser = "seeker";
            
          } else if ( word.equals("G") ) {
            
            gameOrRound = "Game";
            
          } else if ( word.equals("R") ) {
            
            gameOrRound = "Round";
            
          } else if ( word.startsWith("org") ) {

            if ( lastTraverser.equals("hider") ) {
            
              hiderRecords.get(hiderRecords.indexOf(new HiderRecord(lastHider))).setTraverserType(word);
              
              if ( GARBAGE ) System.gc();
              
            } else if ( lastTraverser.equals("seeker") ) {
              
              hiderRecords.get(hiderRecords.indexOf(new HiderRecord(lastHider))).getSeeker(lastSeeker).setTraverserType(word);
              
              if ( GARBAGE ) System.gc();
              
            } 

          // If we come across an attribute entry
          } else {
            
            lastAttribute = word;
            
          }
          
        }
        
      }
      
    }
    
    return hiderRecords;
    
  }
  
  /**
  * @param traversers
  * @param players
  * @param gameOrRound
  * @param title
  * @param attribute
  * @param outputEnabled
  * @param figureToOverwrite
  */
  public void showLineGraphForAttribute(ArrayList<TraverserRecord> traversers, ArrayList<TraverserRecord> players, String gameOrRound, String title, String attribute, boolean outputEnabled, String figureToOverwrite) {
    
    if ( gameOrRound.equals("Game") ) {
      
      showGraphForAttribute(traversers, players, gameOrRound, title, "Line", "Game Number", attribute, "", outputEnabled, figureToOverwrite);
      
    } else if ( gameOrRound.equals("Round") ) {
      
      showGraphForAttribute(traversers, players, gameOrRound, title, "Line", "Round Number", attribute, "", outputEnabled, figureToOverwrite);
      
    }
    
  }
  
  /**
  * @param traversers
  * @param players
  * @param gameOrRound
  * @param title
  * @param attribute
  * @param outputEnabled
  */
  public void showLineGraphForAttribute(ArrayList<TraverserRecord> traversers, ArrayList<TraverserRecord> players, String gameOrRound, String title, String attribute, boolean outputEnabled) {
    
    if ( gameOrRound.equals("Game") ) {
      
      showGraphForAttribute(traversers, players, gameOrRound, title, "Line", "Game Number", attribute, "", outputEnabled);
      
    } else if ( gameOrRound.equals("Round") ) {
      
      showGraphForAttribute(traversers, players, gameOrRound, title, "Line", "Round Number", attribute, "", outputEnabled);
      
    }
    
  }
  
  /**
  * @param traversers
  * @param players
  * @param gameOrRound
  * @param title
  * @param attribute
  * @param outputEnabled
  * @param figureToOverwrite
  */
  public void showLineOneGraphForAttribute(ArrayList<TraverserRecord> traversers, ArrayList<TraverserRecord> players, String gameOrRound, String title, String attribute, boolean outputEnabled, String figureToOverwrite) {
    
    if ( gameOrRound.equals("Game") ) {
      
      showGraphForAttribute(traversers, players, gameOrRound, title, "LineOne", "Game Number", attribute, "", outputEnabled, figureToOverwrite);
      
    } else if ( gameOrRound.equals("Round") ) {
      
      showGraphForAttribute(traversers, players, gameOrRound, title, "LineOne", "Round Number", attribute, "", outputEnabled, figureToOverwrite);
      
    }
    
  }
  
  /**
  * @param traversers
  * @param players
  * @param gameOrRound
  * @param title
  * @param attribute
  * @param outputEnabled
  */
  public void showLineOneGraphForAttribute(ArrayList<TraverserRecord> traversers, ArrayList<TraverserRecord> players, String gameOrRound, String title, String attribute, boolean outputEnabled) {
    
    if ( gameOrRound.equals("Game") ) {
      
      showGraphForAttribute(traversers, players, gameOrRound, title, "LineOne", "Game Number", attribute, "", outputEnabled);
      
    } else if ( gameOrRound.equals("Round") ) {
      
      showGraphForAttribute(traversers, players, gameOrRound, title, "LineOne", "Round Number", attribute, "", outputEnabled);
      
    }
    
  }
  
  /**
  * @param traversers
  * @param players
  * @param gameOrRound
  * @param title
  * @param attribute
  * @param category
  * @param outputEnabled
  */
  public void showBarGraphForAttribute(ArrayList<TraverserRecord> traversers, ArrayList<TraverserRecord> players, String gameOrRound, String title, String attribute, String category, boolean outputEnabled, boolean outputPermutations) {
    
    showGraphForAttribute(traversers, players, gameOrRound, title, "Bar", "Game Number", attribute, category, outputEnabled, outputPermutations);
    
  }

  /**
  * @param traversers
  * @param players
  * @param gameOrRound
  * @param title
  * @param attribute
  * @param category
  * @param outputEnabled
  * @param outputPermutations
  */
  public void showBarGraphForAttribute(ArrayList<TraverserRecord> traversers, ArrayList<TraverserRecord> players, String gameOrRound, String title, String attribute, String category, boolean outputEnabled) {
    
    showBarGraphForAttribute(traversers, players, gameOrRound, title, attribute, category, outputEnabled, false);
    
  }
  
  /**
  * @param traversers
  * @param players
  * @param gameOrRound
  * @param title
  * @param attribute
  * @param category
  * @param outputEnabled
  * @param figureToOverwrite
  */
  public void showBarGraphForAttribute(ArrayList<TraverserRecord> traversers, ArrayList<TraverserRecord> players, String gameOrRound, String title, String attribute, String category, boolean outputEnabled, String figureToOverwrite) {
    
    showGraphForAttribute(traversers, players, gameOrRound, title, "Bar", "Game Number", attribute, category, outputEnabled, figureToOverwrite);
    
  }
  
  /**
  * @param traversers
  * @param players
  * @param gameOrRound
  * @param title
  * @param attribute
  * @param category
  * @param outputEnabled
  */
  public void show3DGraphForAttribute(ArrayList<TraverserRecord> traversers, ArrayList<TraverserRecord> players, String gameOrRound, String title, String attribute, String category, boolean outputEnabled) {
    
    showGraphForAttribute(traversers, players, gameOrRound, title, "3D", "Game Number", attribute, category, outputEnabled);
    
  }
  
  /**
  * @param traversers
  * @param players
  * @param gameOrRound
  * @param title
  * @param attribute
  * @param category
  * @param outputEnabled
  */
  public void show3DGraphForAttribute(ArrayList<TraverserRecord> traversers, ArrayList<TraverserRecord> players, String gameOrRound, String title, String attribute, String category, boolean outputEnabled, String figureToOverwrite) {
    
    showGraphForAttribute(traversers, players, gameOrRound, title, "3D", "Game Number", attribute, category, outputEnabled, figureToOverwrite);
    
  }
  
  /**
  * 
  * 16/5 -- Only a placeholder. Expansion done elsewhere.
  * @param traverserRecords
  * @return
  */
  protected ArrayList<TraverserRecord> expandTraverserRecords( ArrayList<TraverserRecord> traverserRecords ) {
    
    ArrayList<TraverserRecord> localTraverserRecords = new ArrayList<TraverserRecord>();
    
    for ( TraverserRecord traverserRecord : traverserRecords ) {
      
      localTraverserRecords.add(traverserRecord);
      
      if ( traverserRecord instanceof HiderRecord ) {
        
        // Uncomment to actually expand here
        // localTraverserRecords.addAll(((HiderRecord)traverserRecord).getSeekersAndAttributes());
        
      }
      
    }
    return localTraverserRecords;
    
  }
  
  /**
  * @param traverser
  * @param gameOrRound
  * @return
  */
  protected LinkedHashMap<AttributeSetIdentifier, Hashtable<String,Double>> getTraverserSeries(TraverserRecord traverser, String gameOrRound) {
    
    if ( gameOrRound.equals("Game") ) {
      
      return new LinkedHashMap<AttributeSetIdentifier, Hashtable<String,Double>>(traverser.getGameSeries());
      
    } else if ( gameOrRound.equals("Round") ) {
      
      return new LinkedHashMap<AttributeSetIdentifier, Hashtable<String,Double>>(traverser.getRoundSeries(traverser.getRounds()));
      
    } else {
      
      return new LinkedHashMap<AttributeSetIdentifier, Hashtable<String,Double>>();
      
    }
    
  }
  
  /**
  * @param traverserRecords
  * @return
  */
  protected Hashtable<String, Double> maxForAttributeInAllSeries( ArrayList<TraverserRecord> traverserRecords, String gameOrRound, GraphType graphType ) {
    
    Hashtable<String, Double> maxAttributeToValueAllSeries = new Hashtable<String, Double>();
    
    ArrayList<TraverserRecord> localTraverserRecords = expandTraverserRecords(traverserRecords);
    
    for ( TraverserRecord traverserRecord : localTraverserRecords ) {
      
      for ( Entry<String, TraverserDataset> attributeToDataset : traverserRecord.getAttributeToDataset(getTraverserSeries(traverserRecord, gameOrRound), null, null).entrySet() ) {
        
        ArrayList<Double> valuesInSeriesForAttribute = attributeToDataset.getValue().getDataset();
        
        if ( graphType == GraphType.BAR ) {
          
          valuesInSeriesForAttribute = new ArrayList<Double>();
          
          valuesInSeriesForAttribute.add(attributeToDataset.getValue().getMean());
          
        }
        
        for ( double valueInSeriesForAttribute : valuesInSeriesForAttribute ) {
          
          if ( !maxAttributeToValueAllSeries.containsKey(attributeToDataset.getKey()) ) {
            
            maxAttributeToValueAllSeries.put(attributeToDataset.getKey(), valueInSeriesForAttribute);
            
          } else {
            
            if ( valueInSeriesForAttribute > maxAttributeToValueAllSeries.get(attributeToDataset.getKey())) {
              
              maxAttributeToValueAllSeries.put(attributeToDataset.getKey(), valueInSeriesForAttribute);
              
            }
            
          }
          
        }
        
      }
      
    }
    
    return maxAttributeToValueAllSeries;
    
  }
  
  /**
  * @param traverserRecords
  * @return
  */
  protected Hashtable<String, Double> minForAttributeInAllSeries( ArrayList<TraverserRecord> traverserRecords, String gameOrRound, GraphType graphType ) {
    
    Hashtable<String, Double> minAttributeToValueAllSeries = new Hashtable<String, Double>();
    
    ArrayList<TraverserRecord> localTraverserRecords = expandTraverserRecords(traverserRecords);
    
    for ( TraverserRecord traverserRecord : localTraverserRecords ) {
      
      for ( Entry<String, TraverserDataset> attributeToDataset : traverserRecord.getAttributeToDataset(getTraverserSeries(traverserRecord, gameOrRound), null, null).entrySet() ) {
        
        ArrayList<Double> valuesInSeriesForAttribute = attributeToDataset.getValue().getDataset();
        
        if ( graphType == GraphType.BAR ) {
          
          valuesInSeriesForAttribute = new ArrayList<Double>();
          
          valuesInSeriesForAttribute.add(attributeToDataset.getValue().getMean());
          
        }
        
        for ( double valueInSeriesForAttribute : valuesInSeriesForAttribute ) {
          
          if (!minAttributeToValueAllSeries.containsKey(attributeToDataset.getKey())) {
            
            minAttributeToValueAllSeries.put(attributeToDataset.getKey(), valueInSeriesForAttribute);
            
          } else {
            
            if ( valueInSeriesForAttribute < minAttributeToValueAllSeries.get(attributeToDataset.getKey())) {
              
              minAttributeToValueAllSeries.put(attributeToDataset.getKey(), valueInSeriesForAttribute);
              
            }
            
          }
          
        }
        
      }
      
    }
    
    return minAttributeToValueAllSeries;
    
  }
  
  /**
  * @param entries
  * @param minInSeries
  * @param maxInSeries
  * @return
  */
  protected double normaliseEntry(Hashtable<String, Double> entries, Hashtable<String, Double> minInSeries,  Hashtable<String, Double> maxInSeries) {
    
    return ( entries.get(Metric.COST.getText()) - minInSeries.get(Metric.COST.getText()) ) / ( maxInSeries.get(Metric.COST.getText()) - minInSeries.get(Metric.COST.getText()) );
    
  }
  
  /**
  * 
  * Gets payoff information out of class.
  * 
  * @param traverserRecords
  * @return
  */
  public ArrayList<Pair<TraverserRecord, Double>> matrixPayoff(ArrayList<TraverserRecord> traverserRecords, ArrayList<TraverserRecord> allPlayers) {
    
    ArrayList<Pair<TraverserRecord, Double>> matrixPayoffValues = new ArrayList<Pair<TraverserRecord, Double>>();
    
    for ( TraverserRecord traverser : traverserRecords ) {
      
      matrixPayoffValues.add(new Pair<TraverserRecord, Double>(traverser, traverserPayoff(traverser, minForAttributeInAllSeries(allPlayers, "Game", GraphType.BAR), maxForAttributeInAllSeries(allPlayers, "Game", GraphType.BAR)).getValue() * 10));
      
    }
    
    return matrixPayoffValues;
    
  }
  
  public AbstractMap.SimpleEntry<TraverserRecord, Double> traverserSuccessPayoff(TraverserRecord traverser, boolean useBaseline) {

    return traverserSuccessPayoff(traverser, useBaseline, null, null, null);

  }

  public AbstractMap.SimpleEntry<TraverserRecord, Double> traverserSuccessPayoff(TraverserRecord traverser, boolean useBaseline, Hashtable<String, Double> entries, Hashtable<String, Double> minInSeries,  Hashtable<String, Double> maxInSeries) {

    if ( !(traverser instanceof HiderRecord) ) {
      
      Class<? extends Seeker> seekerClass = null;
      try {
        seekerClass = (Class<? extends Seeker>)Class.forName(traverser.getTraverserType());
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }

      String seekerClassInterfaces = String.join(",", Arrays.stream(seekerClass.getInterfaces()).map(i -> i.getName()).toArray(String[]::new));

      // Traversers are, by default, affected by unsuccessful games to a degree reflected by BASE_NON_RESOURCE_IMMUNITY. 
      // This immunity may be improved by additional game-wide traverser immunity, subject to behaviour. 
      // ResourceImmune traversers are less affected, to a degree reflected by the BASE_RESOURCE_IMMUNITY decrement.
      double decrement = 
        seekerClassInterfaces.contains("ResourceImmune") 
          ? 
            // Lower decrement is better, but higher base immunities (resource immune and other traverers) is better, so subtract immunities from 1
            1 - Success.BASE_RESOURCE_IMMUNITY 
          : 
            1 - Math.min( Success.BASE_NON_RESOURCE_IMMUNITY + 
              // Aim to increase immunity via additional resource immunity in environment
              ( seekerClassInterfaces.contains("VariableImmune") 
                ? 
                  ( Success.LEVERAGE_IMMUNITY(traverser.getTraverser()) 
                    ? 
                      ( Math.max( traverser.getAdditionalResourceImmunity(), traverser.getAdditionalResourceImmunity() * Success.BASE_NON_RESOURCE_IMMUNITY ) )
                    : 
                      0 ) 
                : 
                  // Interventions aim to increase immunity of non-resource immune traversers by a proportion of their existing immunity
                  // If base immunity of non-immune traversers is zero, this will have no effect, so just use the additional immunity
                  ( Math.max( traverser.getAdditionalResourceImmunity(), traverser.getAdditionalResourceImmunity() * Success.BASE_NON_RESOURCE_IMMUNITY ) ) 
              )
            , 
            Success.BASE_RESOURCE_IMMUNITY);

      double successfulGames = (entries!=null && minInSeries!=null && maxInSeries!=null) ? ( entries.get(Metric.SUCCESS.getText()) - minInSeries.get(Metric.SUCCESS.getText()) ) / ( maxInSeries.get(Metric.SUCCESS.getText()) - minInSeries.get(Metric.SUCCESS.getText()) ) : traverser.getAttributeToGameAverage(Metric.SUCCESS.getText());

      double unsuccessfulGames = 1 - successfulGames;
      // Extent of seeker (negative) payoff is determined by number of unsuccessful games
      double payoff = -1 * unsuccessfulGames;
      // As such, a lower decrement is better for payoff (moves it closer to zero)
      payoff = decrement * payoff;
      // Put on positive scale for graph
      payoff = 1 + payoff;

      if(useBaseline) {
        JSONObject baseline = plugin.getJSONObject("baseline");
        ArrayList<String> matchingBaselineKeys = baseline.keySet().stream().filter(key->traverser.getTraverser().contains(key)).collect(Collectors.toCollection(ArrayList::new));
        // We are graphing the baseline, so just show the baseline data
        if(traverser.getOpponents().contains(baseline.getString("environment").toString())) {
          if(baseline.has(traverser.getTraverser())) payoff = baseline.getJSONObject(traverser.getTraverser()).getDouble("data");
        // Otherwise, use change from baseline payoff to increase or decrease the baseline data
        } else if(matchingBaselineKeys.size()>0) {
          JSONObject baselineTraverser = baseline.getJSONObject(matchingBaselineKeys.get(0));
          double baselinePayoff = baselineTraverser.getDouble("payoff") > -1 ? baselineTraverser.getDouble("payoff") : Math.random();
          double change = (payoff - baselinePayoff) / baselinePayoff;
          if(baseline.has(matchingBaselineKeys.get(0))) { 
            double data = baseline.getJSONObject(matchingBaselineKeys.get(0)).getDouble("data");
            double additional = change * baseline.getJSONObject(matchingBaselineKeys.get(0)).getDouble("data");
            boolean invert = baseline.getBoolean("invert");
            payoff = invert ? Math.max(0, data - additional) : data + additional; 
          }
        } else {
          System.out.println("WARN: Baseline requested but no baseline data available.");
        }
      }

      Utils.talk(toString(), traverser + " payoff: " + payoff);

      return new AbstractMap.SimpleEntry<TraverserRecord, Double>(traverser, payoff);
      
    } else {

      return new AbstractMap.SimpleEntry<TraverserRecord, Double>(traverser, -1.0);

    }

  }

  /**
  * @param traverser
  * @param minForAttributeInAllSeries
  * @param maxForAttributeInAllSeries
  * @return
  */
  public AbstractMap.SimpleEntry<TraverserRecord, Double> traverserPayoff(TraverserRecord traverser, Hashtable<String, Double> minForAttributeInAllSeries, Hashtable<String, Double> maxForAttributeInAllSeries) {
    
    if ( traverser instanceof HiderRecord ) {
      
      if ( ((HiderRecord)traverser).getSeekersAndAttributes().size() > 1 ) System.err.println("WARNING: A Hider is matched to more than one seeking agent. Taking an average of the performance of these seekers in order to calculate payoff.");
      
      ArrayList<TraverserRecord> allSeekers = new ArrayList<TraverserRecord>();
      
      for ( TraverserRecord hidersSeeker : ((HiderRecord)traverser).getSeekersAndAttributes()) {
        
        allSeekers.add(hidersSeeker);
        
      }
      
      double normalisedSeekerCost = 0.0;
      
      for ( TraverserRecord hidersSeeker : ((HiderRecord)traverser).getSeekersAndAttributes()) {
        
        normalisedSeekerCost += hidersSeeker.getAttributeToGameAverage(Metric.COST.getText(), minForAttributeInAllSeries, maxForAttributeInAllSeries);
        
        Utils.talk(toString(), hidersSeeker + ": " + normalisedSeekerCost);
        
        Utils.talk(toString(), "Seeker cost: " + ( ( normalisedSeekerCost / (double)((HiderRecord)traverser).getSeekersAndAttributes().size() )));
        
        Utils.talk(toString(), "Hider cost: " +  ( traverser.getAttributeToGameAverage(Metric.COST.getText(), minForAttributeInAllSeries, maxForAttributeInAllSeries) ) );
        
        Utils.talk(toString(), traverser + " vs " + hidersSeeker + " payoff: " + ( ( normalisedSeekerCost / (double)((HiderRecord)traverser).getSeekersAndAttributes().size() ) - traverser.getAttributeToGameAverage(Metric.COST.getText(), minForAttributeInAllSeries, maxForAttributeInAllSeries) ) );
        
      }
      
      return new AbstractMap.SimpleEntry<TraverserRecord, Double>(traverser, ( normalisedSeekerCost / (double)((HiderRecord)traverser).getSeekersAndAttributes().size() ) - traverser.getAttributeToGameAverage(Metric.COST.getText(), minForAttributeInAllSeries, maxForAttributeInAllSeries ));
      
      
    } else {
      
      Utils.talk(toString(), traverser + " payoff: " + ( -1 * traverser.getAttributeToGameAverage(Metric.COST.getText(), minForAttributeInAllSeries, maxForAttributeInAllSeries )));
      
      return new AbstractMap.SimpleEntry<TraverserRecord, Double>(traverser, -1 * traverser.getAttributeToGameAverage(Metric.COST.getText(), minForAttributeInAllSeries, maxForAttributeInAllSeries ));
      
    }
    
  }
  
  /**
  * @param playerRecords
  * @param traverserRecords
  * @param gameOrRound
  * @param title
  * @param graphType
  * @param xLabel
  * @param yLabel
  * @param category
  * @param outputEnabled
  */
  public void showGraphForAttribute(ArrayList<TraverserRecord> playerRecords, ArrayList<TraverserRecord> traverserRecords, String gameOrRound, String title, String graphType, String xLabel, String yLabel, String category, boolean outputEnabled) {
    
    showGraphForAttribute(playerRecords, traverserRecords, gameOrRound, title, graphType, xLabel, yLabel, category, outputEnabled, false);
    
  }

  /**
  * @param playerRecords
  * @param traverserRecords
  * @param gameOrRound
  * @param title
  * @param graphType
  * @param xLabel
  * @param yLabel
  * @param category
  * @param outputEnabled
  * @param permutationsEnabled
  */
  public void showGraphForAttribute(ArrayList<TraverserRecord> playerRecords, ArrayList<TraverserRecord> traverserRecords, String gameOrRound, String title, String graphType, String xLabel, String yLabel, String category, boolean outputEnabled, boolean permutationsEnabled) {
    
    String figureID;
    if( permutationsEnabled ) {
      
      Set<String> players = new HashSet<String>(traverserRecords.stream().map(traverserRecord->traverserRecord.getTraverser()).collect(Collectors.toList()));
      Set<String> opponents = new HashSet<String>(traverserRecords.stream().map(traverserRecord->traverserRecord.getOpponents()).collect(Collectors.toList()));
      
      ArrayList<String> playersSorted = new ArrayList<String>(players);
      ArrayList<String> opponentsSorted = new ArrayList<String>(opponents);

      Collections.sort(playersSorted);
      Collections.sort(opponentsSorted);

      figureID = playersSorted.toString().replaceAll(" ", "-").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(",", "") + "_" + opponentsSorted.toString().replaceAll(" ", "-").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(",", "");
    
    } else {
    
      figureID = "figure" + new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
    
    }
    
    showGraphForAttribute(playerRecords, traverserRecords, gameOrRound, title, graphType, xLabel, yLabel, category, outputEnabled, figureID);
    
  }
  
  private static boolean STRICT_RENAME = false;
  
  private String pluginXLabel(String xLabel, String graphType, ArrayList<TraverserRecord> traverserRecords, ArrayList<TraverserRecord> playerRecords) {
    
    String suffix = "";
    JSONObject xLabelGame = plugin.getJSONObject("game").getJSONObject("graph").getJSONObject("xLabel");
    
    // Determine if seeker present that indicates relabelling
    ArrayList<String> pluginSeekers = Arrays.stream(JSONObject.getNames(xLabelGame.getJSONObject("seekers"))).filter(pluginSeeker -> traverserRecords.stream().filter(storedSeeker -> storedSeeker.getTraverserType().contains(pluginSeeker)).count() > 0).collect(Collectors.toCollection(ArrayList::new));
    if(pluginSeekers.size() > 0) suffix += xLabelGame.getJSONObject("seekers").getString(pluginSeekers.get(0));

    // Determine if config present that indicates relabelling
    ArrayList<String> pluginConfigs = Arrays.stream(JSONObject.getNames(xLabelGame.getJSONObject("config"))).filter(pluginConfig -> playerRecords.stream().filter(playerRecord -> playerRecord.getParameters() != null).collect(Collectors.toList()).get(0).getParameters().contains(pluginConfig)).collect(Collectors.toCollection(ArrayList::new));
    if(pluginConfigs.size() > 0) suffix += xLabelGame.getJSONObject("config").getString(pluginConfigs.get(0));

    return plugin == null ? xLabel : plugin.getJSONObject("graph").getJSONObject(graphType).getString("xLabel") + ( suffix.length() > 0 ? " (" + suffix + ")" : "" );

  }

  /**
  * @param playerRecords All players
  * @param traverserRecords Only those selected
  * @param gameOrRound
  * @param title
  * @param graphType
  * @param xLabel
  * @param yLabel
  * @param category
  * @param outputEnabled
  */
  public void showGraphForAttribute(ArrayList<TraverserRecord> playerRecords, ArrayList<TraverserRecord> traverserRecords, String gameOrRound, String title, String graphType, String xLabel, String yLabel, String category, boolean outputEnabled, String figureID) {
    
    if (traverserRecords.size() == 0 || playerRecords.size() == 0) {
      
      System.err.println("Warning, no traverser supplied");
      
      return;
      
    }
    
    SignificanceTable significanceTable = new SignificanceTable(); 
    
    if ( graphType.equals("Bar") ) SHOW_OPPONENT = false;
    
    if ( graphType.contains("Line") ) SHOW_OPPONENT = true;
    
    if ( !SHOW_OPPONENT ) for ( TraverserRecord record : traverserRecords ) record.doNotShowOpponents();
    
    if ( SHOW_OPPONENT ) for ( TraverserRecord record : traverserRecords ) record.showOpponents();
    
    Collections.sort(traverserRecords);
    
    //TraverserGraph graph = null;
    GNUGraph graph = null;
    
    if ( title.length() > 200 ) title = title.substring(0, 200);
    
    Hashtable<String, Double> minForAttributeInAllSeries;
    
    Hashtable<String, Double> maxForAttributeInAllSeries;
    
    if (graphType.contains("Line") || graphType.equals("3D")) {
      
      minForAttributeInAllSeries = minForAttributeInAllSeries(playerRecords, gameOrRound, GraphType.LINE);
      
      maxForAttributeInAllSeries = maxForAttributeInAllSeries(playerRecords, gameOrRound, GraphType.LINE);
      
      if (graphType.equals("Line")) {
        
        //graph = new LineGraph(title);
        graph = new GNULineGraph(title, textBased);
        
      } else if (graphType.equals("LineOne")) {
        
        graph = new GNULineGraph(title, textBased);
        
        ((GNULineGraph)graph).startsAtOne();
        
      } else if (graphType.equals("3D")) {
        
        graph = new GNU3DCollection(title, textBased);
        
      }
      
      LinkedHashMap<String, ArrayList<ArrayList<Double>>> multipleAttributeToValues = new LinkedHashMap<String, ArrayList<ArrayList<Double>>>();
    
      for ( TraverserRecord traverser : traverserRecords ) {
        
        if ( !multipleAttributeToValues.containsKey(traverser.getTraverser())) multipleAttributeToValues.put(traverser.getTraverser(), new ArrayList<ArrayList<Double>>());
        
        ArrayList<Double> attributeToValues = new ArrayList<Double>();
        
        int seriesNumber = 0;
        
        for ( Entry<AttributeSetIdentifier, Hashtable<String,Double>> seriesEntry : getTraverserSeries(traverser, gameOrRound).entrySet() ) {
          
          if ( yLabel.contains("Payoff" ) ) {
            
            if ( traverser instanceof HiderRecord ) {
              
              double cumulativeNormalisedSeekerCosts = 0.0;
              
              //if ( ((HiderRecord)traverser).getSeekersAndAttributes().size() > 1 ) System.err.println("WARNING: A Hider is matched to more than one seeking agent. Taking an average of the performance of these seekers in order to calculate payoff.");
              
              for ( TraverserRecord hidersSeeker : ((HiderRecord)traverser).getSeekersAndAttributes()) {
                
                cumulativeNormalisedSeekerCosts += normaliseEntry(new ArrayList<Hashtable<String, Double>>(getTraverserSeries(hidersSeeker, gameOrRound).values()).get(seriesNumber), minForAttributeInAllSeries, maxForAttributeInAllSeries );
                
              }
              
              // Payoff = (Average) Seeker(s) cost - Hider cost.
              attributeToValues.add( ( cumulativeNormalisedSeekerCosts / (double)((HiderRecord)traverser).getSeekersAndAttributes().size() ) - normaliseEntry(seriesEntry.getValue(), minForAttributeInAllSeries, maxForAttributeInAllSeries) );
              
            } else {
              
              if ( yLabel.equals("Success Payoff") ) {

                attributeToValues.add( traverserSuccessPayoff(traverser, false, seriesEntry.getValue(), minForAttributeInAllSeries, maxForAttributeInAllSeries).getValue() );
              
              } else if ( yLabel.equals("Baseline Success Payoff") ) {

                attributeToValues.add( traverserSuccessPayoff(traverser, true, seriesEntry.getValue(), minForAttributeInAllSeries, maxForAttributeInAllSeries).getValue() );

              } else {

                attributeToValues.add( -1 * normaliseEntry(seriesEntry.getValue(), minForAttributeInAllSeries, maxForAttributeInAllSeries) );

              }
              
            }
            
          } else {
            
            attributeToValues.add( seriesEntry.getValue().get(yLabel)  );
            
          }
          
          seriesNumber++;
          
        }
        
        multipleAttributeToValues.get(traverser.getTraverser()).add(attributeToValues);
        
        if (graphType.contains("Line")) {
          
          String traverserName = traverser.toString();
          try {
            traverserName = plugin == null ? traverserName : plugin.getJSONObject(traverser.getTraverser().startsWith("h") ? "hiders" : "seekers").getJSONObject("mapping").getString(traverser.getTraverser());
          } catch(JSONException e) {
            System.out.println("WARN: Plugin file incomplete: " + e.getMessage());
          }
          ((GNULineGraph) graph).addDataset(traverserName, attributeToValues);

          xLabel = pluginXLabel(xLabel, "line", traverserRecords, playerRecords);
          
        }

      }
      
      if (graphType.equals("3D")) {
        
        //for ( TraverserRecord traverserRecord : traverserRecords ) {
          
          //System.out.println("TraverserRecord: " + traverserRecord);
          
          //Extrapolate those values that will not 'fill' the entire 3D area to do so. ~MDC Deprecated?
          
          int maxRows = Integer.MIN_VALUE;
          
          //ArrayList<ArrayList<ArrayList<Double>>> datasets = new ArrayList<ArrayList<ArrayList<Double>>>();
          
          for ( Entry<String, ArrayList<ArrayList<Double>>> individualAttributeToValues : multipleAttributeToValues.entrySet()) {
            
            if ( individualAttributeToValues.getValue().size() > maxRows ) maxRows = individualAttributeToValues.getValue().size();
            
            //for ( ArrayList<ArrayList<Double>> dataset : datasets ) {
              
              if (individualAttributeToValues.getValue().size() < maxRows) {
                
                for ( int i = 1; i < maxRows; i ++) {
                  
                  individualAttributeToValues.getValue().add(individualAttributeToValues.getValue().get(0));
                  
                }
                
              }
              
              ((GNU3DCollection) graph).addDataset(individualAttributeToValues.getKey(), individualAttributeToValues.getValue());
              
            //}
              
            ((GNU3DCollection) graph).setZAxisLabel(yLabel);
            
            // ~MDC 20/8 May need putting back in if 'variable' in traverser name 
            /*if ( traverserRecord.getTraverser().toString().contains("Variable") ) {
              
              String traverser = traverserRecord.getTraverser().toString();
              
              traverser = traverser.substring(traverser.indexOf("Variable"), traverser.length());
              
              String[] labels = traverser.split("Variable");
              
              for ( int i = 0; i < labels.length; i++ ) {
                
                if ( labels[i].contains("-") ) {
                  
                  labels[i] = labels[i].substring(0, labels[i].indexOf("-"));
                  
                }
                
              }
              
              if (labels.length > 2 ) {
                
                yLabel = labels[1]; 
                
                xLabel = labels[2];
                
              }
              
            }*/
            
          }
          
        }
          
      //}
    
    } else if (graphType.equals("Bar")) {
    
      graph = new GNUBarGraph(title, textBased);
      
      TreeMap<String, ArrayList<Entry<TraverserRecord, Double>>> categoryToTraverserAndData = new TreeMap<String, ArrayList<Entry<TraverserRecord, Double>>>();
      
      ArrayList<Entry<TraverserRecord, Double>> traverserAndData = new ArrayList<Entry<TraverserRecord, Double>>();
      
      minForAttributeInAllSeries = minForAttributeInAllSeries(playerRecords, gameOrRound, GraphType.BAR);
      
      maxForAttributeInAllSeries = maxForAttributeInAllSeries(playerRecords, gameOrRound, GraphType.BAR);
      
      /* Sort so that check for new category is accurate (i.e. last category is
      * definitely exhausted).
      */
      if ( category.equals("Topology") ) {
        
        Collections.sort(traverserRecords, new Comparator<TraverserRecord>() {
          
          @Override
          public int compare(TraverserRecord o1, TraverserRecord o2) {
            
            if ( o1.getTopology().compareTo(o2.getTopology()) > ComparatorResult.EQUAL ) {
              
              return ComparatorResult.AFTER;
              
            } else if ( o1.getTopology().compareTo(o2.getTopology()) < ComparatorResult.EQUAL ) {
              
              return ComparatorResult.BEFORE;
              
            } else {
              
              return ComparatorResult.EQUAL;
              
            }
            
          }
          
        });
        
      } else if ( category.equals("Opponent") ) {
        
        Collections.sort(traverserRecords, new Comparator<TraverserRecord>() {
          
          @Override
          public int compare(TraverserRecord o1, TraverserRecord o2) {
            
            if ( o1.getOpponents().substring(1).compareTo(o2.getOpponents().substring(1)) < ComparatorResult.EQUAL ) {
              
              return ComparatorResult.AFTER;
              
            } else if ( o1.getOpponents().substring(1).compareTo(o2.getOpponents().substring(1)) > ComparatorResult.EQUAL ) {
              
              return ComparatorResult.BEFORE;
              
            } else {
              
              return ComparatorResult.EQUAL;
              
            }
            
          }
          
        });
        
      }
      
      String localCategory = "";
      
      for ( TraverserRecord traverser : traverserRecords ) {
        
        Utils.talk(toString(), "Processing " + traverser);
        
        //if ( traverser instanceof HiderRecord ) ((HiderRecord)traverser).switchShowSeekers();
        
        if ( category.equals("Topology") ) {
          
          if ( !traverser.getTopology().equals(localCategory) ) traverserAndData.clear(); 
          
          localCategory = traverser.getTopology();
          
        } else if ( category.equals("Opponent") ) {
          
          if ( !traverser.getOpponents().equals(localCategory) ) traverserAndData.clear(); 
          
          localCategory = traverser.getOpponents();
          
        }
        
        if ( yLabel.contains("Baseline Success Payoff") ) {
          
          traverserAndData.add(traverserSuccessPayoff(traverser, true));
        
        } else if ( yLabel.contains("Success Payoff") ) {
          
          traverserAndData.add(traverserSuccessPayoff(traverser, false));
            
        } else if ( yLabel.contains("Payoff") ) {
          
          traverserAndData.add(traverserPayoff(traverser, minForAttributeInAllSeries, maxForAttributeInAllSeries));

        } else {
          
          traverserAndData.add(new AbstractMap.SimpleEntry<TraverserRecord, Double>(traverser, traverser.getAttributeToGameAverage(yLabel)));	
          
        }
        
        categoryToTraverserAndData.put(localCategory, new ArrayList<Entry<TraverserRecord, Double>>(traverserAndData));
        
      }
      
      ArrayList<Entry<TraverserRecord, Double>> crossCategoryData = new ArrayList<Entry<TraverserRecord, Double>>();
      
      for ( Entry<String, ArrayList<Entry<TraverserRecord, Double>>> storedTraverserAndData : categoryToTraverserAndData.entrySet() ) {
        
        crossCategoryData.addAll(storedTraverserAndData.getValue());
        
      }
      
      Hashtable<TraverserRecord, String> traverserToSignificanceClass = new Hashtable<TraverserRecord, String>();
      
      if ( yLabel.equals(Metric.COST.getText()) || yLabel.equals(Metric.PAYOFF.getText()) ) {
        
        outer:
        for ( Entry<TraverserRecord, Double> traverserA : crossCategoryData ) {
          
          double cumulativeP = 0.0;
          
          traverserToSignificanceClass.put(traverserA.getKey(), "");
          
          for ( Entry<TraverserRecord, Double> traverserB : crossCategoryData ) {
            
            // exact same record
            if ( crossCategoryData.indexOf(traverserA) == crossCategoryData.indexOf(traverserB)) continue;
            //if ( traverserA.getKey() == traverserB.getKey() && traverserA.getKey().getOpponents() == traverserB.getKey().getOpponents ) continue;
            
            double pValue = 0.0;
            
            if ( yLabel.equals(Metric.COST.getText()) ) {
              
              if ( traverserA.getKey().pGroup(traverserB.getKey(), Metric.COST).equals("") ) continue outer;
              
              pValue = traverserA.getKey().pValue(traverserB.getKey(), Metric.COST);
              
              Utils.talk(toString(), traverserA.getKey() + " vs " + traverserB.getKey() + " : Percentage Difference - " + ( Utils.percentageChange(traverserB.getValue(), traverserA.getValue()) ) + " PValue - " + pValue);
              
              cumulativeP += pValue;
              
            } else if ( yLabel.equals(Metric.PAYOFF.getText()) ) {
              
              if ( traverserA.getKey().pGroup(traverserB.getKey(), minForAttributeInAllSeries, maxForAttributeInAllSeries ).equals("") ) continue outer;
              
              pValue = traverserA.getKey().pValue(traverserB.getKey(), minForAttributeInAllSeries, maxForAttributeInAllSeries );
              
              Utils.talk(toString(), traverserA.getKey() + " vs " + traverserB.getKey() + " : Percentage Difference - " + ( Utils.percentageChange(traverserB.getValue(), traverserA.getValue()) ) + " PValue - " + pValue);
              
              cumulativeP += pValue;
              
            }
            
            significanceTable.addPValue(traverserA.getKey().getTraverser() + " (vs. " + traverserA.getKey().getOpponents() + ")", traverserB.getKey().getTraverser() + " (vs. " + traverserB.getKey().getOpponents() + ")", traverserA.getValue(), traverserB.getValue(), pValue);
            
          }
          
          Utils.talk(toString(), "Average pValue against other traversers (" + crossCategoryData.size() + "): " + ( cumulativeP / crossCategoryData.size() ));
          
          traverserToSignificanceClass.put(traverserA.getKey(), StatisticalTest.getPGroup(cumulativeP / crossCategoryData.size()));
          
          Utils.talk(toString(), "Significance class of this value: " + traverserToSignificanceClass);
          
        }
        
      }
      
      // ~MDC 19/8 Could be neater
      for ( Entry<String, ArrayList<Entry<TraverserRecord, Double>>> storedTraverserAndData : categoryToTraverserAndData.entrySet() ) {
        
        Utils.talk(this.toString(), "Adding bar: " + storedTraverserAndData.getValue() + " " + storedTraverserAndData.getKey());
        
        String traverserName = storedTraverserAndData.getKey();
        try {
          traverserName = plugin == null ? storedTraverserAndData.getKey() : plugin.getJSONObject(storedTraverserAndData.getKey().startsWith("h") ? "hiders" : "seekers").getJSONObject("mapping").getString(storedTraverserAndData.getKey());
        } catch(JSONException e) {
          System.out.println("WARN: Plugin file incomplete: " + e.getMessage());
        }

        for( Entry<TraverserRecord, Double> matchedTraverserAndData : storedTraverserAndData.getValue() ) {
          
          try {
            matchedTraverserAndData.getKey().setTraverser(plugin == null ? matchedTraverserAndData.getKey().getTraverser() : plugin.getJSONObject(matchedTraverserAndData.getKey().getTraverser().startsWith("h") ? "hiders" : "seekers").getJSONObject("mapping").getString(matchedTraverserAndData.getKey().getTraverser()));
          } catch(JSONException e) {
            System.out.println("WARN: Plugin file incomplete: " + e.getMessage());
          }
        
        }

        ((GNUBarGraph) graph).addBars(storedTraverserAndData.getValue(), traverserName, traverserToSignificanceClass);
        
      }

      xLabel = pluginXLabel(xLabel, "bar", traverserRecords, playerRecords);

    }
  
    graph.styleGraph();
  
    boolean increaseKAndN = false;
    
    yLabel = plugin == null ? yLabel : yLabel.contains("Baseline") ? plugin.getJSONObject("baseline").getJSONObject("graph").getJSONObject(graphType.equals("Bar")?"bar":"line").getString("yLabel") : plugin.getJSONObject("graph").getJSONObject(graphType.equals("Bar")?"bar":"line").getString("yLabel");
    
    if ( graphType.equals("LineOne") ) {
      
      // ~MDC Assumes all graphs that start at one are showing K:N. For our purposes, this is true.
      xLabel = "$K$ ($N = 2K$)";
      
      increaseKAndN = true;
      
      graph.createChart("", xLabel, yLabel);
      
    } else {
      
      graph.createChart("", xLabel, yLabel);
      
    }
    
    boolean overwriting = false;
  
    if ( outputEnabled ) {
      
      ArrayList<String> affectedFiles = new ArrayList<String>();
      
      for ( TraverserRecord traverser : traverserRecords ) {
        
        String graphedSuffix = "";
        
        if ( !traverser.getDatafile().toString().contains("GRAPHED") ) graphedSuffix += "_GRAPHED";
        
        graphedSuffix += ( "_" + figureID );
        
        affectedFiles.add(traverser.getDatafile().toString());
        
        boolean result = traverser.getDatafile().toFile().renameTo(new File(traverser.getDatafile().toString().substring(0, traverser.getDatafile().toString().length() - 4) + graphedSuffix + ".csv"));
        
        if ( STRICT_RENAME && result == false ) {
          
          System.err.println("ERROR: Unable to rename file to include this figureID. YOUR OUTPUT HAS NOT BEEN WRITTEN. Exiting.");
          
          System.exit(0);
          
        }
        
      }
      
      File TEX = new File(Utils.FILEPREFIX + "charts/figures/" + figureID + ".tex");
      
      File EPS = new File(Utils.FILEPREFIX + "charts/figures/" + figureID + ".eps");
      
      if( TEX.exists() && !TEX.isDirectory() ) {
        
        overwriting = true;
        
        try {
          
          Utils.copyFile ( new File(Utils.FILEPREFIX + "charts/figures/" + figureID + ".tex" ), new File( Utils.FILEPREFIX + "charts/figures/" + figureID + "_copy.tex" ) );
          
        } catch (IOException e1) {
          
          e1.printStackTrace();
          
        }
        
      }
      
      if ( EPS.exists() && !EPS.isDirectory() ) {
        
        overwriting = true;
        
        try {
          
          Utils.copyFile ( new File(Utils.FILEPREFIX + "charts/figures/" + figureID + ".eps" ), new File( Utils.FILEPREFIX + "charts/figures/" + figureID + "_copy.eps" ) );
          
        } catch (IOException e1) {
          
          e1.printStackTrace();
          
        }
        
      }
      
      graph.exportChartAsEPS(Utils.FILEPREFIX + "charts/figures/" + figureID + ".eps");
      
      graph.exportChartAsTikz(Utils.FILEPREFIX + "charts/figures/" + figureID + ".tex");
      
      if ( !overwriting ) {
        
        try {
          
          Utils.writeToFile(new FileWriter(Utils.FILEPREFIX + "charts/figures.bib", true), "\n @FIG{" + figureID + ", main = { " + setupCaptionString(playerRecords, traverserRecords, increaseKAndN) + " }, add = { " + title + " " + affectedFiles + " }, file = {/Users/Martin/Dropbox/workspace/SearchGames/output/charts/figures/" + figureID + "}, source = {}}");
          
        } catch (IOException e) {
          
          e.printStackTrace();
          
        }
        
      }
      
      if ( graphType.equals("Bar") ) significanceTable.outputPValueTable(figureID, "pValues for the graph shown in \\fbref{" + figureID + "}.");
      
    }
    
    // Reshow opponents on UI
    if ( !SHOW_OPPONENT ) for ( TraverserRecord record : expandTraverserRecords(traverserRecords) ) record.showOpponents();
  
    /*graph.pack();
    
    RefineryUtilities.centerFrameOnScreen(graph);
    
    graph.setVisible(true);*/
    
  } 

  /**
  * @param traverserRecords
  * @return
  */
  protected String setupCaptionString(ArrayList<TraverserRecord> playerRecords, ArrayList<TraverserRecord> traverserRecords, boolean increaseKAndN) {
    
    HashSet<String> hiders = new HashSet<String>();
    
    HashSet<String> seekers = new HashSet<String>();
    
    boolean hiderTarget;
    
    if (traverserRecords.get(0) instanceof HiderRecord) {
      
      hiderTarget = true;
      
    } else {
      
      hiderTarget = false;
      
    }
    
    //
    
    for (TraverserRecord traverser : Utils.expandTraverserRecords(traverserRecords)) {
      
      if ( traverser instanceof HiderRecord ) {
        
        hiders.add(traverser.getTraverser());
        
      } else {
        
        seekers.add(traverser.getTraverser());
        
      }
      
      /* If no hiders, are found, traverserRecords must be all seekers, 
      * in which case we have to look through all player records, and find those
      * hiders that are associated to the seekers in traverser records (~MDC 10/8 design needs
      * updating to account for this)
      */
      if ( hiders.size() == 0 ) {
        
        for ( TraverserRecord hider : playerRecords ) {
          
          if (hider instanceof HiderRecord) {
            
            if ( ((HiderRecord)hider).containsSeeker(traverser) ) {
              
              hiders.add(hider.getTraverser());
              
            }
            
          }
          
        }
        
      }
      
    }
    
    //
    
    String hiderList = traverserList(hiders, "hider");
    
    String seekerList = traverserList(seekers, "seeker");
    
    //
    
    String suffix = ".";
    
    if ( increaseKAndN ) suffix = " at higher values of $K$ and $N$.";
    
    if ( hiderTarget ) {
      
      return "The performance of " + hiderList + " against " + seekerList + " on a " + traverserRecords.get(0).getTopology() + " network" + suffix;
      
    } else {
      
      return "The performance of " + seekerList + " against " + hiderList + " on a " + traverserRecords.get(0).getTopology() + " network" + suffix;
      
    }
    
  }

  /**
  * @param traversers
  * @param hiderOrSeeker
  * @return
  */
  protected String traverserList(HashSet<String> traversers, String hiderOrSeeker) {
    
    String traverserList;
    
    if ( traversers.size() > 3 ) {
      
      traverserList = "several " + hiderOrSeeker + " strategies";
      
    } else if ( traversers.size() == 1 ) {
      
      // \\\texttt{
      traverserList = "the " + hiderOrSeeker + " strategy " + (new ArrayList<String>(traversers).get(0)) + "";
      
    } else {
      
      traverserList = "the " + Utils.traverserNumberToWord(traversers.size()) + hiderOrSeeker + " strategies " + Utils.listToProse(new ArrayList<String>(traversers), "\texttt{", "}");
      
    }
    
    return traverserList;
    
  }
    
  /**
  * @return
  */
  public String printAllStats() {
    
    String returner = "";
    
    for ( ArrayList<HiderRecord> hiderRecords : cache ) {
      
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
    
    // Archive instead
    for ( Path path : Utils.listFilesForFolder(new File(FILEPREFIX + dataInput)) ) { 
      
      File archivedFile = new File(FILEPREFIX + "/dataArchive/" + path.getFileName());
      
      moveFile(path, Paths.get(archivedFile.getAbsolutePath()));
      
      // deleteFile(path);
      
    }
    
    for ( Path path : Utils.listFilesForFolder(new File(FILEPREFIX + dataInput + "/js/data")) ) deleteFile(path);
    
  }
  
  /**
  * Remove relating .js and .html files if .csv have been removed
  */
  public void removeOrphaned() {
    
    ArrayList<String> CSVIDs = new ArrayList<String>();
    
    for ( Path path : Utils.listFilesForFolder(new File(FILEPREFIX + dataInput)) ) { 
      
      if ( !path.toString().contains(".") ) continue;
      
      // If this is a .csv file, track its ID
      if ( path.toString().substring(path.toString().lastIndexOf("."), path.toString().length()).equals(".csv")) {
        
        CSVIDs.add(path.toString().substring(path.toString().lastIndexOf('/') + 1, path.toString().lastIndexOf('.')));
        
      }
      
    }
    
    // For all .html files, if no corresponding .csv ID recorded, remove.
    for ( Path path : Utils.listFilesForFolder(new File(FILEPREFIX + dataInput)) ) { 
      
      if ( path.toString().contains("-") && !CSVIDs.contains( path.toString().substring(path.toString().lastIndexOf('/') + 1, path.toString().lastIndexOf('-')) )) {
        
        deleteFile(path);
        
      }
      
    }
    
    // Similar for .js files
    for ( Path path : Utils.listFilesForFolder(new File(FILEPREFIX + dataInput + "/js/data")) ) {
      
      if ( path.toString().contains("-") && !CSVIDs.contains( path.toString().substring(path.toString().lastIndexOf('/') + 1, path.toString().lastIndexOf('-')) )) {
        
        deleteFile(path);
        
      }
      
    }
    
  }
  
  /**
  * 
  */
  protected final static String FILEPREFIX = "output/";
  
  /**
  * @param source
  * @param target
  */
  public void moveFile(Path source, Path target) {
    
    try {
      
      Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
      
    } catch (IOException e) {
      
      e.printStackTrace();
      
    }
    
  }
  
  /**
  * @param path
  */
  public void deleteFile(Path path) {
    
    Utils.deleteFile(path);
    
  }
  
  /* (non-Javadoc)
  * @see java.lang.Object#toString()
  */
  public String toString() {
    
    return "OutputManager";
    
  }
  
}
