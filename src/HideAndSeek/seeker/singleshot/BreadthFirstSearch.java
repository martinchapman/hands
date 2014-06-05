package HideAndSeek.seeker.singleshot;

import java.util.HashSet;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.Seeker;

/**
 * Standard BFS implementation tailored to returning to the parent once visiting a child
 * as no assumptions can be made about connectivity between siblings.
 * 
 * @author Martin
 *
 */
public class BreadthFirstSearch extends Seeker {

	/**
	 * @param graph
	 */
	public BreadthFirstSearch(
			GraphController <StringVertex, StringEdge> graphController) {

		super(graphController);
		
		toBeVisited = new HashSet<StringVertex>();
		
	}

	HashSet<StringVertex> toBeVisited;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected StringVertex nextNode(StringVertex currentNode) {

		// For each item that is to be visited
		for ( StringVertex vertex : toBeVisited ) {
			
			// Ensure that all that items children are also in the set
			for ( StringEdge vertexEdge : graphController.edgesOf(vertex) ) {
				
				toBeVisited.add(edgeToTarget(vertexEdge, currentNode));
				
			}
			
		}
		
		
		
		// Return the next item in the set
		return toBeVisited.remove(0);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	protected StringVertex startNode() {
		
		StringVertex startNode = randomNode();
		
		toBeVisited.add(startNode);
		
		return startNode;
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.Seeker#endOfRound()
	 */
	@Override
	public void endOfRound() {
		// TODO Auto-generated method stub
		super.endOfRound();
		
		parents.clear();
		
	}
	
}
