package HideAndSeek.seeker.nonhomo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.singleshot.FixedStartDepthFirstSearch;

public class NodeTypeSearcher extends FixedStartDepthFirstSearch {

	public NodeTypeSearcher(HiddenObjectGraph<StringVertex, StringEdge> graph) {
		super(graph);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.FixedStartDepthFirstSearch#getConnectedEdges(HideAndSeek.graph.StringVertex)
	 * 
	 * Visit those edge which lead to a node which connected to three distinct node types first
	 */
	protected List<StringEdge> getConnectedEdges(StringVertex currentNode) {
		
		List<StringEdge> connectedEdges = new ArrayList<StringEdge>();
		
		HashSet<Character> types = new HashSet<Character>();
		
		// For each outgoing edge of this node:
		for ( StringEdge edge : graph.edgesOf(currentNode)) {
			
			types.clear();
			
			// Find the node at the end of this edge, and look at all the outgoing edges from this
			for ( StringEdge targetEdge : graph.edgesOf(edgeToTarget(edge, currentNode)) ) {
				
				/* If a node, connected to an outgoing edge of the current node, is linked to another node
				   whose type has not yet been listed, list it; determine all unique types heading
				   out of this node */
				if ( !types.contains(graph.getNodeType(edgeToTarget(targetEdge, edgeToTarget(edge, currentNode)))) ) {
					
					types.add(graph.getNodeType(edgeToTarget(targetEdge, edgeToTarget(edge, currentNode))));
					
				}
				
			}
			
			if ( types.size() == graph.getNumberOfNodeTypes() ) {
			
				connectedEdges.add(0, edge);
				
			} else {
				
				connectedEdges.add(edge);
				
			}
			
		}
		
		return connectedEdges;
		
	}

}
