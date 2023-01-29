package org.kclhi.hands.seeker.singleshot.coverage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
 * @author Martin
 *
 * Only modification to superclass is to order nodes by weight prior to DFS.
 */
public class DepthFirstSearchGreedy extends DepthFirstSearch {

	/**
	 * @param graph
	 */
	public DepthFirstSearchGreedy(GraphController <StringVertex, StringEdge> graphController) {

		super(graphController);
	
	}

	/* (non-Javadoc)
	 * @see org.kclhi.hands.seeker.FixedStartDepthFirstSearch#getConnectedEdges(HideAndSeek.graph.StringVertex)
	 */
	public List<StringEdge> getConnectedEdges(StringVertex currentNode) {
		
		ArrayList<StringEdge> edges = new ArrayList<StringEdge>(super.getConnectedEdges(currentNode));
		
		Collections.sort(edges);
		
		return edges;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#getConnectedEdge(HideAndSeek.graph.StringVertex, java.util.List)
	 */
	@Override
	public StringEdge getConnectedEdge(StringVertex currentNode, List<StringEdge> connectedEdges) {
		
		for (StringEdge edge : connectedEdges ) {

			if ( uniquelyVisitedNodes().contains(edgeToTarget(edge, currentNode)) ) continue;
			
			return edge;
			
		}
		
		return connectedEdges.get((int)(Math.random() * connectedEdges.size()));
		
	}

}
