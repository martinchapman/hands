package HideAndSeek.hider.nonhomo;

import java.util.HashSet;

import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.Hider;

/**
 * @author Martin
 *
 */
public class NodeTypeRestrictedHider extends Hider {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 */
	public NodeTypeRestrictedHider(
			HiddenObjectGraph<StringVertex, StringEdge> graph,
			int numberOfHideLocations) {
		super(graph, numberOfHideLocations);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#hideHere(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected boolean hideHere(StringVertex vertex) {
		
		HashSet<Character> types = new HashSet<Character>();
		
		/* Permit hide if node is connected to other nodes of all types
		  (mimics connectivity constraints) */
		for ( StringEdge edge : graph.edgesOf(vertex)) {
			
			if ( !types.contains(graph.getNodeType(edgeToTarget(edge, vertex))) ) {
				
				types.add(graph.getNodeType(edgeToTarget(edge, vertex)));
				
			}
			
		}
		
		if (types.size() == graph.getNumberOfNodeTypes()) return true;
		
		return false;
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected StringVertex nextNode(StringVertex currentNode) {
		// TODO Auto-generated method stub
		return connectedNode(currentNode);
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	protected StringVertex startNode() {
		
		StringVertex[] vertices = new StringVertex[graph.vertexSet().size()];
		
		return graph.vertexSet().toArray(vertices)[0];
		
	}

}
