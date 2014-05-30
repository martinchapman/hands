package HideAndSeek.seeker.singleshot;

import java.util.ArrayList;

import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.Seeker;

public class FixedStartRandomWalk extends Seeker {

	public FixedStartRandomWalk(
			HiddenObjectGraph<StringVertex, StringEdge> graph) {

		super(graph);

	}

	@Override
	protected StringVertex nextNode(StringVertex currentNode) {

		return connectedNode(currentNode);
	}

	@Override
	protected StringVertex startNode() {

		StringVertex[] vertices = new StringVertex[graph.vertexSet().size()];
		
		return graph.vertexSet().toArray(vertices)[0];
		
	}

}
