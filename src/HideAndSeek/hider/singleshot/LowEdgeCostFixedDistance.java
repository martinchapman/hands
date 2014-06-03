package HideAndSeek.hider.singleshot;

import java.util.List;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;

/**
 * Orders edges according to lowest cost
 * 
 * @author Martin
 *
 */
public class LowEdgeCostFixedDistance extends RandomFixedDistance {

	/**
	 * @param graph
	 */
	public LowEdgeCostFixedDistance(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations) {
		
		super(graphController, numberOfHideLocations);
		
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
