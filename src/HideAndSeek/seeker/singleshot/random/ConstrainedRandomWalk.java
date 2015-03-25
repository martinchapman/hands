package HideAndSeek.seeker.singleshot.random;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.SeekingAgent;

public class ConstrainedRandomWalk extends SeekingAgent {

	public ConstrainedRandomWalk(
			GraphController <StringVertex, StringEdge> graphController) {

		super(graphController);

	}

	@Override
	public StringVertex nextNode(StringVertex currentNode) {

		return connectedNode(currentNode);
	}

	@Override
	public StringVertex startNode() {

		return randomNode();
		
	}

}
