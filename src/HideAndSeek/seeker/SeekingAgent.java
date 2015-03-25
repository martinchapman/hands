package HideAndSeek.seeker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import HideAndSeek.GraphTraversingAgent;
import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import Utility.Utils;

/**
 * @author Martin
 *
 */
public abstract class SeekingAgent extends GraphTraversingAgent implements Runnable, Seeker {

	/**
	 * 
	 */
	protected ArrayList<StringVertex> hideLocations;
	
	/**
	 * All hide locations encountered in a game.
	 */
	protected ArrayList<StringVertex> allHideLocations;
	
	/**
	 * 
	 */
	protected ArrayList<StringVertex> exploredNodes;

	/**
	 * @param graph
	 */
	public SeekingAgent(GraphController <StringVertex, StringEdge> graphController) {
		
		super(graphController);
		
		// ID according to time of generation
		ID = "" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + Math.random() * 100;
		
		// Record of where hidden items have been found
		hideLocations = new ArrayList<StringVertex>();
		
		// Record of where hidden items have been found (in the whole game)
		allHideLocations = new ArrayList<StringVertex>();
		
		// List of nodes that have been explored
		exploredNodes = new ArrayList<StringVertex>();
		
		name = this.getClass().toString().substring(this.getClass().toString().lastIndexOf('.') + 1, this.getClass().toString().length());
		
	}
	
	/**
	 * @param name
	 * @param graphController
	 */
	public SeekingAgent(String name, GraphController <StringVertex, StringEdge> graphController) {
		
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
	public void addHideLocation(StringVertex location) {
		
		Utils.talk(this.toString(), "----------------------------------Found " + location);
		
		hideLocations.add(location); 
		
		allHideLocations.add(location);
		
	}
	
	/**
	 * @return
	 */
	public void search() {
		
		StringVertex startNode = startNode();
		
		if ( currentNode != null && currentNode != startNode ) {
			
			graphController.walkPathFromVertexToVertex(this, currentNode, startNode);
			
		} 
		
		currentNode = startNode;
		
		StringVertex nextNode = null;
		
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
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.SeekerTemplate#printRoundStats()
	 */
	@Override
	public String printRoundStats() {
		
		return super.printRoundStats() + "Cost, " + graphController.latestRoundCosts(this, false);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.SeekerTemplate#printGameStats()
	 */
	@Override
	public String printGameStats() {
		
		return super.printGameStats() +
			   "Cost, " + graphController.averageGameCosts(this)  +  
			   ", PathLength, " + graphController.averagePathLength(this);
		
	}
	
	/**
	 * Record of the number of rounds passed 
	 */
	protected int roundsPassed = 0;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#endOfRound()
	 */
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.SeekerTemplate#endOfRound()
	 */
	@Override
	public void endOfRound() {
		
		super.endOfRound();
		
		exploredNodes.clear();
		
		hideLocations.clear();
		
		roundsPassed++;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#endOfGame()
	 */
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.SeekerTemplate#endOfGame()
	 */
	@Override
	public void endOfGame() {
		
		super.endOfGame();
		
		roundsPassed = 0;
		
		allHideLocations.clear();
		
	}
	
}
