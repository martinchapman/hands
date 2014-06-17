package HideAndSeek;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.Hider;
import HideAndSeek.hider.repeatgame.VariableBiasHider;
import HideAndSeek.hider.singleshot.LowEdgeCostRandomFixedDistance;
import HideAndSeek.hider.singleshot.LowEdgeCostVariableFixedDistance;
import HideAndSeek.hider.singleshot.MaxDistance;
import HideAndSeek.hider.singleshot.MinimumConnectivity;
import HideAndSeek.hider.singleshot.Random;
import HideAndSeek.hider.singleshot.RandomDirection;
import HideAndSeek.hider.singleshot.RandomFixedDistance;
import HideAndSeek.hider.singleshot.RandomSet;
import HideAndSeek.hider.singleshot.VariableFixedDistance;
import HideAndSeek.seeker.Seeker;
import HideAndSeek.seeker.repeatgame.HighProbabilitySeeker;
import HideAndSeek.seeker.singleshot.BacktrackPath;
import HideAndSeek.seeker.singleshot.BreadthFirstSearch;
import HideAndSeek.seeker.singleshot.BreadthFirstSearchLowCost;
import HideAndSeek.seeker.singleshot.ConstrainedRandomWalk;
import HideAndSeek.seeker.singleshot.DepthFirstSearch;
import HideAndSeek.seeker.singleshot.DepthFirstSearchLowCost;
import HideAndSeek.seeker.singleshot.FixedStartRandomWalk;
import HideAndSeek.seeker.singleshot.LeastConnectedFirst;
import HideAndSeek.seeker.singleshot.LowEdgeCost;
import HideAndSeek.seeker.singleshot.RandomWalk;
import Utility.Pair;
import Utility.Utils;

/**
 * @author Martin
 *
 */
public class Main {

	/**
	 * Data
	 */
	private final static String FILEPREFIX = "Output/";
	
	/**
	 * 
	 */
	private int gameNumber;
	
	/**
	 * 
	 */
	private int totalGames;
	
	/**
	 * 
	 */
	private String currentSimulationIdentifier = "";
	
	/**
	 * Graph 
	 */
	private GraphController<StringVertex, StringEdge> graphController;
	
	/**
	 * @param args
	 */
	public Main(String[] args) {
		
		currentSimulationIdentifier = Utils.readFromFile(FILEPREFIX + "simRecordID.txt").get(0);
		
		System.out.println(currentSimulationIdentifier);
		
		Utils.talk("Main", "Simulation parameters " + Arrays.toString(args));
		
		gameNumber = Integer.parseInt(args[0]);
		
		totalGames = Integer.parseInt(args[1]);
		
		//
		
		String topology = args[4];
		
		int numberOfVertices = Integer.parseInt(args[5]);
		
		String fixedOrUpperBound = args[9];
		
		double fixedOrUpperValue = Double.parseDouble(args[8]);
		
		int edgeTraversalDecrement = Integer.parseInt(args[10]);
		
		initGraph(topology, numberOfVertices, fixedOrUpperBound, fixedOrUpperValue, edgeTraversalDecrement);
		
		//
		
		String agentList;
		
		int rounds = Integer.parseInt(args[7]);
		
		String hiderList = args[2];
		
		String seekerList = args[3];
		
		int numberOfHideLocations = Integer.parseInt(args[6]);
		
		startRounds(initHiders(hiderList, numberOfHideLocations), initSeekers(seekerList), rounds, false);
		
	}
	
	/**
	 * @param args
	 */
	private void initGraph(String topology, int numberOfVertices, String fixedOrUpperBound, double fixedOrUpperValue, int edgeTraversalDecrement) {
		
		graphController = new GraphController<StringVertex, StringEdge>(topology, numberOfVertices, fixedOrUpperBound, fixedOrUpperValue, edgeTraversalDecrement);
		
	}
	
