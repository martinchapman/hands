package HideAndSeek.seeker.singleshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;

/**
 * @author Martin
 *
 * Only modification to superclass is to order nodes by weight prior to DFS.
 */
public class DepthFirstSearchLowCost extends DepthFirstSearch {

	/**
	 * @param graph
	 */
	public DepthFirstSearchLowCost(
			GraphController <StringVertex, StringEdge> graphController) {

		super(graphController);
	
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.FixedStartDepthFirstSearch#getConnectedEdges(HideAndSeek.graph.StringVertex)
	 * 
	 *
	 */
	protected List<StringEdge> getConnectedEdges(StringVertex currentNode) {
		
		ArrayList<StringEdge> edges = new ArrayList<StringEdge>(graphController.edgesOf(currentNode));
		
		Collections.sort(edges);
		
		return edges;
		
	}

}
