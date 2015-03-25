package HideAndSeek.hider;

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
public abstract class HidingAgent extends GraphTraversingAgent implements Runnable, Hider {

	/**
	 * 
	 */
	protected int numberOfHideLocations;
	
	/**
	 * 
	 */
	protected ArrayList<StringVertex> hideLocations;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.HiderTemplate#getHideLocations()
	 */
	@Override
	public ArrayList<StringVertex> getHideLocations() {
		
		return hideLocations;
	
	}

	/**
	 * 
	 */
	protected ArrayList<StringVertex> exploredNodes;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.HiderTemplate#getExploredNodes()
	 */
	@Override
	public ArrayList<StringVertex> getExploredNodes() {
		
		return exploredNodes;
	
	}
	
	/**
	 * @param graph
	 */
	public HidingAgent(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations) {
	
		super(graphController);
		
		this.numberOfHideLocations = numberOfHideLocations;
		
		hideLocations = new ArrayList<StringVertex>();
		
		exploredNodes = new ArrayList<StringVertex>();
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.HiderTemplate#toString()
	 */
	@Override
	public String toString() {
		
		return "h" + name;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.HiderTemplate#addHideLocation(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public void addHideLocation(StringVertex location) {
		
		Utils.talk(toString(), "---------------------------------- Hiding in " + location);
		
		// Hider's local copy of where he has hidden
		hideLocations.add(location); 
		
		graphController.addHideLocation(this, location);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.HiderTemplate#run()
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
		
		StringVertex startNode = startNode();
		
		if ( currentNode != null && currentNode != startNode ) {
			
			graphController.walkPathFromVertexToVertex(this, currentNode, startNode);
			
		} 
		
		currentNode = startNode;
		
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
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.HiderTemplate#printRoundStats()
	 */
	@Override
	public String printRoundStats() {
		
		return super.printRoundStats() + "Cost, " + graphController.latestRoundCosts(this, false) + ",Path," + exploredNodes.toString().replace(",", "");
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.HiderTemplate#printGameStats()
	 */
	@Override
	public String printGameStats() {
		
		return super.printGameStats() + "Cost, " + graphController.averageGameCosts(this);
		
	}
	

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.HiderTemplate#endOfRound()
	 */
	@Override
	public void endOfRound() {
		
		super.endOfRound();
		
		hideLocations.clear();
		
		exploredNodes.clear();
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.HiderTemplate#hideHere(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public abstract boolean hideHere(StringVertex vertex);

}
