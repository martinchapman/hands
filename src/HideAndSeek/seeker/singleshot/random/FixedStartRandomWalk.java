package HideAndSeek.seeker.singleshot.random;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.SeekingAgent;

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
