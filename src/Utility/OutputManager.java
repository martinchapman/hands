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
import java.util.Scanner;

/**
 * @author Martin
 *
 */
public class OutputManager {

	/**
	 * 
	 */
	public OutputManager() {
		
		System.out.println("In the output folder: ");
		
		for ( Path path : listFilesForFolder(new File(FILEPREFIX + "/Data")) ) {
			
			if (path.toString().substring(path.toString().length() - 3, path.toString().length()).equals("csv")) {
				
				String titles = "\n";
				
				HashMap<Integer, Double> positionInCSVToData = new HashMap<Integer, Double>();
				
				ArrayList<String> lines = Utils.readFromFile(path.toString());
				
				String parameters = lines.remove(0);
				
				for ( String line : lines ) {
				
					int position = 0;
					
					for ( String word : line.split(",")) {
						
						position++;
						
						try {
							
							Double value = Double.parseDouble(word);
							
							if ( positionInCSVToData.containsKey(position)) {
								
								positionInCSVToData.put(position, positionInCSVToData.get(position) + value);
								
							} else {
								
								positionInCSVToData.put(position, value);
								
							}
							
						} catch (NumberFormatException e) { 
						
							titles += word + "	 ";
							
							continue; 
							
						}
						
					}
					
				}
				
				System.out.println(parameters);
				
				System.out.println(titles);
				
				for ( Double totalCosts : positionInCSVToData.values() ) {
					
					System.out.print( totalCosts / lines.size() + "  " );
					
				}
				
			}
			
		}
			
		Scanner in = new Scanner(System.in);
		
		System.out.println("\n REMOVE FILES?");
		
		if (in.nextLine().equals("")) {
		
			for ( Path path : listFilesForFolder(new File(FILEPREFIX + "/Data")) ) deleteFile(path);
			
			for ( Path path : listFilesForFolder(new File(FILEPREFIX + "/Data/js/Data")) ) deleteFile(path);
			
		}
		
		in.close();
		
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
