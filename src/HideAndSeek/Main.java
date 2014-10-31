package HideAndSeek;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.Hider;
import HideAndSeek.hider.repeatgame.FixedStartVariableBias;
import HideAndSeek.hider.repeatgame.VariableBias;
import HideAndSeek.hider.singleshot.LeastConnected;
import HideAndSeek.hider.singleshot.LowEdgeCostRandomFixedDistance;
import HideAndSeek.hider.singleshot.LowEdgeCostRandomSet;
import HideAndSeek.hider.singleshot.LowEdgeCostVariableFixedDistance;
import HideAndSeek.hider.singleshot.MaxDistance;
import HideAndSeek.hider.singleshot.Random;
import HideAndSeek.hider.singleshot.RandomFixedDistance;
import HideAndSeek.hider.singleshot.RandomFixedDistanceFixedStart;
import HideAndSeek.hider.singleshot.RandomFixedStart;
import HideAndSeek.hider.singleshot.RandomSet;
import HideAndSeek.hider.singleshot.RandomVariableHidePotential;
import HideAndSeek.hider.singleshot.VariableFixedDistance;
import HideAndSeek.hider.singleshot.VariableFixedDistanceFixedStart;
import HideAndSeek.hider.singleshot.VariableLowEdgeCost;
import HideAndSeek.seeker.Seeker;
import HideAndSeek.seeker.repeatgame.HighProbability;
import HideAndSeek.seeker.singleshot.BacktrackPath;
import HideAndSeek.seeker.singleshot.BreadthFirstSearch;
import HideAndSeek.seeker.singleshot.BreadthFirstSearchLowCost;
import HideAndSeek.seeker.singleshot.ConstrainedRandomWalk;
import HideAndSeek.seeker.singleshot.DepthFirstSearch;
import HideAndSeek.seeker.singleshot.DepthFirstSearchLowCost;
import HideAndSeek.seeker.singleshot.FixedStartRandomWalk;
import HideAndSeek.seeker.singleshot.LeastConnectedFirst;
import HideAndSeek.seeker.singleshot.LowEdgeCost;
import HideAndSeek.seeker.singleshot.MostConnectedFirst;
import HideAndSeek.seeker.singleshot.RandomWalk;
import HideAndSeek.seeker.singleshot.VariableBacktrackPath;
import Utility.Pair;
import Utility.Utils;

/**
 * @author Martin
 *
 */
public class Main {
	
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
		
		currentSimulationIdentifier = Utils.readFromFile(Utils.FILEPREFIX + "simRecordID.txt").get(0);
		
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
		
		boolean mixHiders = Boolean.parseBoolean(args[11]);
		
		boolean mixSeekers = Boolean.parseBoolean(args[12]);
		
		//
		
		String agentList;
		
		int rounds = Integer.parseInt(args[7]);
		
		String hiderList = args[2];
		
		String seekerList = args[3];
		
		int numberOfHideLocations = Integer.parseInt(args[6]);
		
