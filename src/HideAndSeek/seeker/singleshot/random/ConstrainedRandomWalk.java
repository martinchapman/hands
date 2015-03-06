package HideAndSeek.seeker.singleshot.random;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.Seeker;

public class ConstrainedRandomWalk extends Seeker {

	public ConstrainedRandomWalk(
			GraphController <StringVertex, StringEdge> graphController) {

		super(graphController);

	}

	@Override
	protected StringVertex nextNode(StringVertex currentNode) {

		return connectedNode(currentNode);
	}

	@Override
	protected StringVertex startNode() {

		return randomNode();
		
	}

}
