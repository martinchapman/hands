package HideAndSeek.hider.singleshot;

import java.util.ArrayList;
import java.util.List;

import HideAndSeek.GraphTraverser;
import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import Utility.Utils;

/**
 * @author Martin
 *
 */
public class LowEdgeCostVariableDistanceHider extends FixedDistanceHider implements Runnable {
	
	/**
	 * @param graph
	 */
	public LowEdgeCostVariableDistanceHider(HiddenObjectGraph<StringVertex, StringEdge> graph, int numberOfHideLocations, int minHideDistance) {
	
		super(graph, numberOfHideLocations);
		
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
