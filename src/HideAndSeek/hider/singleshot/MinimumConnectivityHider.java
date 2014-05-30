package HideAndSeek.hider.singleshot;

import java.util.HashSet;

import org.jgrapht.alg.DijkstraShortestPath;

import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.Hider;
import Utility.Utils;

/**
 * Attempts to hide nodes with the least possible connectivity.
 * 
 * Best is thus leaf nodes.
 * 
 * @author Martin
 *
 */
public class MinimumConnectivityHider extends Hider {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 */
	public MinimumConnectivityHider(HiddenObjectGraph<StringVertex, StringEdge> graph,
			int numberOfHideLocations) {
		super(graph, numberOfHideLocations);
		// TODO Auto-generated constructor stub
		
		triedNodes = new HashSet<StringVertex>();
		
	}

	/**
	 * 
	 */
	private int minConnections = Integer.MAX_VALUE;
	
	/**
	 * 
	 */
	private static int MAXCONNECTIONS = 1;
	
	/**
	 * 
	 */
	private HashSet<StringVertex> triedNodes;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#hideHere(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected boolean hideHere(StringVertex vertex) {
		
		triedNodes.add(vertex);
		
		// If all nodes have been tried, cannot continue, so return true.
		if (triedNodes.size() == (graph.vertexSet().size() - hideLocations.size())) { 
			
			// Increase max connections if none exist at pre-set maximum (e.g. we set out 
			// to find connections of degree 1, but none of these exist).
			MAXCONNECTIONS = minConnections;
			
			triedNodes.clear();
			
			return true; 
				
		}
		
		if (graph.edgesOf(vertex).size() < minConnections) minConnections = graph.degreeOf(vertex);
		
		if (graph.edgesOf(vertex).size() == MAXCONNECTIONS) { 
			
			triedNodes.clear(); 
			
			return true; 
			
		}
		
		return false;
		
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
