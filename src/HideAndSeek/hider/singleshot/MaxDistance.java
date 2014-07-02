package HideAndSeek.hider.singleshot;

import java.util.HashSet;

import org.jgrapht.alg.DijkstraShortestPath;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.HiderLocalGraph;
import Utility.Utils;

/**
 * Attempts to hide nodes with a maximum possible distance 
 * between them.
 * 
 * @author Martin
 *
 */
public class MaxDistance extends HiderLocalGraph {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 */
	public MaxDistance(GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations) {
		super(graphController, numberOfHideLocations);
		// TODO Auto-generated constructor stub
		
		triedNodes = new HashSet<StringVertex>();
		
		/* Try and set the minimum distance between two hide locations to be 
		 * the diameter of the graph. */
		
		MINDISTANCE = graphController.requestGraphDiameter();
		
		Utils.talk(toString(), "Diameter " + MINDISTANCE);
		
	}

	/**
	 * 
	 */
	private int maxDistance = 0;
	
	/**
	 * 
	 */
	private static double MINDISTANCE;
	
	/**
	 * 
	 */
	private HashSet<StringVertex> triedNodes;
	
	/* 
	 * Guarantees that at least two items will be hidden at max distance from one another?
	 * 
	 * (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#hideHere(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected boolean hideHere(StringVertex vertex) {
		
		Utils.talk(toString(), "Top");
		
		DijkstraShortestPath<StringVertex, StringEdge> DSP = null;
		
		triedNodes.add(vertex);
		
		// First hide location is the starting vertex (NB: won't ever try and hide here again, so ok)
		if ( vertex.equals(startNode()) ) return true; 
		
		// If all nodes have been tried, cannot hide at min distance, so reduce it
		if (triedNodes.size() == (graphController.vertexSet().size() - hideLocations.size())) { 
			
			triedNodes.clear();
			
			// Potentially reduce min distance based on the best longest distance found in the graph;
			MINDISTANCE = maxDistance;
			
			Utils.talk(toString(), "Reducing distance. Now " + MINDISTANCE);
			
		}
		
		Utils.talk(toString(), "Potential hide location: " + vertex);
		
		// If the proposed vertex is not further away than the min distance
		// from all other hide locations, hide here.
		for ( StringVertex location : hideLocations ) {
			
			DSP = new DijkstraShortestPath<StringVertex, StringEdge>(localGraph, vertex, location);
			
			// Try to learn max distance in graph
			if ( DSP.getPathEdgeList().size() > maxDistance ) maxDistance = DSP.getPathEdgeList().size();
			
			Utils.talk(toString(), "Min distance: " + MINDISTANCE + " | Distance to: " + location + " " + DSP.getPathEdgeList().size());
			
			if ( DSP.getPathEdgeList().size() < MINDISTANCE ) return false;
			
		}
		
		triedNodes.clear();
		
		return true;
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#printGameStats()
	 */
	@Override
	public String printGameStats() {
		// TODO Auto-generated method stub
		return super.printGameStats() + ", GraphDiameter, " + graphController.requestGraphDiameter();
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected StringVertex nextNode(StringVertex currentNode) {
		
		super.nextNode(currentNode);
		
		return connectedNode(currentNode);
		
	}
    
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	protected StringVertex startNode() {
		
		return randomNode();
		
	}


}
