package HideAndSeek.hider.singleshot;

import java.util.HashSet;

import org.jgrapht.alg.DijkstraShortestPath;

import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.Hider;
import Utility.Utils;

/**
 * Attempts to hide nodes with a maximum possible distance 
 * between them.
 * 
 * @author Martin
 *
 */
public class MaxDistanceHider extends Hider {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 */
	public MaxDistanceHider(HiddenObjectGraph<StringVertex, StringEdge> graph,
			int numberOfHideLocations) {
		super(graph, numberOfHideLocations);
		// TODO Auto-generated constructor stub
		
		triedNodes = new HashSet<StringVertex>();
		
	}

	/**
	 * 
	 */
	private int maxDistance = 0;
	
	/**
	 * 
	 */
	private static int MINDISTANCE = 5;
	
	/**
	 * 
	 */
	private HashSet<StringVertex> triedNodes;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#hideHere(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected boolean hideHere(StringVertex vertex) {
		
		DijkstraShortestPath<StringVertex, StringEdge> DSP = null;
		
		triedNodes.add(vertex);
		
		// First hide location is the starting vertex
		if ( vertex.equals(startNode()) ) return true; 
		
		// If all nodes have been tried, cannot continue, so return true.
		if (triedNodes.size() == (graph.vertexSet().size() - hideLocations.size())) { 
			
			triedNodes.clear();
			
			// Potentially reduce min distance based on the best longest distance found in the graph;
			MINDISTANCE = maxDistance;
			
		}
		
		// If the proposed vertex is not further away than the min distance
		// from all other hide locations, hide here.
		for ( StringVertex location : hideLocations ) {
			
			DSP = new DijkstraShortestPath<StringVertex, StringEdge>(graph, vertex, location);
			
			// Try to learn max distance in graph
			if ( DSP.getPathEdgeList().size() > maxDistance ) maxDistance = DSP.getPathEdgeList().size();
			
			if ( DSP.getPathEdgeList().size() < MINDISTANCE ) return false;
			
		}
		
		triedNodes.clear();
		
		return true;
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected StringVertex nextNode(StringVertex currentNode) {
		
		return connectedNode(currentNode);
		
	}
    
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	protected StringVertex startNode() {
		
		StringVertex[] vertices = new StringVertex[graph.vertexSet().size()];
				
		return graph.vertexSet().toArray(vertices)[0];
		
	}


}
