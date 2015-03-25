package Utility;

import HideAndSeek.GraphTraversingAgent;
import HideAndSeek.graph.GraphController;

public class AdaptiveUtils {
	
	/**
	 * For a given traverser, returns a number between 0.0 (bad) and 1.0 (best) denoting
	 * the performance of this traverser based upon the reduction in cost ('cost score') 
	 * they have achieved between this round and the last. 
	 * 
	 * @param graphController
	 * @param traverser
	 * @return
	 */
	public static double performanceOfSelfUnderCostChange(GraphController<?, ?> graphController, GraphTraversingAgent traverser) {
		
	
		// -1 indicates that not enough information is available to calculate change (for example, if it is an early round).
		if ( graphController.latestTraverserRoundPerformance(traverser, ScoreMetric.COST_CHANGE) == -1 ) return 1.0; 
		
		System.out.println("= " + graphController.latestTraverserRoundPerformance(traverser, ScoreMetric.COST_CHANGE));
		
		// Any improvement in cost score is seen as wholly positive
		if ( graphController.latestTraverserRoundPerformance(traverser, ScoreMetric.COST_CHANGE) > 0 ) { 
			
			System.out.println("A: 1.0");
			return 1.0;
		
		/* Any loss in cost score, greater than -100%, is taken as an absolute value, and inverted, as an
		 * indicator of performance. For example, -40%, would indicated a 60% (0.6) performance, as decreasing
		 * by 40% is not as bad as, say, -60%, which would indicate 40% (0.4) performance.
		 */
			
		} else if ( graphController.latestTraverserRoundPerformance(traverser, ScoreMetric.COST_CHANGE) < 0 &&
				    graphController.latestTraverserRoundPerformance(traverser, ScoreMetric.COST_CHANGE) > -100) {
			
			System.out.println("B: " + (1.0 - (Math.abs(graphController.latestTraverserRoundPerformance(traverser, ScoreMetric.COST_CHANGE)) / 100)));
			
			return 1.0 - (Math.abs(graphController.latestTraverserRoundPerformance(traverser, ScoreMetric.COST_CHANGE)) / 100);
		
		// Anything else must be greater than an increase in double of cost, and should thus indicate that change is necessary.
		} else {
			
			System.out.println("C: 0");
			
			return 0.0;

		}
				
	}

}
