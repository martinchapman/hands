package HideAndSeek.seeker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import HideAndSeek.GraphTraverser;
import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import Utility.Utils;

/**
 * This seeker constructs its own version of the search graph,
 * in an attempt to gain a more holistic view of the game environment.
 * 
 * Designed to be extended.
 * 
 * @author Martin
 *
 */
public abstract class SeekerLocalGraph extends Seeker {

	/**
	 * 
	 */
	protected HiddenObjectGraph<StringVertex, StringEdge> localGraph;
	
	/**
	 * @param graph
	 */
	public SeekerLocalGraph(GraphController <StringVertex, StringEdge> graphController) {
		
		super(graphController);
		
		localGraph = new HiddenObjectGraph<StringVertex, StringEdge>(StringEdge.class);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected StringVertex nextNode(StringVertex currentNode) {
		
		// Update the local graph from the current node as the Seeker moves
		for ( StringEdge edge : graphController.edgesOf(currentNode) ) {
			
			localGraph.addVertexIfNonExistent(edge.getSource());
			
			localGraph.addVertexIfNonExistent(edge.getTarget());
			
			localGraph.addEdgeIfNonExistent(edge, edge.getSource(), edge.getTarget());
			
		}
		
		return connectedNode(currentNode);
		
	}
	
}
