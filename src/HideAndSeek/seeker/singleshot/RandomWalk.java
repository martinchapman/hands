package HideAndSeek.seeker.singleshot;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.Seeker;

/**
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

	/**
	 * 
	 */
	protected boolean uniquelyVisitNodes = false;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected StringVertex nextNode(StringVertex currentNode) {

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
