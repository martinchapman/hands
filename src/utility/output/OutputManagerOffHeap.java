package Utility.output;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map.Entry;

import org.mapdblocal.BTreeMap;
import org.mapdblocal.DB;

import Utility.Pair;
import Utility.Utils;

/**
 * @author Martin
 */
public class OutputManagerOffHeap extends OutputManager {
	
	/**
	 * 
	 */
	final double cacheSizeInGB = 40.0;
	
	/**
	 * Multiple hiders per file, and multiple files.
	 * (was ArrayList<ArrayList<HiderRecord>>)
	 */
	private BTreeMap<Integer, BTreeMap<Integer, HiderRecord>> cache;
	
	/**
	 * To store main list of records
	 */
	private DB db;
	
	/**
	 * @return
	 */
	public BTreeMap<Integer, BTreeMap<Integer, HiderRecord>> getOffHeapCache() {
		
        return cache;
        
	}
	
	/**
	 * 
	 */
	private void addToCache(BTreeMap<Integer, HiderRecord> records) {
		
		cache.put(cache.size(), records);
		
	}
	
	/**
	 * For Grouped Records
	 */
	private ArrayList<Pair<DB, BTreeMap<Integer, HiderRecord>>> groupedDBAndBTreeMap;
	
	/**
	 * For Hider Records
	 */
	private ArrayList<Pair<DB, BTreeMap<Integer, HiderRecord>>> hiderRecordDBAndBTreeMap;
	
	/**
	 * 
	 */
	public OutputManagerOffHeap(String dataInput, boolean recursiveSub) {
		
		super(dataInput, recursiveSub);
		
		Pair<DB, BTreeMap<Integer, BTreeMap<Integer, HiderRecord>>> mainCache = Utils.getDBAndCache(cacheSizeInGB);
		
		// Create main store
        db = mainCache.getElement0();

        // Create main cache
        cache = mainCache.getElement1();
		
		groupedDBAndBTreeMap = new ArrayList<Pair<DB, BTreeMap<Integer, HiderRecord>>>();
		
		hiderRecordDBAndBTreeMap = new ArrayList<Pair<DB, BTreeMap<Integer, HiderRecord>>>();
		
	}
	
	/**
	 * 
	 */
	public void closeDBs() {
		
		cache.clear();
		
		db.close();
		
		for ( Pair<DB, BTreeMap<Integer, HiderRecord>> entry : groupedDBAndBTreeMap ) {
			
			entry.getElement1().clear();
			
			entry.getElement0().close();
			
		}
		
		for ( Pair<DB, BTreeMap<Integer, HiderRecord>> entry : hiderRecordDBAndBTreeMap ) {
			
			entry.getElement1().clear();
			
			entry.getElement0().close();
			
		}
		
	}
	
	public void processOutput(Datafile datafile) {
        
		if ( datafile instanceof GroupedDatafiles ) {
			
			Pair<DB, BTreeMap<Integer, HiderRecord>> groupedDBAndBTreeMap = Utils.getDBAndCache(cacheSizeInGB);
			
			this.groupedDBAndBTreeMap.add(groupedDBAndBTreeMap);
			
			BTreeMap<Integer, HiderRecord> groupedHiderRecords = groupedDBAndBTreeMap.getElement1();
			
			ArrayList<BTreeMap<Integer, HiderRecord>> toBeSplit = new ArrayList<BTreeMap<Integer, HiderRecord>>();
			
			ArrayList<Datafile> dataFiles = ((GroupedDatafiles)datafile).getAllDatafiles();
			
			for ( Datafile file : ((GroupedDatafiles)datafile).getAllDatafiles() ) {
				
				if ( textBased ) System.out.println("\nProcessing datafile (" + ((GroupedDatafiles)datafile).getAllDatafiles().indexOf(file) + "): " + file.getPath());
				
				toBeSplit.add(createHiderRecordsOffHeap(file.getPath()));
				
			}
			
			for ( int i = 0; i < toBeSplit.get(0).size(); i++ ) {
				
				groupedHiderRecords.put(i, new GroupedHiderRecords());
				
			}
			
			for ( BTreeMap<Integer, HiderRecord> individualRecordList : toBeSplit ) {
				
				for ( Entry<Integer, HiderRecord> individualRecord : individualRecordList.entrySet() ) {
				
					((GroupedHiderRecords)groupedHiderRecords.get(new ArrayList<HiderRecord>(individualRecordList.values()).indexOf(individualRecord.getValue()))).addHider(individualRecord.getValue());
				
				}
				
			}
			
			addToCache(groupedHiderRecords);
			
		} else {
			
			if ( textBased ) System.out.println("\nProcessing datafile: " + datafile.getPath());
			
			addToCache(createHiderRecordsOffHeap(datafile.getPath()));
			
		}
		
	}
	
