package HideAndSeek.seeker.singleshot;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.Seeker;

/**
 * @author Martin
 *
 */
public class BacktrackPath extends Seeker {

	/**
	 * @param graph
	 */
	public BacktrackPath(GraphController <StringVertex, StringEdge> graphController) {
		// TODO Auto-generated constructor stub
		super(graphController);	
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected StringVertex nextNode(StringVertex currentNode) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	protected StringVertex startNode() {
		// TODO Auto-generated method stub
		return null;
	}

}
