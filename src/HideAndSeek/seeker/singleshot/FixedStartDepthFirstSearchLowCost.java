package HideAndSeek.seeker.singleshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import Utility.Utils;

/**
 * @author Martin
 *
 * Only modification to superclass is to order nodes by weight prior to DFS.
 */
public class FixedStartDepthFirstSearchLowCost extends FixedStartDepthFirstSearch {

	public FixedStartDepthFirstSearchLowCost(
			HiddenObjectGraph<StringVertex, StringEdge> graph) {

		super(graph);
	
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.FixedStartDepthFirstSearch#getConnectedEdges(HideAndSeek.graph.StringVertex)
	 * 
	 *
	 */
	protected List<StringEdge> getConnectedEdges(StringVertex currentNode) {
		
		ArrayList<StringEdge> edges = new ArrayList<StringEdge>(graph.edgesOf(currentNode));
		
		Collections.sort(edges);
		
		return edges;
		
	}

}
