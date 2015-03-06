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
	 * @return
	 */
	public ArrayList<StringVertex> getExploredNodes() {
		
		return exploredNodes;
	
	}

	/**
	 * 
	 */
	private String ID;
	
	/**
	 * @param ID
	 */
	public void setName(String name) {
		
		this.name = name;
		
	}
	
	/**
	 * @param graph
	 */
	public Hider(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations) {
	
		super(graphController);
		
		this.numberOfHideLocations = numberOfHideLocations;
		
		name = this.getClass().toString().substring(this.getClass().toString().lastIndexOf('.') + 1, this.getClass().toString().length());
		
		ID = "" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + Math.random() * 100;

		hideLocations = new ArrayList<StringVertex>();
		
		exploredNodes = new ArrayList<StringVertex>();
		
	}
	
	/**
	 * 
	 */
	private String name;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		return "h" + name;
		
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
	 * 
	 */
	private StringVertex currentNode = null;
	
	/**
	 * @return
	 */
	protected StringVertex getCurrentNode() {
		
		if (currentNode == null) {
			
			return randomNode();
			
		} else {
			
			return currentNode;
			
		}
		
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
	
	/**
	 * @return
	 */
	public String printRoundStats() {
		
		return super.printRoundStats() + "Cost, " + graphController.latestRoundCosts(this) + ",Path," + exploredNodes.toString().replace(",", "");
		
	}
	
	/**
	 * @param rounds
	 * @return
	 */
	public String printGameStats() {
		
		return super.printGameStats() + "Cost, " + graphController.requestAverageGameCosts(this) + 
			 ", Score, " + graphController.requestAverageHiderScore(this);
		
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
		
		exploredNodes.clear();
		
		roundsPassed++;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#endOfGame()
	 */
	public void endOfGame(){
		
		roundsPassed = 0;
		
	}
	
	/**
	 * Given a vertex, do I hide in that vertex, based upon
	 * the content of this method
	 * 
	 * Note: returning 'true', with non-static inter-round
	 * movement, will make this choice, effectively, random.
	 * @param vertex
	 * @return
	 */
	protected abstract boolean hideHere(StringVertex vertex);

}
