package HideAndSeek.hider.singleshot;

import java.util.ArrayList;

import org.jgrapht.alg.DijkstraShortestPath;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.HiderLocalGraph;
import Utility.Utils;

/**
 * Produces a K size set of random nodes and then
 * heads for those nodes on the graph to hide in them
 * 
 * @author Martin
 *
 */
public class RandomSet extends HiderLocalGraph {

	/**
	 * 
	 */
	private ArrayList<StringVertex> hideSet;
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public RandomSet(GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations) {
		
		super(graphController, numberOfHideLocations);
		
		hideSet = new ArrayList<StringVertex>();
		
		currentPath = new ArrayList<StringEdge>();
		
		populateHideSet();
		
	}
	
	/**
	 * 
	 */
	private void populateHideSet() {
		
		StringVertex randomVertex = randomNode();
		
		while ( hideSet.size() != numberOfHideLocations ) {
			
			if (!hideSet.contains(randomVertex)) {
				
				hideSet.add(randomVertex);
				
			}
			
			randomVertex = randomNode();
			
		}
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#hideHere(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected boolean hideHere(StringVertex vertex) {

		Utils.talk(toString(), "Current node: " + vertex + " Random set: " + hideSet + " Hide locations: " + hideLocations);
		
		// If we land on a node in our random set, hide here
		if ( hideSet.contains(vertex) ) { 
			
			hideSet.remove(vertex);
			
			return true;
		
		}
		
		return false;
		
	}
  
	/**
	 * 
	 */
	ArrayList<StringEdge> currentPath;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.HiderLocalGraph#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected StringVertex nextNode(StringVertex currentNode) {
		
		super.nextNode(currentNode);
		
		// If we're on a path to a node in the random set, follow this first
		if ( currentPath.size() > 0 ) {
			
			return edgeToTarget(currentPath.remove(0), currentNode);
			
		}
		
		// Otherwise, try and calculate a path to the next node in the set
		if ( hideSet.size() > 0 && localGraph.containsVertex( hideSet.get(0) ) ) {
			
			DijkstraShortestPath<StringVertex, StringEdge> dsp = new DijkstraShortestPath<StringVertex, StringEdge>(localGraph, currentNode, hideSet.get(0));
	    	
			// If no path available, return random connected node
			if (dsp.getPathEdgeList() == null || dsp.getPathEdgeList().size() == 0) return connectedNode(currentNode);
			
			currentPath = new ArrayList<StringEdge>(dsp.getPathEdgeList());
			
			return edgeToTarget(currentPath.remove(0), currentNode);
			
		}
		
		return connectedNode(currentNode);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	protected StringVertex startNode() {
		
		return randomNode();
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#endOfRound()
	 */
	@Override
	public void endOfRound() {
		
		super.endOfRound();
		
		populateHideSet();
		
	}

}
