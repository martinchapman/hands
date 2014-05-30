package HideAndSeek.hider.singleshot;

import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.Hider;

/**
 * 
 * Weaker version of truly random hider, which only moves randomly, but 
 * hides in the first K nodes moved to.
 * 
 * @author Martin
 *
 */
public class RandomDirectionHider extends Hider {

	public RandomDirectionHider(
			HiddenObjectGraph<StringVertex, StringEdge> graph,
			int numberOfHideLocations) {
		super(graph, numberOfHideLocations);
		
	}

	@Override
	protected boolean hideHere(StringVertex vertex) {
		
		return true;
		
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
