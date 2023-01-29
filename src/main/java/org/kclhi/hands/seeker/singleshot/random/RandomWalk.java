package org.kclhi.hands.seeker.singleshot.random;

import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.seeker.SeekingAgent;

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
