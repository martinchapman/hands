package HideAndSeek;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.jgrapht.VertexFactory;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.generate.GraphGenerator;
import org.jgrapht.generate.RandomGraphGenerator;
import org.jgrapht.generate.RingGraphGenerator;
import org.jgrapht.generate.ScaleFreeGraphGenerator;
import org.jgrapht.graph.ClassBasedVertexFactory;

import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.Hider;
import HideAndSeek.hider.singleshot.FixedDistanceHider;
import HideAndSeek.hider.singleshot.LowEdgeCostFixedDistanceHider;
import HideAndSeek.hider.singleshot.MinimumConnectivityHider;
import HideAndSeek.hider.singleshot.VariableDistanceHider;
import HideAndSeek.seeker.Seeker;
import HideAndSeek.seeker.repeatgame.adaptive.AdaptiveVariableHighProbabilitySeeker;
import HideAndSeek.seeker.singleshot.FixedStartDepthFirstSearch;
import HideAndSeek.seeker.singleshot.FixedStartDepthFirstSearchLowCost;
import HideAndSeek.seeker.singleshot.FixedStartGreedy;
import HideAndSeek.seeker.singleshot.FixedStartRandomWalk;
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
	private final static String FILEPREFIX = "/Users/Martin/Dropbox/University/PhD/2013/33. 12th August - 16th August/Simulations/";
	
	/**
	 * 
	 */
	private int gameNumber;
	
	/**
	 * 
	 */
	private String currentSimulationIdentifier = "";
	
	/**
	 * Graph 
	 */
	private HiddenObjectGraph<StringVertex, StringEdge> graph;
	
	/**
	 * @param args
	 */
	public Main(String[] args) {
		
		currentSimulationIdentifier = Utils.readFromFile(FILEPREFIX + "simRecordID.txt").get(0);
		
		System.out.println(currentSimulationIdentifier);
		
		Utils.talk("Main", "Simulation parameters " + Arrays.toString(args));
		
		gameNumber = Integer.parseInt(args[0]);
		
		//
		
		String topology = args[3];
		
		int numberOfVertices = Integer.parseInt(args[4]);
		
		String fixedOrUpperBound = args[8];
		
		double fixedOrUpperValue = Double.parseDouble(args[7]);
		
		int edgeTraversalDecrement = Integer.parseInt(args[9]);
		
		initGraph(topology, numberOfVertices, fixedOrUpperBound, fixedOrUpperValue, edgeTraversalDecrement);
		
		//
		
		String agentList;
		
		int rounds = Integer.parseInt(args[6]);
		
		String hiderList = args[1];
		
		String seekerList = args[2];
		
		int numberOfHideLocations = Integer.parseInt(args[5]);
		
		//startRounds(initHiders(hiderList, numberOfHideLocations), initSeekers(seekerList), rounds, false);
		
		startRounds(initHiders(numberOfHideLocations), initSeekers(), rounds, false);
		
	}
	
	/**
	 * @param args
	 */
	private void initGraph(String topology, int numberOfVertices, String fixedOrUpperBound, double fixedOrUpperValue, int edgeTraversalDecrement) {
		
		/**************************
    	 * 
    	 * Set up search graph
    	 * 
    	 * * * * * * * * * * * * */
		
		graph = new HiddenObjectGraph<StringVertex, StringEdge>(StringEdge.class);
		
		graph.setEdgeTraversalDecrement(edgeTraversalDecrement);
		
		GraphGenerator<StringVertex, StringEdge, StringVertex> generator = null;
		
		// Select generator
		
		if (	topology.equals("ring")	  ) {
	    	
	        generator = new RingGraphGenerator<StringVertex, StringEdge>(numberOfVertices);
	        
    	} else if (	   topology.equals("random")    ) {
    		
    		generator = new RandomGraphGenerator<StringVertex, StringEdge>(numberOfVertices, numberOfVertices * 3);
    		
    	} else if (    topology.equals("scalefree")    ) {
    		
    		generator = new ScaleFreeGraphGenerator<StringVertex, StringEdge>(numberOfVertices);
    		
    	}
		
		
		// Generate graph
		
		Utils.talk("Main", "Generating graph.");
		
		VertexFactory<StringVertex> factory = new ClassBasedVertexFactory<StringVertex>(StringVertex.class);
		
		ConnectivityInspector<StringVertex, StringEdge> con = new ConnectivityInspector<StringVertex, StringEdge>(graph);
		
		do {
			
			generator.generateGraph(graph, factory, null);
			
			if (!con.isGraphConnected()) { graph.removeAllEdges(graph.edgeSet()); }
			
		} while ( !con.isGraphConnected() );
		
		Utils.talk("Main", "Graph generated. \n" + graph.edgeSet());
		
		
		// Assign nodes types
		
		graph.setNodeTypes(new char[]{ 'A', 'B', 'C' });
		
		
		// Set edge weights
		
		for ( StringEdge edge : graph.edgeSet() ) {
			
			if ( fixedOrUpperBound.equals("fixed") ) {
				
				graph.setEdgeWeight(edge, fixedOrUpperValue);
				
			} else if (fixedOrUpperBound.equals("upper")) {
				
				graph.setEdgeWeight(edge, Math.random() * fixedOrUpperValue);
				
			}
			
		}
		
		System.out.println(graph.edgeSet().size());
		
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
		
		for( Pair<String, String> seekerType : Utils.stringToArray(agentList, "(\\[([0-9a-zA-Z]+),([0-9]+)\\])") ) {
			
			if (seekerType.getElement0().equals("FixedDistanceHider")) {
				
				allHidingAgents.add(new FixedDistanceHider(graph, numberOfHideLocations));
			
			} else if (seekerType.getElement0().equals("VariableDistanceHider")) {
				
				allHidingAgents.add(new VariableDistanceHider(graph, numberOfHideLocations, gameNumber));
				
			} else if (seekerType.getElement0().equals("LowEdgeCostHider")) {
				
				allHidingAgents.add(new LowEdgeCostFixedDistanceHider(graph, numberOfHideLocations));
				
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
			
			if (seekerType.getElement0().equals("FixedStartRandomWalk")) {
				
				allSeekingAgents.add(new FixedStartRandomWalk(graph));
				
			} else if (seekerType.getElement0().equals("FixedStartDepthFirstSearch")) {
				
				allSeekingAgents.add(new FixedStartDepthFirstSearch(graph));
				
			} else if (seekerType.getElement0().equals("FixedStartDepthFirstSearchLowCost")) {
				
				allSeekingAgents.add(new FixedStartDepthFirstSearchLowCost(graph));
				
			} else if (seekerType.getElement0().equals("FixedStartGreedy")) {
				
				allSeekingAgents.add(new FixedStartGreedy(graph));
				
			}
			
		}
		
		return allSeekingAgents;
		
	}
	
	/**
	 * @param agentList
	 * @param numberOfHideLocations
	 * @return
	 */
	private ArrayList<Hider> initHiders(int numberOfHideLocations) {
		
		/**************************
    	 * 
    	 * Set up hiding agents
    	 * 
    	 * * * * * * * * * * * * */
		 
		ArrayList<Hider> allHidingAgents = new ArrayList<Hider>();
		
		allHidingAgents.add(new MinimumConnectivityHider(graph, numberOfHideLocations));
		
		return allHidingAgents;
		
	}
	
	/**
	 * @param agentList
	 * @return
	 */
	private ArrayList<Seeker> initSeekers() {
		
		/**************************
    	 * 
    	 * Set up seeking agents
    	 * 
    	 * * * * * * * * * * * * */
		
		ArrayList<Seeker> allSeekingAgents = new ArrayList<Seeker>();
		
		allSeekingAgents.add(new AdaptiveVariableHighProbabilitySeeker(graph));
		
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
		
		Utils.writeToFile(outputJavascript, "var graphNodes = \"" + graph.edgeSet() + "\"; \n var hidden = new Array(); \n var path = new Array(); \n");
		
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
				
				graph.clearHideLocations();
	    		
	    		// Visualise first hider and first seeker, for novelty, mainly.
	    		
	    		Utils.writeToFile(outputJavascript, "hidden[" + i + "] = \"" + hiders.get(0).getHideLocations() + "\"; \n");
	    		
	    		Utils.writeToFile(outputJavascript, "path[" + i + "] = \"" + graph.latestRoundPaths(seekers.get(0)) + "\"; \n");
	    		
	    		//
	    		
	    		if (recordPerRound) {
	        		
	    			Utils.writeToFile(mainOutputWriter, "Round " + i + ",");
	        		
	    			Utils.writeToFile(mainOutputWriter, hider.toString() + "," + hider.printRoundStats() + ",");
	    			
	    			for( Seeker seeker : seekers ) {
	    				
	    				Utils.writeToFile(mainOutputWriter, seeker.toString() + "," + seeker.printRoundStats());
	    				
	    			}
	    			
	    			Utils.writeToFile(mainOutputWriter, "\n");
	    			
	        	}
	    		
	    		graph.notifyEndOfRound();
	    		
	    		hider.endOfRound();
	    		
	    		for (Seeker seeker : seekers) {
	    			
	    			seeker.endOfRound();
	    			
	    		}
	    		
    			Utils.talk("Main", "Score: " + hider + " " + graph.latestHiderRoundScores(hider));
	    			
			}
			
			//
	    		
	    	ArrayList<String> javascriptOutputTemplate = Utils.readFromFile(FILEPREFIX + "Data/js/vis-template.js");
	    	
	    	for ( String line : javascriptOutputTemplate ) {
	    		
	    		Utils.writeToFile(outputJavascript, line + "\n");
	    		
	    	}
	    			
			ArrayList<String> firstHalfHTMLTemplate = Utils.readFromFile(FILEPREFIX + "Data/vis-template-1.html");
			
			for (String line : firstHalfHTMLTemplate) {
			
				Utils.writeToFile(outputHTML, line + "\n");
				
			}
			
			Utils.writeToFile(outputHTML, "<script type=\"text/javascript\" src=\"js/Data/" + currentSimulationIdentifier + "-vis.js\"></script>");
	    	
			ArrayList<String> secondHalfHTMLTemplate = Utils.readFromFile(FILEPREFIX + "Data/vis-template-2.html");
			
			for (String line : secondHalfHTMLTemplate) {
				
				Utils.writeToFile(outputHTML, line + "\n");
				
			}
			
			// Output hider stats
			
			Utils.talk("Main", "End of game \n------------------------------------------");
			
			Utils.talk("Main", hider.toString() + "," + hider.printGameStats());
			
			Utils.writeToFile(mainOutputWriter, hider.toString() + "," + hider.printGameStats() + ",");
			
	    	// Output costs for Seekers
		
			for ( Seeker seeker : seekers ) {
			
				Utils.talk("Main", seeker.toString() + "," + seeker.printGameStats());
				
				// Average cost per round
				
				Utils.writeToFile(mainOutputWriter, seeker.toString() + "," + seeker.printGameStats() + ",");
				
			}
			
			if (recordPerRound) Utils.writeToFile(mainOutputWriter, "\n");
			
			graph.newGame();
			
		} // End of hider loop
		
		Utils.writeToFile(mainOutputWriter, "\n");
		
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
