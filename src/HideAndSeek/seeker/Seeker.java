package HideAndSeek.seeker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import HideAndSeek.GraphTraverser;
import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import Utility.Utils;

/**
 * @author Martin
 *
 */
public abstract class Seeker extends GraphTraverser implements Runnable {

	/**
	 * 
	 */
	protected String ID;
	
	/**
	 * 
	 */
	protected String name;
	
	/**
	 * @param name
	 */
	public void setName(String name) {
		
		this.name = name;
	
	}

	/**
	 * 
	 */
	protected ArrayList<StringVertex> hideLocations;
	
	/**
	 * 
	 */
	protected ArrayList<StringVertex> exploredNodes;

	/**
	 * @param graph
	 */
	public Seeker(GraphController <StringVertex, StringEdge> graphController) {
		
		super(graphController);
		
		// ID according to time of generation
		ID = "" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + Math.random() * 100;
		
		// Record of where hidden items have been found
		hideLocations = new ArrayList<StringVertex>();
		
		// List of nodes that have been explored
		exploredNodes = new ArrayList<StringVertex>();
		
		name = this.getClass().toString().substring(this.getClass().toString().lastIndexOf('.') + 1, this.getClass().toString().length());
		
	}
	
	/**
	 * @param name
	 * @param graphController
	 */
	public Seeker(String name, GraphController <StringVertex, StringEdge> graphController) {
		
		this(graphController);
		
		this.name = name;
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		return "s" + name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		Utils.talk(toString(), "Running " + ID + " " + name);
		
		search();
		
	}
	
	/**
	 * What happens when a node is found
	 * 
	 * @param location
	 */
	protected void addHideLocation(StringVertex location) {
		
		Utils.talk(this.toString(), "----------------------------------Found " + location);
		
		hideLocations.add(location); 
		
	}
	
	/**
	 * @return
	 */
	protected void search() {
		
		StringVertex currentNode = startNode();
		
		StringVertex nextNode;
		
		while ( hideLocations.size() != graphController.numberOfHideLocations() ) {
			
			//Utils.talk(this.toString(), "At: " + currentNode);
			
			exploredNodes.add(currentNode);
			
			addUniquelyVisitedNode(currentNode);
			
			 if ( graphController.isHideLocation(currentNode) && !hideLocations.contains(currentNode) ) { 
	        		
        		addHideLocation(currentNode);
				
				if (hideLocations.size() == graphController.numberOfHideLocations()) { break; }
			
        	}
			 
			nextNode = nextNode(currentNode);
			
			addUniquelyVisitedEdge(graphController.getEdge(currentNode, nextNode));
			
			if ( !graphController.fromVertexToVertex(this, currentNode, nextNode) ) { 
				
				Utils.talk(this.toString(), "Error traversing supplied path.");
				
			} else {
			
				currentNode = nextNode;
				
			}
			 
		}
		
	}
	
	/**
	 * @return
	 */
	public String printRoundStats() {
		
		return super.printRoundStats() + "Cost, " + graphController.latestRoundCosts(this);
		
	}
	
	/**
	 * @param rounds
	 * @return
	 */
	public String printGameStats() {
		
		return super.printGameStats() +
			   "Cost, " + graphController.requestAverageGameCosts(this)  + 
			   ", Score, " + graphController.requestAverageSeekerScore(this) + 
			   ", PathLength, " + graphController.requestAveragePathLength(this);
		
	}
	
	/**
	 * Record of the number of rounds passed 
	 */
	protected int roundsPassed = 0;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#endOfRound()
	 */
	public void endOfRound() {
		
		super.endOfRound();
		
		exploredNodes.clear();
		
		hideLocations.clear();
		
		roundsPassed++;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#endOfGame()
	 */
	public void endOfGame() {
		
		super.endOfGame();
		
		roundsPassed = 0;
		
	}
	
}
