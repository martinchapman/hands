package hideandseek.seeker.singleshot.random;

import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;
import hideandseek.seeker.SeekingAgent;

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
