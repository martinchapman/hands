package Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


/**
 * @author Martin
 *
 */
public class Runner {
	
	/**
	 * 
	 */
	final static String FILEPREFIX = "Output/";
	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[]) {
		
		new Runner();

	}
	
	/**
	 * 
	 */
	public Runner() {
		
		// Collect list of simulations
        ArrayList<String> simulations = Utils.readFromFile(FILEPREFIX + "simulationSchedule.txt");
		
        String currentSimulationIdentifier = "";
        
		for (String currentSimulation : simulations) {
			
			// Allows for replacement of wildcards with static values
			String[] simulationParameters = currentSimulation.split("\\s");
			
			//String[] simulationParameters = Arrays.copyOf(simulationParameters, simulationParameters.length);
			
			// If the simulation is commented out, do not run it.
			if(		simulationParameters[0].equals("//")	) { continue; }
			
			System.out.println("Simulation parameters: " + Arrays.toString(simulationParameters));
			
			/***********/
			
	        // Generate ID For this simulation
			
			currentSimulationIdentifier = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			
			Utils.writeToFile(FILEPREFIX + "simRecordID.txt", currentSimulationIdentifier);
			
			Utils.writeToFile(FILEPREFIX + "/Data/" + currentSimulationIdentifier + ".csv", "");
			
			Utils.writeToFile(FILEPREFIX + "/Data/" + currentSimulationIdentifier + ".csv", Arrays.toString(simulationParameters) + "\n");
				
			/***********/
			
			String[] parameters = { "Hiders", 
					  "Seekers",
	  				  "Topology", // Topology
	  				  "NumberOfNodes", // Number of nodes in graph
	  				  "NumberOfHideLocations", // Number of hide locations
	  				  "Rounds", // rounds 
	  				  "EdgeWeight", // cost of traversing an edge
	  				  "FixedOrUpperWeight", // whether cost supplied is static value or the upper bound of a distribution
	  				  "EdgeTraversalDecrement" // % discount gained by an agent for having traversed an edge before (100 = no discount; < 100 = discount)
	  				  };
	  				  
			String[] defaultParameters = { simulationParameters[1],
					 simulationParameters[2],
					 "random", // Topology
					 "100", // Number of nodes in graph
					 "5", // Number of hide locations
					 "120", // rounds
					 "100.0", // cost of traversing an edge
					 "upper", // whether cost supplied is static value or the upper bound of a distribution
					 "0"// % discount gained by an agent for having traversed an edge before (1.0 = no discount; < 1.0 = discount)
			  		  };
				
			/***********/
			
			final int GAMES = Integer.parseInt(simulationParameters[0]);
			
			// Run 'games' of simulation by repeat running program
			
		    for(int i = 0; i < GAMES; i++) {
			    	
				System.out.println("-----------------------------------------------------------------");
				System.out.println("Run: " + (i + 1));
				System.out.println("-----------------------------------------------------------------");
			    
				// Remove wildcards from param string and replace with values
				
				for (int j = 0; j < simulationParameters.length; j++) {
		    		  
		    	    if (simulationParameters[j].contains("i*")) { 
		    	    	
		    	    	simulationParameters[j] = 
		    	    	
		    	        simulationParameters[j].replaceAll("(i\\*([0-9]+))", 
		    											   "" + (i * Integer.parseInt(
															               simulationParameters[j].substring( 
															            		   Utils.startIndexOf(simulationParameters[j], "(i\\*([0-9]+))") + 2, 
															            		   Utils.endIndexOf(simulationParameters[j], "(i\\*([0-9]+))")
															            		   )
		    													            		  
		    													      		)
		    													)
		    	    									   );
		    	    
		    	    } else if (simulationParameters[j].contains("i+")) { 
		    	    	
		    	    	simulationParameters[j] = 
		    	    	
		    	        simulationParameters[j].replaceAll("(i\\+([0-9]+))", 
		    	        								   "" + (i + Integer.parseInt(
		    	        										   			simulationParameters[j].substring(
		    	        										   					Utils.startIndexOf(simulationParameters[j], "(i\\+([0-9]+))") + 2, 
		    	        										   					Utils.endIndexOf(simulationParameters[j], "(i\\+([0-9]+))")
		    	        										   			)
		    	        										   	  )
		    	        										)
		    	        								   ); 
		    	    
		    	    } else if (simulationParameters[j].contains(",i")) { 
		    	  		
		    	  		simulationParameters[j] = 
		    	  				
		    	  		simulationParameters[j].replaceAll("(\\,i)", "," + i); 
		    	  		
		    	  	}
	    	   
	    	    }
			    
				// Alter the default parameters to reflect those that have been input
				
	            for (String param : simulationParameters) {
	        	  
					  if (param.indexOf('{') != -1) {
					  
						  Pair<String, String> paramPair = Utils.stringToArray(param, "(\\{([0-9a-zA-Z]+),([0-9a-zA-Z]+)\\})").get(0);
						  
						  defaultParameters[Arrays.asList(parameters).indexOf(paramPair.getElement0())] = paramPair.getElement1(); 
						  
					  }
	        	  
	            }
			    
	            // Construct the param string to supply to the program
	            
				String paramString = "";
				  
				for (int k = 0; k < defaultParameters.length; k++) {
					  
					paramString += defaultParameters[k] + " ";
					  
				}
			    
				/***********/
				
				Process proc = null;
				
				try {
					
					proc = Runtime.getRuntime().exec("java -classpath bin:/Users/Martin/Downloads/jgrapht-0.9.0/lib/jgrapht-core-0.9.0.jar HideAndSeek.Main " + i + " " + paramString);
				
				} catch (IOException e1) {
					
					// TODO Auto-generated catch block
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
					
					// TODO Auto-generated catch block
					e1.printStackTrace();
				
				}
				
				try {
				
					while ((line = errors.readLine()) != null) {  
						
						System.out.println(line);  
					
					}
					
				} catch (IOException e1) {
					
					// TODO Auto-generated catch block
					e1.printStackTrace();
				
				} 
				  
				try {
				
					proc.waitFor();
				
				} catch (InterruptedException e) { System.out.println(e); }
			    
		    } // End of game run loop
				
		} // End of simulation loop
 
	}
	
}
