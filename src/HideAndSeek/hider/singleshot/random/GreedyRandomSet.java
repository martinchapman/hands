package HideAndSeek.hider.singleshot.random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.HidingAgent;
import Utility.Utils;

/**
 * Produces a K size set of random nodes and then
 * heads for those nodes on the graph to hide in them.
 * 
 * Does so on lowest cost edges, as opposed to random ones
 * in the absence of enough knowledge to compute the shortest
 * path.
 * 
 * @author Martin
 *
 */
public class GreedyRandomSet extends RandomSet {

	/**
	 * 
	 */
	private ArrayList<StringVertex> hideSet;
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public GreedyRandomSet(GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations) {
		
		super(graphController, numberOfHideLocations);
		
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

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#getConnectedEdges(HideAndSeek.graph.StringVertex)
	 */
	public List<StringEdge> getConnectedEdges(StringVertex currentNode) {
		
		ArrayList<StringEdge> edges = new ArrayList<StringEdge>(super.getConnectedEdges(currentNode));
		
		Collections.sort(edges);
		
		return edges;
		
	}

}
