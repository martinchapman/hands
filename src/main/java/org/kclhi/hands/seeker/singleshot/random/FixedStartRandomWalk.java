package org.kclhi.hands.seeker.singleshot.random;

import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.seeker.SeekingAgent;

public class FixedStartRandomWalk extends SeekingAgent {

	/**
	 * @param graph
	 */
	public FixedStartRandomWalk(
			GraphController <StringVertex, StringEdge> graphController) {

		super(graphController);

	}

	/**
	 * 
	 */
	protected boolean uniquelyVisitNodes = false;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public StringVertex nextNode(StringVertex currentNode) {

		return connectedNode(currentNode);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	public StringVertex startNode() {

		return firstNode();
		
	}

}
