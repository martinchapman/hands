package HideAndSeek.hider;

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
public abstract class Hider extends GraphTraverser implements Runnable {

	/**
	 * 
	 */
	protected int numberOfHideLocations;
	
	/**
	 * 
	 */
	protected ArrayList<StringVertex> hideLocations;
	
	/**
	 * @return
	 */
	public ArrayList<StringVertex> getHideLocations() {
		
		return hideLocations;
	
	}

	/**
	 * 
	 */
	protected ArrayList<StringVertex> exploredNodes;
	
	/**
	 * 
	 */
	private String ID;
	
	/**
	 * @param graph
	 */
	public Hider(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations) {
	
		super(graphController);
		
		this.numberOfHideLocations = numberOfHideLocations;
		
		ID = "" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + Math.random() * 100;

		hideLocations = new ArrayList<StringVertex>();
		
		exploredNodes = new ArrayList<StringVertex>();
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		return "h" + this.getClass().toString().substring(this.getClass().toString().lastIndexOf('.') + 1, this.getClass().toString().length());
		
	}
	
	/**
	 * @param location
	 */
	protected void addHideLocation(StringVertex location) {
		
		Utils.talk(toString(), "---------------------------------- Hiding in " + location);
		
		// Hider's local copy of where he has hidden
		hideLocations.add(location); 
		
		graphController.addHideLocation(this, location);
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		Utils.talk(toString(), "Running " + ID + " " + this.getClass());
		
		startPlaying();
		
		hide();
		
	}
	
	/**
	 * @param searchPath
	 * @return
	 */
	private boolean hide() {
		
		StringVertex currentNode = startNode();
		
		StringVertex nextNode = null;
		
		while (graphController.numberOfHideLocations() != numberOfHideLocations) {
			
			exploredNodes.add(currentNode);
			
			addUniquelyVisitedNode(currentNode);
			
		    if ( !hideLocations.contains(currentNode) && hideHere(currentNode) ) { 
	        		
        		addHideLocation(currentNode);
				
				if (graphController.numberOfHideLocations() == numberOfHideLocations) { break; }
			
        	}
			 
			nextNode = nextNode(currentNode);
			
			addUniquelyVisitedEdge(graphController.getEdge(currentNode, nextNode));
			
			if ( !graphController.fromVertexToVertex(this, currentNode, nextNode) ) { 
				
				Utils.talk(this.toString(), "Error traversing supplied path.");
				
				return false; 
				
			} else {
				
				currentNode = nextNode;
			
			}
			
		}
		
	    return true;
		
	}
	
	/**
	 * @return
	 */
	public String printRoundStats() {
		
		return "Cost, " + graphController.latestRoundCosts(this);
		
	}
	
	/**
	 * @param rounds
	 * @return
	 */
	public String printGameStats() {
		
		return "Cost, " + graphController.requestAverageGameCosts(this) + ", Score, " + graphController.requestAverageHiderScore(this) + ", LeastConnections, " + graphController.getTopologyProperties().degreeOfLeastConnectedNode() + ", MostConnections, " + graphController.getTopologyProperties().degreeOfMostConnectedNode();
		
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
		
		hideLocations.clear();
		
		roundsPassed++;
		
	}
	
	/**
	 * Given a vertex, do I hide in that vertex, based upon
	 * the content of this method
	 * @param vertex
	 * @return
	 */
	protected abstract boolean hideHere(StringVertex vertex);

}
