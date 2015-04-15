package HideAndSeek.seeker.singleshot.random;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.SeekingAgent;

/**
 * 
 * Truly random walk that picks an outgoing node at random
 * from each vertex is comes across.
 * 
 * @author Martin
 *
 */
public class RandomWalk extends SeekingAgent {

	/**
	 * @param graphController
	 */
	public RandomWalk( GraphController <StringVertex, StringEdge> graphController ) {

		super(graphController);

	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public StringVertex nextNode(StringVertex currentNode) {
		
		uniquelyVisitNodes = false;
		
		return connectedNode(currentNode);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	public StringVertex startNode() {

		return randomNode();
		
	}

}