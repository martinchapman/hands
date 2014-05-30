package HideAndSeek.seeker.singleshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.Seeker;

public class FixedStartGreedy extends Seeker {

	public FixedStartGreedy(
			HiddenObjectGraph<StringVertex, StringEdge> graph) {

		super(graph);

	}

	protected List<StringEdge> getConnectedEdges(StringVertex currentNode) {
		
		ArrayList<StringEdge> edges = new ArrayList<StringEdge>(graph.edgesOf(currentNode));
		
		Collections.sort(edges);
		
		return edges;
		
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
