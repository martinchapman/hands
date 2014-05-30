package HideAndSeek.seeker.singleshot;

import java.util.ArrayList;

import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;

public class RandomStartRandomWalk extends FixedStartRandomWalk {

	public RandomStartRandomWalk(
			HiddenObjectGraph<StringVertex, StringEdge> graph) {
		super(graph);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected StringVertex startNode() {
		// TODO Auto-generated method stub
		return randomNode();
	}

}
