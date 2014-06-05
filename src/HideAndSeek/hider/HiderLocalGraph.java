package HideAndSeek.hider;

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
 * @author Martin
 *
 */
public abstract class HiderLocalGraph extends Hider {
	
	/**
	 * 
	 */
	protected HiddenObjectGraph<StringVertex, StringEdge> localGraph;
	
	/**
	 * @param graph
	 */
	public HiderLocalGraph(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations) {
	
		super(graphController, numberOfHideLocations);
		
		localGraph = new HiddenObjectGraph<StringVertex, StringEdge>(StringEdge.class);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected StringVertex nextNode(StringVertex currentNode) {
		
		addUniquelyVisitedNode(currentNode);
		
		// Update the local graph from the current node as the Seeker moves
		for ( StringEdge edge : graphController.edgesOf(currentNode) ) {
			
			localGraph.addVertexIfNonExistent(edge.getSource());
			
			localGraph.addVertexIfNonExistent(edge.getTarget());
			
			localGraph.addEdgeIfNonExistent(edge.getSource(), edge.getTarget());
			
			localGraph.setEdgeWeight(localGraph.getEdge(edge.getSource(), edge.getTarget()), graphController.getEdgeWeight(edge));
			
		}
		
		return null;
		
	}

}
