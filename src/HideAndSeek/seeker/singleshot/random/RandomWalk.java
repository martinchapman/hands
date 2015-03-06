package HideAndSeek.seeker.singleshot.random;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.Seeker;

/**
 * 
 * Truly random walk that picks an outgoing node at random
 * from each vertex is comes across.
 * 
 * @author Martin
 *
 */
public class RandomWalk extends Seeker {

	/**
	 * @param graph
	 */
	public RandomWalk(
			GraphController <StringVertex, StringEdge> graphController) {

		super(graphController);

	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected StringVertex nextNode(StringVertex currentNode) {
		
		uniquelyVisitNodes = false;
		
		return connectedNode(currentNode);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	protected StringVertex startNode() {

		return randomNode();
		
	}

}
