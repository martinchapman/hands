package HideAndSeek.seeker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

import HideAndSeek.GraphTraverser;
import HideAndSeek.graph.HiddenObjectGraph;
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
	protected ArrayList<StringVertex> hideLocations;
	
	/**
	 * 
	 */
	protected ArrayList<StringVertex> exploredNodes;

	/**
	 * @param graph
	 */
	public Seeker(HiddenObjectGraph<StringVertex, StringEdge> graph) {
		
		super(graph);
		
		// ID according to time of generation
		ID = "" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + Math.random() * 100;
		
		// Record of where hidden items have been found
		hideLocations = new ArrayList<StringVertex>();
		
		// List of nodes that have been explored
		exploredNodes = new ArrayList<StringVertex>();
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		return this.getClass().toString().substring(this.getClass().toString().lastIndexOf('.') + 1, this.getClass().toString().length());
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		System.out.println("Running " + ID + " " + this.getClass());
		
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
	private boolean search() {
		
		StringVertex currentNode = startNode();
		
		StringVertex nextNode;
		
		while (hideLocations.size() != graph.numberOfHideLocations()) {
	
			exploredNodes.add(currentNode);
			
			uniquelyVisitedNodes.add(currentNode);
			
			 if ( graph.isHideLocation(currentNode) && !hideLocations.contains(currentNode) ) { 
	        		
        		addHideLocation(currentNode);
				
				if (hideLocations.size() == graph.numberOfHideLocations()) { break; }
			
        	}
			 
			nextNode = nextNode(currentNode);
			
			if ( !graph.fromVertexToVertex(this, currentNode, nextNode) ) { 
				
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
		
		Utils.talk(this.toString(), "" + graph.latestRoundPaths(this));
		
		return graph.latestRoundPaths(this).size() + "," + graph.edgeSet().size() + "," + ((graph.latestRoundPaths(this).size() / ((double)graph.edgeSet().size())) * 100) + ",";
		
	}
	
	/**
	 * @param rounds
	 * @return
	 */
	public String printGameStats() {
		
		return "";
		
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
	
}
