package HideAndSeek.seeker.singleshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.SeekerLocalGraph;
import Utility.Utils;

/**
 * Extends parent to first order connected nodes by weight, so
 * child nodes are visited with the lowest cost first
 * 
 * @author Martin
 *
 */
public class BreadthFirstSearchLowCost extends BreadthFirstSearch {

	/**
	 * @param graph
	 */
	public BreadthFirstSearchLowCost(GraphController <StringVertex, StringEdge> graphController) {

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


