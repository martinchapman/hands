package HideAndSeek.hider.repeatgame.random.adaptable;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.AdaptiveHiderStrategy;
import HideAndSeek.hider.singleshot.random.RandomSet;
import Utility.ScoreMetric;
import Utility.Utils;

/**
 * @author Martin
 *
 */
public class RandomSetAdaptable extends RandomSet implements AdaptiveHiderStrategy {

	public RandomSetAdaptable(
			GraphController<StringVertex, StringEdge> graphController,
			int numberOfHideLocations) {
		super(graphController, numberOfHideLocations);
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.AdaptiveHiderStrategy#relevanceOfStrategy()
	 */
	@Override
	public double relevanceOfStrategy() {
		return 1.0;
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.AdaptiveHiderStrategy#performanceOfOpponent()
	 */
	@Override
	public double performanceOfOpponent() {
		return 0.0;
		//return graphController.requestAverageSeekersPerformance(ScoreMetric.PATH) / 100;
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.AdaptiveHiderStrategy#performanceOfSelf()
	 */
	@Override
	public double performanceOfSelf() {
		return 1.0;
	}

	

}