	/**
	 * @param treeBTreeMap
	 * @param record
	 */
	private void addHiderToBTreeMap(BTreeMap<Integer, HiderRecord> treeBTreeMap, HiderRecord record) {
		
		treeBTreeMap.put(treeBTreeMap.size(), record);
		
	}
	
	/**
	 * 
	 */
	private BTreeMap<Integer, HiderRecord> createHiderRecordsOffHeap(Path path) {
		
		Pair<DB, BTreeMap<Integer, HiderRecord>> hiderRecordDBAndBTreeMap = Utils.getDBAndCache(cacheSizeInGB);
		
		this.hiderRecordDBAndBTreeMap.add(hiderRecordDBAndBTreeMap);
		
		Hashtable<String, Pair<Integer, HiderRecord>> hiderList = new Hashtable<String, Pair<Integer, HiderRecord>>();
        
		BTreeMap<Integer, HiderRecord> hiderRecords = hiderRecordDBAndBTreeMap.getElement1();
		
		ArrayList<String> lines = Utils.readFromFile(path.toString());
		
		String parameters = lines.remove(0);
		
		String topology = "";
		
		int rounds = -1;
		
		for ( String parameter : parameters.split(" ") ) {
			
			String[] keyAndValue = parameter.split(",");
			
			if ( keyAndValue[0].replace("{", "").equals("Topology") ) {
				
				topology = keyAndValue[1].replace("}", "");
				
			} else if ( keyAndValue[0].replace("{", "").equals("Rounds") ) {
				
				rounds = Integer.parseInt(keyAndValue[1].replace("}", ""));
				
			}
			
		}
		
		for ( String line : lines ) {
		
			Utils.printSystemStats("Hider records: " + hiderRecords.size() + " Cached records: " + cache.size());
			
			String lastHider = "";
			
			String lastSeeker = "";
			
			String lastTraverser = "";
			
			String lastAttribute = "";
			
			String gameOrRound = "";
			
			for ( String word : line.split(",")) {
				
				Utils.printSystemStats("Hider records: " + hiderRecords.size() + " Cached records: " + cache.size());
				
				word = word.trim();
				
				try {
					
					Double value = Double.parseDouble(word);
					
					if ( lastTraverser.equals("hider") ) {
						
						HiderRecord hider = hiderList.get(lastHider).getElement1();
						
						hider.attribute(gameOrRound, lastAttribute, value);
						
						hiderRecords.put(hiderList.get(lastHider).getElement0(), hider);
						
					} else if ( lastTraverser.equals("seeker") ) {
					
						HiderRecord hider = hiderList.get(lastHider).getElement1();
						
						TraverserRecord seeker = hider.getSeeker(lastSeeker);
						
						seeker.attribute(gameOrRound, lastAttribute, value);
						
						hiderRecords.put(hiderList.get(lastHider).getElement0(), hider);
						
					}

				} catch (NumberFormatException e) { 
					
					// If we come across an entry for a Hider
					if ( word.charAt(0) == 'h' ) {
					
						HiderRecord newHider = null;
						
						// Mixing, and first time round
						if ( parameters.contains("{MixHiders,true}") ) {
							
							// If mixing, only ever one hide record, for all hiders
							if ( (hiderRecords.size() == 0) ) {
							
								newHider = new HiderRecord(path, "MixedHiderStrats");
								
								if ( GARBAGE ) System.gc();
								
								newHider.setTopology(topology);
								
								newHider.setRounds(rounds);
								
								newHider.setParameters(parameters);
								
								newHider.setDatafile(path);
								
								hiderList.put("MixedHiderStrats", new Pair<Integer, HiderRecord>(hiderRecords.size(), newHider));
								
								if ( GARBAGE ) System.gc();
								
								hiderRecords.put(hiderRecords.size(), newHider);
								
							}
							
							// ~MDC Update for cache may break functionality
							lastHider = newHider.getTraverser();
							
						} else {
							
							// If we do not yet have a record for this hider
							if (!hiderList.containsKey(word)) {
								
								// Create it
								newHider = new HiderRecord(path, word);
								
								if ( GARBAGE ) System.gc();
								
								newHider.setTopology(topology);
								
								newHider.setRounds(rounds);
								
								newHider.setParameters(parameters);
								
								newHider.setDatafile(path);
								
								hiderList.put(word, new Pair<Integer, HiderRecord>(hiderRecords.size(), newHider));
			
								if ( GARBAGE ) System.gc();
								
								hiderRecords.put(hiderRecords.size(), newHider);
								
							}
							
							lastHider = word;
							
						}
						
						lastTraverser = "hider";
						
					// If we come across an entry for a Seeker
					} else if ( word.charAt(0) == 's' ) {
						
						if ( parameters.contains("{MixSeekers,true}") ) { 
							
							if ( hiderList.get(lastHider).getElement1().getSeekersAndAttributes().size() == 0 ) {
								
								HiderRecord record = hiderList.get(lastHider).getElement1(); //hiderRecords.get(new ArrayList<HiderRecord>(hiderRecords.values()).indexOf(new HiderRecord(lastHider)));
								
								record.addSeeker(new TraverserRecord("MixedSeekerStrats"));
								
								if ( GARBAGE ) System.gc();
								
								record.getSeeker("MixedSeekerStrats").setTopology(topology);
								
								record.getSeeker("MixedSeekerStrats").setRounds(rounds);
								
								record.getSeeker("MixedSeekerStrats").setDatafile(path);
								
								hiderRecords.put(hiderList.get(lastHider).getElement0(), record);
							
							}
							
							lastSeeker = hiderList.get(lastHider).getElement1().getSeeker("MixedSeekerStrats").getTraverser();
							
						} else {
							
							// If the last hider doesn't have a record of this seeker, add it
							if (!hiderList.get(lastHider).getElement1().containsSeeker(new TraverserRecord(word))) {
								
								HiderRecord record = hiderList.get(lastHider).getElement1();
								
								record.addSeeker(new TraverserRecord(word));
								
								if ( GARBAGE ) System.gc();
								
								record.getSeeker(word).setTopology(topology);
								
								record.getSeeker(word).setRounds(rounds);
								
								record.getSeeker(word).setDatafile(path);
								
								// ~MDC 28/8 Because cache immutable
								hiderRecords.put(hiderList.get(lastHider).getElement0(), record);
							
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
		
		return hiderRecords;
	
	}
	
	public String printAllStats() {
		
		String returner = "";
		
		for ( Entry<Integer, BTreeMap<Integer, HiderRecord>> hiderRecords : cache.entrySet() ) {
			
			for ( Entry<Integer, HiderRecord> hiderRecord : hiderRecords.getValue().entrySet() ) {
				
				returner += "\n" + hiderRecord.getValue().getTopology();
				
				returner += "\n" + hiderRecord.getValue().printStats();
				
			}
			
		}
		
		return returner;
		
	}

}