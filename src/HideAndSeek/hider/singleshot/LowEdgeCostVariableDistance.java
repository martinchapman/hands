package HideAndSeek.hider.singleshot;

import java.util.List;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;

/**
 * @author Martin
 *
 */
public class LowEdgeCostVariableDistance extends RandomFixedDistance implements Runnable {
	
	/**
	 * @param graph
	 */
	public LowEdgeCostVariableDistance(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations, int minHideDistance) {
	
		super(graphController, numberOfHideLocations);
		
		this.minHideDistance = minHideDistance;

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
