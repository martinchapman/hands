package HideAndSeek.hider.singleshot;

import java.util.List;

import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;

/**
 * Orders edges according to lowest cost
 * 
 * @author Martin
 *
 */
public class LowEdgeCostFixedDistanceHider extends RandomFixedDistanceHider {

	/**
	 * @param graph
	 */
	public LowEdgeCostFixedDistanceHider(HiddenObjectGraph<StringVertex, StringEdge> graph, int numberOfHideLocations) {
		
		super(graph, numberOfHideLocations);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#getConnectedNode(java.util.List)
	 */
	@Override
	protected StringEdge getConnectedEdge(StringVertex currentNode, List<StringEdge> connectedEdges) {
		
		StringEdge minimumEdge = null;
		
		double minimumEdgeCost = Double.MAX_VALUE;
		
		for ( StringEdge edge : connectedEdges ) {
			
			if ( edge.getWeight() < minimumEdgeCost ) {
				
				minimumEdge = edge;
				minimumEdgeCost = edge.getWeight();
				
			}
			
		}
		
		return minimumEdge;
		
	}

}
