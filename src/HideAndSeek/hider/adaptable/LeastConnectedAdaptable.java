package HideAndSeek.hider.adaptable;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.AdaptiveHiderStrategy;
import HideAndSeek.hider.singleshot.preference.LeastConnected;

/**
 * 
 * Attempts to work out how many minimum connectivity nodes are in a graph, and thus 
 * if it is worth attempting to hide in them.
 * 
 * @author Martin
 *
 */
public class LeastConnectedAdaptable extends LeastConnected implements AdaptiveHiderStrategy {

	public LeastConnectedAdaptable(
			GraphController<StringVertex, StringEdge> graphController,
			int numberOfHideLocations) {
		super(graphController, numberOfHideLocations);
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.AdaptiveGraphTraverserStrategy#relevanceOfStrategy()
	 */
	@Override
	public double relevanceOfStrategy() {
		return 100;
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.AdaptiveGraphTraverserStrategy#performanceOfOpponent()
	 */
	@Override
	public double performanceOfOpponent() {
		return 100;
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.AdaptiveGraphTraverserStrategy#performanceOfSelf()
	 */
	@Override
	public double performanceOfSelf() {
		return 100;
	}

	
}
