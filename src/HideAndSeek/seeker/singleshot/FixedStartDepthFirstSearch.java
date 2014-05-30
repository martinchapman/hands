package HideAndSeek.seeker.singleshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.Seeker;
import Utility.Utils;

/**
 * @author Martin
 *
 */
public class FixedStartDepthFirstSearch extends Seeker {

	/**
	 * @param graph
	 */
	public FixedStartDepthFirstSearch(
			HiddenObjectGraph<StringVertex, StringEdge> graph) {

		super(graph);
		
		currentBranch = new ArrayList<StringEdge>();

	}

	/**
	 * 
	 */
	protected ArrayList<StringEdge> currentBranch;

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected StringVertex nextNode(StringVertex currentNode) {

		List<StringEdge> connectedEdges = getConnectedEdges(currentNode);
		
		StringEdge connectedEdge = null;
		
		StringVertex target = null;
		
		HashSet<StringEdge> selectedInThisSession = new HashSet<StringEdge>();
		
		do {
			
			// For each possible outgoing edge:
			for ( StringEdge edge : connectedEdges ) {
				
				// Check if it leads to a node which has already been visited
				if ( !uniquelyVisitedNodes.contains( edgeToTarget( edge, currentNode ) ) ) {
					
					connectedEdge = edge;
					
					// Edge found, break from loop.
					break;
					
				}
				
			}
			
			if ( connectedEdge == null ) {
				
				// If all edges have been traversed, move upwards through the current branch
				return edgeToTarget( currentBranch.remove(currentBranch.size() - 1), currentNode );
			
			} else {
				
				target = edgeToTarget(connectedEdge, currentNode);
				
				selectedInThisSession.add(connectedEdge);
				
			}
			
					// Loop while not allowed to repeat nodes BUT
		} while (   uniquelyVisitNodes == true && uniquelyVisitedNodes.contains( target ) &&
				    // only if we haven't already tried all outgoing edges available
				    selectedInThisSession.size() != connectedEdges.size()  );
		
		currentBranch.add(connectedEdge);
		
		return target;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	protected StringVertex startNode() {

		StringVertex[] vertices = new StringVertex[graph.vertexSet().size()];
		
		return graph.vertexSet().toArray(vertices)[0];
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.Seeker#endOfRound()
	 */
	@Override
	public void endOfRound() {
		// TODO Auto-generated method stub
		super.endOfRound();
		
		currentBranch.clear();
		
	}
	
}
