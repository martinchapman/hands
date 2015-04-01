package HideAndSeek.hider.singleshot.preference.adaptable;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.AdaptiveHider;
import HideAndSeek.hider.singleshot.preference.LeastConnected;

/**
 * 
 * Attempts to work out how many minimum connectivity nodes are in a graph, and thus 
 * if it is worth attempting to hide in them.
 * 
 * @author Martin
 *
 */
public class LeastConnectedAdaptable extends LeastConnected implements AdaptiveHider {

	public LeastConnectedAdaptable(
			GraphController<StringVertex, StringEdge> graphController,
			int numberOfHideLocations) {
		super(graphController, numberOfHideLocations);
	}

	
	/**
	 * Relevance of strategy based upon smallest connections in graph, as a portion of the
	 * total nodes in the graph, inverted, such that the smaller the minimum connection,
	 * the more relevant the strategy.
	 * 
	 * To add precision, we also consider the *number* of minimal connections.
	 * 
	 * @return
	 */
	@Override
	public double relevanceOfStrategy() {
		
		return (graphController.vertexSet().size() - maxConnections) / graphController.vertexSet().size();
	
	}

	/**
	 * @return
	 */
	@Override
	public double performanceOfOpponent() {
		return -1;
	}

	/**
	 * @return
	 */
	@Override
	public double performanceOfSelf() {
		return -1;
	}
	
	/**
	 * 
	 */
	@Override
	public void stopStrategy() {}
	
}
