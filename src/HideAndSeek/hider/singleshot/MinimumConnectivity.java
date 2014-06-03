package HideAndSeek.hider.singleshot;

import java.util.HashSet;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.Hider;

/**
 * Attempts to hide nodes with the least possible connectivity.
 * 
 * Best is thus leaf nodes.
 * 
 * @author Martin
 *
 */
public class MinimumConnectivity extends Hider {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 */
	public MinimumConnectivity(GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations) {
		super(graphController, numberOfHideLocations);
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
		if (triedNodes.size() == (graphController.vertexSet().size() - hideLocations.size())) { 
			
			// Increase max connections if none exist at pre-set maximum (e.g. we set out 
			// to find connections of degree 1, but none of these exist).
			MAXCONNECTIONS = minConnections;
			
			triedNodes.clear();
			
			return true; 
				
		}
		
		if (graphController.edgesOf(vertex).size() < minConnections) minConnections = graphController.degreeOf(vertex);
		
		if (graphController.edgesOf(vertex).size() == MAXCONNECTIONS) { 
			
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
	
		return randomNode();
		
	}


}
