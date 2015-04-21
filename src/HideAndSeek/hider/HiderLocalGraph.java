package HideAndSeek.hider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import HideAndSeek.GraphTraversingAgent;
import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import Utility.Utils;

/**
 * @author Martin
 *
 */
public abstract class HiderLocalGraph extends HidingAgent {
	
	/**
	 * 
	 */
	protected HiddenObjectGraph<StringVertex, StringEdge> localGraph;
	
	/**
	 * 
	 */
	protected static final boolean KNOWS_VERTICES = true;
	
	/**
	 * @param graph
	 */
	public HiderLocalGraph(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations) {
	
		super(graphController, numberOfHideLocations);
		
		localGraph = new HiddenObjectGraph<StringVertex, StringEdge>(StringEdge.class);
		
		for ( StringVertex vertex : graphController.vertexSet() ) {
			
			if ( KNOWS_VERTICES ) localGraph.addVertex(vertex);
			
		}
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public StringVertex nextNode(StringVertex currentNode) {
		
		addUniquelyVisitedNode(currentNode);
		
		// Update the local graph from the current node as the Seeker moves
		for ( StringEdge edge : getConnectedEdges(currentNode) ) {
			
			localGraph.addVertexIfNonExistent(edge.getSource());
			
			localGraph.addVertexIfNonExistent(edge.getTarget());
			
			localGraph.addEdgeIfNonExistent(edge.getSource(), edge.getTarget());
			
			localGraph.setEdgeWeight(localGraph.getEdge(edge.getSource(), edge.getTarget()), graphController.getEdgeWeight(edge));
			
		}
		
		return null;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#randomNode()
	 */
	protected StringVertex knownRandomNode() {
		
		StringVertex[] vertices = new StringVertex[localGraph.vertexSet().size()];
		
		localGraph.vertexSet().toArray(vertices);
		
		if ( vertices.length == 0 ) { return null; }
		
		return vertices[(int)(Math.random() * vertices.length)];
		
	}

}
