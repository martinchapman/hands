package HideAndSeek.hider.repeatgame.random.adaptable;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.AdaptiveHiderStrategy;
import HideAndSeek.hider.repeatgame.random.UniqueRandomSetRepeat;
import Utility.ScoreMetric;
import Utility.Utils;

/**
 * @author Martin
 *
 */
public class UniqueRandomSetRepeatAdaptable extends UniqueRandomSetRepeat implements AdaptiveHiderStrategy {

	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public UniqueRandomSetRepeatAdaptable( GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations ) {
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
		//return graphController.requestAverageSeekersPerformance(ScoreMetric.PATH);
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.AdaptiveHiderStrategy#performanceOfSelf()
	 */
	@Override
	public double performanceOfSelf() {
		return 1.0;
	}

}