		startRounds(initHiders(hiderList, numberOfHideLocations, mixHiders), initSeekers(seekerList, mixSeekers), rounds, true);
		
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
	private List<Hider> initHiders(String agentList, int numberOfHideLocations, boolean mixHiders) {
		
		/**************************
    	 * 
    	 * Set up hiding agents
    	 * 
    	 * * * * * * * * * * * * */
		 
		List<Hider> allHidingAgents = new ArrayList<Hider>();
		
		for( Pair<String, String> hiderType : Utils.stringToArray(agentList, "(\\[([0-9a-zA-Z]+),([0-9]+)\\])") ) {
			
			// Single-shot:
			
			if (hiderType.getElement0().equals("Random")) {
				
				allHidingAgents.add(new Random(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("RandomFixedStart")) {
				
				allHidingAgents.add(new RandomFixedStart(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("RandomVariableHidePotential")) {
				
				allHidingAgents.add(new RandomVariableHidePotential(graphController, numberOfHideLocations, gameNumber / ((float)totalGames)));
			
			} 
			
			if (hiderType.getElement0().equals("RandomDirection")) {
				
				allHidingAgents.add(new VariableFixedDistance(graphController, numberOfHideLocations, 0));
				
				// Have to set ID manually as identifier and class used are different
				allHidingAgents.get(allHidingAgents.size() - 1).setName("RandomDirection");
			
			} 
			
			if (hiderType.getElement0().equals("RandomSet")) {
				
				allHidingAgents.add(new RandomSet(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("LowEdgeCostRandomSet")) {
				
				allHidingAgents.add(new LowEdgeCostRandomSet(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("RandomFixedDistance")) {
				
				allHidingAgents.add(new RandomFixedDistance(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("RandomFixedDistanceFixedStart")) {
				
				allHidingAgents.add(new RandomFixedDistanceFixedStart(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("LowEdgeCostRandomFixedDistance")) {
				
				allHidingAgents.add(new LowEdgeCostRandomFixedDistance(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("VariableFixedDistance")) {
				
				allHidingAgents.add(new VariableFixedDistance(graphController, numberOfHideLocations, gameNumber));
			
			} 
			
			if (hiderType.getElement0().equals("VariableFixedDistanceFixedStart")) {
				
				allHidingAgents.add(new VariableFixedDistanceFixedStart(graphController, numberOfHideLocations, gameNumber));
			
			} 
			
			if (hiderType.getElement0().equals("LowEdgeCostVariableFixedDistance")) {
				
				allHidingAgents.add(new LowEdgeCostVariableFixedDistance(graphController, numberOfHideLocations, gameNumber));
			
			} 
			
			if (hiderType.getElement0().equals("MinimumConnectivity")) {
				
				allHidingAgents.add(new LeastConnected(graphController, numberOfHideLocations));
			
			} 

			if (hiderType.getElement0().equals("MaxDistance")) {
				
				allHidingAgents.add(new MaxDistance(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("LowEdgeCost")) {
				
				allHidingAgents.add(new VariableLowEdgeCost(graphController, numberOfHideLocations, 1.0));
			
			} 
			
			if (hiderType.getElement0().equals("LowEdgeCost")) {
				
				allHidingAgents.add(new VariableLowEdgeCost(graphController, numberOfHideLocations, 1.0));
			
			} 

			if (hiderType.getElement0().equals("VariableLowEdgeCost")) {
				
				allHidingAgents.add(new VariableLowEdgeCost(graphController, numberOfHideLocations, gameNumber / (float)totalGames));
			
			} 
			
			// Repeat-game:
			
			if (hiderType.getElement0().equals("FullyBias")) {
				
				allHidingAgents.add(new VariableBias(graphController, numberOfHideLocations, 1.0));
				
				allHidingAgents.get(allHidingAgents.size() - 1).setName("FullyBias");
			
			}
			
			if (hiderType.getElement0().equals("FullyExplorative")) {
				
				allHidingAgents.add(new VariableBias(graphController, numberOfHideLocations, 0.0));
				
				allHidingAgents.get(allHidingAgents.size() - 1).setName("FullyExplorative");
			
			}
			
			if (hiderType.getElement0().equals("LooselyBias")) {
				
				allHidingAgents.add(new VariableBias(graphController, numberOfHideLocations, 0.5));
				
				allHidingAgents.get(allHidingAgents.size() - 1).setName("LooselyBias");
			
			} 
			
			if (hiderType.getElement0().equals("VariableBias")) {
				
				allHidingAgents.add(new VariableBias(graphController, numberOfHideLocations, gameNumber / (float)totalGames));
			
			} 
			
			if (hiderType.getElement0().equals("FixedStartVariableBias")) {
				
				allHidingAgents.add(new FixedStartVariableBias(graphController, numberOfHideLocations, gameNumber / (float)totalGames));
			
			} 
			
			//
			
		}
		
		if (mixHiders) {
			
			Collections.shuffle(allHidingAgents);
			
			allHidingAgents = allHidingAgents.subList(0, 1);
			
			System.out.println("Strat: " + allHidingAgents);
			
		}
		
		return allHidingAgents;
		
	}
	
	/**
	 * @param agentList
	 * @return
	 */
	private List<Seeker> initSeekers(String agentList, boolean mixSeekers) {
		
		/**************************
    	 * 
    	 * Set up seeking agents
    	 * 
    	 * * * * * * * * * * * * */
		
		List<Seeker> allSeekingAgents = new ArrayList<Seeker>();
		 
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
			
			if (seekerType.getElement0().equals("MostConnectedFirst")) {
				
				allSeekingAgents.add(new MostConnectedFirst(graphController));
				
			}
			
			if (seekerType.getElement0().equals("BacktrackPath")) {
				
				allSeekingAgents.add(new BacktrackPath(graphController));
				
			}
			
			if (seekerType.getElement0().equals("VariableBacktrackPath")) {
				
				allSeekingAgents.add(new VariableBacktrackPath(graphController, gameNumber));
				
			}
			
			// Optimal backtrack path -- found by experimentation
			if (seekerType.getElement0().equals("OptimalBacktrackPath")) {
				
				allSeekingAgents.add(new VariableBacktrackPath(graphController, 1));
				
				allSeekingAgents.get(allSeekingAgents.size() - 1).setName("OptimalBacktrackPath");
				
			}

			// Repeat-game: 
			
			if (seekerType.getElement0().equals("HighProbability")) {
				
				allSeekingAgents.add(new HighProbability(graphController));
				
			}
			
			
		}
		
		if (mixSeekers) {
			
			Collections.shuffle(allSeekingAgents);
			
			allSeekingAgents = allSeekingAgents.subList(0, 1);
			
			System.out.println("Strat: " + allSeekingAgents);
			
		}

		return allSeekingAgents;
		
	}
	
	/**
	 * Rounds are designed to re-test the same parameter configurations (which may vary between games)
	 * multiples times AND to allow for patterns or histories to develop
	 * 
	 * @param list2
	 * @param list
	 * @param rounds
	 * @param recordPerRound
	 */
	private void startRounds(List<Hider> hiders, List<Seeker> seekers, int rounds, boolean recordPerRound) {
		
		// Pre-round outputting
		
		FileWriter mainOutputWriter = null, outputJavascript = null, outputHTML = null;
		
		try {
			
			mainOutputWriter = new FileWriter(Utils.FILEPREFIX + "/data/" + currentSimulationIdentifier + ".csv", true);
		
			outputJavascript = new FileWriter(Utils.FILEPREFIX + "/data/js/data/" + currentSimulationIdentifier + "-vis.js", true);
			
			outputHTML = new FileWriter(Utils.FILEPREFIX + "/data/" + currentSimulationIdentifier + "-vis.html", true);
        
		} catch (IOException e) {
		
			e.printStackTrace();
		
		}
		
		Utils.writeToFile(outputJavascript, "var graphNodes = \"" + graphController.edgeSet(this) + "\"; \n var hidden = new Array(); \n var path = new Array(); \n");
		
		/**************************
    	 * 
    	 * Main rounds loop
    	 * 
    	 * * * * * * * * * * * * */
		
		// Run rounds and record output per hider
		for ( Hider hider : hiders ) {
			
			Utils.talk("Main", hiders.toString());
			
			for (int i = 0; i < rounds; i++) {
	        	
	        	Utils.talk("Main", "Game " + gameNumber + " Round " + i);
	        	
	        	System.out.println( "Game " + gameNumber + " Round " + i + ": " + ( ( i / ( ( (float) rounds * hiders.size() ) ) ) * 100 ) + "%" );
	        	
				hider.run();
				
				for ( Seeker seeker : seekers ) {
					
					seeker.run();
					
				}
				
				graphController.clearHideLocations(this);
	    		
	    		// Visualise first hider and first seeker, for novelty, mainly.
	    		
	    		Utils.writeToFile(outputJavascript, "hidden[" + i + "] = \"" + hiders.get(0).getHideLocations() + "\"; \n");
	    		
	    		Utils.writeToFile(outputJavascript, "path[" + i + "] = \"" + graphController.latestRoundPaths(this, seekers.get(0)) + "\"; \n");
	    		
	    		//
	    		
	    		if (recordPerRound) {
	        		
	    			Utils.writeToFile(mainOutputWriter, "R, " + hider.toString() + "," + hider.printRoundStats() + ",");
	    			
	    			Utils.talk("Main", hider.toString() + "," + hider.printRoundStats());
	    			
	    			for( Seeker seeker : seekers ) {
	    				
	    				Utils.writeToFile(mainOutputWriter, seeker.toString() + "," + seeker.printRoundStats());
	    				
	    				Utils.talk("Main ", seeker.toString() + "," + seeker.printRoundStats());
	    				
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
	    		
	    	ArrayList<String> javascriptOutputTemplate = Utils.readFromFile(Utils.FILEPREFIX + "data/js/vis-template.js");
	    	
	    	for ( String line : javascriptOutputTemplate ) {
	    		
	    		Utils.writeToFile(outputJavascript, line + "\n");
	    		
	    	}
	    			
			ArrayList<String> firstHalfHTMLTemplate = Utils.readFromFile(Utils.FILEPREFIX + "data/template/vis-template-1.html");
			
			for (String line : firstHalfHTMLTemplate) {
			
				Utils.writeToFile(outputHTML, line + "\n");
				
			}
			
			Utils.writeToFile(outputHTML, "<script type=\"text/javascript\" src=\"js/data/" + currentSimulationIdentifier + "-vis.js\"></script>");
	    	
			ArrayList<String> secondHalfHTMLTemplate = Utils.readFromFile(Utils.FILEPREFIX + "data/template/vis-template-2.html");
			
			for (String line : secondHalfHTMLTemplate) {
				
				Utils.writeToFile(outputHTML, line + "\n");
				
			}
			
			// Output hider stats
			
			Utils.talk("Main", "End of game \n------------------------------------------");
			
			hider.endOfGame();
			
			System.out.println("Main" + hider.toString() + "," + hider.printGameStats());
			
			//if ( !recordPerRound ) {
			
				Utils.writeToFile(mainOutputWriter, "G, " + hider.toString() + "," + hider.printGameStats() + ",");
				
		    	// Output costs for Seekers
			
				for ( Seeker seeker : seekers ) {
				
					seeker.endOfGame();
					
					Utils.talk("Main", seeker.toString() + "," + seeker.printGameStats());
					
					// Cost per round
					
					Utils.writeToFile(mainOutputWriter, seeker.toString() + "," + seeker.printGameStats() + ",");
					
				}
				
				Utils.writeToFile(mainOutputWriter, "\n");
			
			//}
			
			graphController.nextHider(this);
			
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