	/**
	 * @param agentList
	 * @param numberOfHideLocations
	 * @return
	 */
	private ArrayList<Hider> initHiders(String agentList, int numberOfHideLocations) {
		
		/**************************
    	 * 
    	 * Set up hiding agents
    	 * 
    	 * * * * * * * * * * * * */
		 
		ArrayList<Hider> allHidingAgents = new ArrayList<Hider>();
		
		for( Pair<String, String> hiderType : Utils.stringToArray(agentList, "(\\[([0-9a-zA-Z]+),([0-9]+)\\])") ) {
			
			// Single-shot:
			
			if (hiderType.getElement0().equals("Random")) {
				
				allHidingAgents.add(new Random(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("RandomDirection")) {
				
				allHidingAgents.add(new RandomDirection(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("RandomSet")) {
				
				allHidingAgents.add(new RandomSet(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("RandomFixedDistance")) {
				
				allHidingAgents.add(new RandomFixedDistance(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("LowEdgeCostRandomFixedDistance")) {
				
				allHidingAgents.add(new LowEdgeCostRandomFixedDistance(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("VariableFixedDistance")) {
				
				allHidingAgents.add(new VariableFixedDistance(graphController, numberOfHideLocations, gameNumber));
			
			} 
			
			if (hiderType.getElement0().equals("LowEdgeCostVariableFixedDistance")) {
				
				allHidingAgents.add(new LowEdgeCostVariableFixedDistance(graphController, numberOfHideLocations, gameNumber));
			
			} 
			
			if (hiderType.getElement0().equals("MinimumConnectivity")) {
				
				allHidingAgents.add(new MinimumConnectivity(graphController, numberOfHideLocations));
			
			} 

			if (hiderType.getElement0().equals("MaxDistance")) {
				
				allHidingAgents.add(new MaxDistance(graphController, numberOfHideLocations));
			
			} 
			
			// Repeat-game:
			
			if (hiderType.getElement0().equals("FullyBiasHider")) {
				
				allHidingAgents.add(new VariableBiasHider(graphController, numberOfHideLocations, 1.0));
			
			}
			
			if (hiderType.getElement0().equals("LooselyBiasHider")) {
				
				allHidingAgents.add(new VariableBiasHider(graphController, numberOfHideLocations, 1.0));
			
			} 
			
			if (hiderType.getElement0().equals("VariableBiasHider")) {
				
				allHidingAgents.add(new VariableBiasHider(graphController, numberOfHideLocations, gameNumber/((float)totalGames)));
			
			} 
			
			
			
		}
		
		return allHidingAgents;
		
	}
	
	/**
	 * @param agentList
	 * @return
	 */
	private ArrayList<Seeker> initSeekers(String agentList) {
		
		/**************************
    	 * 
    	 * Set up seeking agents
    	 * 
    	 * * * * * * * * * * * * */
		
		ArrayList<Seeker> allSeekingAgents = new ArrayList<Seeker>();
		 
		for( Pair<String, String> seekerType : Utils.stringToArray(agentList, "(\\[([0-9a-zA-Z]+),([0-9]+)\\])") ) {
			
			// Single-shot:
			
			if (seekerType.getElement0().equals("RandomWalk")) {
				
				allSeekingAgents.add(new RandomWalk(graphController));
				
			}
			
			if (seekerType.getElement0().equals("ConstrainedRandomWalk")) {
				
				allSeekingAgents.add(new ConstrainedRandomWalk(graphController));
				
			}
			
			if (seekerType.getElement0().equals("FixedStartRandomWalk")) {
				
				allSeekingAgents.add(new FixedStartRandomWalk(graphController));
				
			}
			
			if (seekerType.getElement0().equals("LowEdgeCost")) {
				
				allSeekingAgents.add(new LowEdgeCost(graphController));
				
			}
			
			if (seekerType.getElement0().equals("DepthFirstSearch")) {
				
				allSeekingAgents.add(new DepthFirstSearch(graphController));
				
			}
			
			if (seekerType.getElement0().equals("DepthFirstSearchLowCost")) {
				
				allSeekingAgents.add(new DepthFirstSearchLowCost(graphController));
				
			}
			
			if (seekerType.getElement0().equals("BreadthFirstSearch")) {
				
				allSeekingAgents.add(new BreadthFirstSearch(graphController));
				
			}
			
			if (seekerType.getElement0().equals("BreadthFirstSearchLowCost")) {
				
				allSeekingAgents.add(new BreadthFirstSearchLowCost(graphController));
				
			}
			
			if (seekerType.getElement0().equals("LeastConnectedFirst")) {
				
				allSeekingAgents.add(new LeastConnectedFirst(graphController));
				
			}
			
			if (seekerType.getElement0().equals("BacktrackPath")) {
				
				allSeekingAgents.add(new BacktrackPath(graphController));
				
			}
			
			// Repeat-game: 
			
			if (seekerType.getElement0().equals("HighProbabilitySeeker")) {
				
				allSeekingAgents.add(new HighProbabilitySeeker(graphController));
				
			}
			
			
		}
		
		return allSeekingAgents;
		
	}
	
	/**
	 * Rounds are designed to re-test the same parameter configurations (which may vary between games)
	 * multiples times AND to allow for patterns or histories to develop
	 * 
	 * @param hiders
	 * @param seekers
	 * @param rounds
	 * @param recordPerRound
	 */
	private void startRounds(ArrayList<Hider> hiders, ArrayList<Seeker> seekers, int rounds, boolean recordPerRound) {
		
		// Pre-round outputting
		
		FileWriter mainOutputWriter = null, outputJavascript = null, outputHTML = null;
		
		try {
			
			mainOutputWriter = new FileWriter(FILEPREFIX + "/Data/" + currentSimulationIdentifier + ".csv", true);
		
			outputJavascript = new FileWriter(FILEPREFIX + "/Data/js/Data/" + currentSimulationIdentifier + "-vis.js", true);
			
			outputHTML = new FileWriter(FILEPREFIX + "/Data/" + currentSimulationIdentifier + "-vis.html", true);
        
		} catch (IOException e) {
		
			e.printStackTrace();
		
		}
		
		Utils.writeToFile(outputJavascript, "var graphNodes = \"" + graphController.edgeSet(this) + "\"; \n var hidden = new Array(); \n var path = new Array(); \n");
		
		if (recordPerRound) {
    		
			Utils.writeToFile(mainOutputWriter, "------------	START OF GAME	------------ \n");
    	
		}
		
		/**************************
    	 * 
    	 * Main rounds loop
    	 * 
    	 * * * * * * * * * * * * */
		
		// Run rounds and record output per hider
		for ( Hider hider : hiders ) {
			
			for (int i = 0; i < rounds; i++) {
	        	
	        	Utils.talk("Main", "Game " + gameNumber + " Round " + i);
	        	
				hider.run();
				
				Utils.talk("Main", hider.toString() + "," + hider.printRoundStats());
				
				for ( Seeker seeker : seekers ) {
					
					seeker.run();
					
					Utils.talk("Main", seeker.toString() + "," + seeker.printRoundStats());
					
				}
				
				graphController.clearHideLocations(this);
	    		
	    		// Visualise first hider and first seeker, for novelty, mainly.
	    		
	    		Utils.writeToFile(outputJavascript, "hidden[" + i + "] = \"" + hiders.get(0).getHideLocations() + "\"; \n");
	    		
	    		Utils.writeToFile(outputJavascript, "path[" + i + "] = \"" + graphController.latestRoundPaths(this, seekers.get(0)) + "\"; \n");
	    		
	    		//
	    		
	    		if (recordPerRound) {
	        		
	    			Utils.writeToFile(mainOutputWriter, "Round " + i + ",");
	        		
	    			Utils.writeToFile(mainOutputWriter, hider.toString() + "," + hider.printRoundStats() + ",");
	    			
	    			for( Seeker seeker : seekers ) {
	    				
	    				Utils.writeToFile(mainOutputWriter, seeker.toString() + "," + seeker.printRoundStats());
	    				
	    			}
	    			
	    			Utils.writeToFile(mainOutputWriter, "\n");
	    			
	        	}
	    		
	    		graphController.notifyEndOfRound(this);
	    		
	    		hider.endOfRound();
	    		
	    		for (Seeker seeker : seekers) {
	    			
	    			seeker.endOfRound();
	    			
	    		}
	    		
			}
			
			//
	    		
	    	ArrayList<String> javascriptOutputTemplate = Utils.readFromFile(FILEPREFIX + "Data/js/vis-template.js");
	    	
	    	for ( String line : javascriptOutputTemplate ) {
	    		
	    		Utils.writeToFile(outputJavascript, line + "\n");
	    		
	    	}
	    			
			ArrayList<String> firstHalfHTMLTemplate = Utils.readFromFile(FILEPREFIX + "Data/Template/vis-template-1.html");
			
			for (String line : firstHalfHTMLTemplate) {
			
				Utils.writeToFile(outputHTML, line + "\n");
				
			}
			
			Utils.writeToFile(outputHTML, "<script type=\"text/javascript\" src=\"js/Data/" + currentSimulationIdentifier + "-vis.js\"></script>");
	    	
			ArrayList<String> secondHalfHTMLTemplate = Utils.readFromFile(FILEPREFIX + "Data/Template/vis-template-2.html");
			
			for (String line : secondHalfHTMLTemplate) {
				
				Utils.writeToFile(outputHTML, line + "\n");
				
			}
			
			// Output hider stats
			
			Utils.talk("Main", "End of game \n------------------------------------------");
			
			hider.endOfGame();
			
			Utils.talk("Main", hider.toString() + "," + hider.printGameStats());
			
			Utils.writeToFile(mainOutputWriter, hider.toString() + "," + hider.printGameStats() + ",");
			
	    	// Output costs for Seekers
		
			for ( Seeker seeker : seekers ) {
			
				seeker.endOfGame();
				
				Utils.talk("Main", seeker.toString() + "," + seeker.printGameStats());
				
				// Average cost per round
				
				Utils.writeToFile(mainOutputWriter, seeker.toString() + "," + seeker.printGameStats() + ",");
				
			}
			
			if (recordPerRound) Utils.writeToFile(mainOutputWriter, "\n");
			
			graphController.newGame(this);
			
			Utils.writeToFile(mainOutputWriter, "\n");
			
		} // End of hider loop
		
		try {
			
			mainOutputWriter.close();
			
			outputJavascript.close();
			
			outputHTML.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @param hiders
	 * @param seekers
	 * @return
	 */
	private ArrayList<GraphTraverser> allTraversers(ArrayList<Hider> hiders, ArrayList<Seeker> seekers) {
		
		ArrayList<GraphTraverser> traversers = new ArrayList<GraphTraverser>();
		
		for (Hider hider : hiders) {
			
			traversers.add(hider);
			
		}
		
		for (Seeker seeker : seekers) {
			
			traversers.add(seeker);
			
		}
		
		return traversers;
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		new Main(args);

	}

}
