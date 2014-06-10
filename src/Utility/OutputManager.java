package Utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Martin
 *
 */
public class OutputManager {

	/**
	 * @author Martin
	 *
	 */
	private class TraverserRecord {
		
		/**
		 * 
		 */
		protected String traverser;
		
		protected int updates;
		
		/**
		 * @param traverser
		 */
		public TraverserRecord(String traverser) {
		
			this.traverser = traverser;
			
			attributeToValue = new Hashtable<String, Double>();
			
			updates = 0;
			
		}
			
		/**
		 * @return
		 */
		public String getTraverser() {
			
			return traverser;
		
		}
			
		/**
		 * 
		 */
		protected Hashtable<String, Double> attributeToValue;
			
		/**
		 * @param attribute
		 * @param value
		 */
		public void attribute(String attribute, double value) {
		
			updates++;
			
			if (attributeToValue.containsKey(attribute)) {
			
				attributeToValue.put(attribute, attributeToValue.get(attribute) + value);
				
			} else {
			
				attributeToValue.put(attribute, value);
			
			}
			
		}
		
		/**
		 * 
		 */
		protected Hashtable<String, Double> calculateAverage() {
			
			Hashtable<String, Double> averageAttributeToValue = new Hashtable<String, Double>();
			
			for (Entry<String, Double> attribute : attributeToValue.entrySet()) {
				
				/* To find the number of times an individual attribute has been updated, 
				 * divide the total updates by the number attributes */
				averageAttributeToValue.put(attribute.getKey(), attribute.getValue() / (updates / attributeToValue.keySet().size()));
				
			}
			
			return averageAttributeToValue;
			
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
		
			return traverser + " " + calculateAverage();
			
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			
			return getTraverser().equals(((TraverserRecord) obj).getTraverser());
			
		}
		
			
	}
	
	/**
	 * @author Martin
	 *
	 */
	private class HiderRecord extends TraverserRecord {
		
		/**
		 * 
		 */
		private ArrayList<TraverserRecord> seekersAndAttributes;
		
		/**
		 * @param hider
		 */
		public HiderRecord(String hider) {
			
			super(hider);
			
			seekersAndAttributes = new ArrayList<TraverserRecord>();
		
		}
		
		/**
		 * @param seekerRecord
		 */
		public void addSeeker(TraverserRecord seekerRecord) {
		
			seekersAndAttributes.add(seekerRecord);
			
		}
		
		/**
		 * @param seeker
		 * @return
		 */
		public boolean containsSeeker(TraverserRecord seeker) {
			
			return seekersAndAttributes.contains(seeker);
			
		}
		
		/**
		 * @param seeker
		 * @return
		 */
		public TraverserRecord getSeeker(String seeker) {
		
			return seekersAndAttributes.get(seekersAndAttributes.indexOf(new TraverserRecord(seeker)));
			
		}
		
		/* (non-Javadoc)
		 * @see Utility.OutputManager.TraverserRecord#toString()
		 */
		public String toString() {
			
			String returner = "";
			
			returner += traverser + " " + calculateAverage() + "\n";
			
			returner += seekersAndAttributes;
			
			return returner;
			
		}

	}
	
	/**
	 * 
	 */
	public OutputManager() {
		
		ArrayList<Path> files = listFilesForFolder(new File(FILEPREFIX + "/Data"));
		
		if (files.size() == 0) {
			
			System.out.println("No output files.");
			
			System.exit(0);
			
		}
		
		System.out.println("In the output folder: ");
		
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
				
				System.out.println(parameters);
				
				for ( HiderRecord hiderRecord : hiderRecords ) {
				
					System.out.println(hiderRecord);
					
				}
				
			}
			
		}
		
		//
		
		TimerTask task = new TimerTask() {
			
			public void run() {
			
				System.out.println( "Timed out. Exit..." );
				
				System.exit( 0 );
			
			}
			
		};
		
		Timer timer = new Timer();
		
		timer.schedule( task, 10*1000 );
		
		//
		
		Scanner in = new Scanner(System.in);
		
		System.out.println("\nPRESS RETURN TO REMOVE FILES");
		
		if (in.nextLine().equals("")) {
		
			for ( Path path : listFilesForFolder(new File(FILEPREFIX + "/Data")) ) deleteFile(path);
			
			for ( Path path : listFilesForFolder(new File(FILEPREFIX + "/Data/js/Data")) ) deleteFile(path);
			
		}
		
		in.close();
		
		System.exit(0);
		
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
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		new OutputManager();

	}
	
}