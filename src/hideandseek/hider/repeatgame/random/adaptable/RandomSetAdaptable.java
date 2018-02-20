package hideandseek.hider.repeatgame.random.adaptable;

import java.util.ArrayList;

import Utility.Metric;
import Utility.Utils;
import Utility.adaptive.AdaptiveMeasure;
import Utility.adaptive.AdaptiveUtils;
import Utility.adaptive.AdaptiveWeightings;
import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;
import hideandseek.hider.AdaptiveHider;
import hideandseek.hider.singleshot.random.RandomSet;

/**
 * @author Martin
 *
 */
public class RandomSetAdaptable extends RandomSet implements AdaptiveHider {

	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public RandomSetAdaptable(
			GraphController<StringVertex, StringEdge> graphController,
			int numberOfHideLocations) {
		super(graphController, numberOfHideLocations);
		// Auto-generated constructor
	}

	/**
	 * @param graphController
	 * @param name
	 * @param numberOfHideLocations
	 */
	public RandomSetAdaptable(
			GraphController<StringVertex, StringEdge> graphController,
			String name, int numberOfHideLocations) {
		super(graphController, name, numberOfHideLocations);
		// Auto-generated constructor
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.AdaptiveGraphTraverser#environmentalMeasure()
	 */
	@Override
	public AdaptiveMeasure environmentalMeasure() {
		
		return new AdaptiveMeasure(0.0);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.AdaptiveGraphTraverser#socialMeasure()
	 */
	@Override
	public AdaptiveMeasure socialMeasure() {
		
		return new AdaptiveMeasure(0.0);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.AdaptiveGraphTraverser#internalMeasure(java.util.ArrayList)
	 */
	@Override
	public AdaptiveMeasure internalMeasure(ArrayList<Double> roundStrategyPerformance) {
		
		Utils.talk(toString(), "0.33 * AdaptiveUtils.internalMeasure(roundStrategyPerformance, Metric.RELATIVE_COST, localGraph) ) " + ( 0.33 * AdaptiveUtils.internalMeasure(roundStrategyPerformance, Metric.RELATIVE_COST, localGraph ) ) );
		Utils.talk(toString(), "0.33 * AdaptiveUtils.internalMeasure(roundStrategyPerformance, Metric.COST_CHANGE_PAYOFF, localGraph) ) " + ( 0.33 * AdaptiveUtils.internalMeasure(roundStrategyPerformance, Metric.COST_CHANGE_PAYOFF, localGraph ) ) );
		Utils.talk(toString(), "0.33 * AdaptiveUtils.internalMeasure(roundStrategyPerformance, Metric.TOTAL_EDGE_COST, localGraph) ) " + ( 0.33 * AdaptiveUtils.internalMeasure(roundStrategyPerformance, Metric.TOTAL_EDGE_COST, localGraph ) ) );
		
		return new AdaptiveMeasure( ( 0.33 * AdaptiveUtils.internalMeasure(roundStrategyPerformance, Metric.RELATIVE_COST, localGraph) ) + ( 0.33 * AdaptiveUtils.internalMeasure(roundStrategyPerformance, Metric.COST_CHANGE_PAYOFF, localGraph) ) + ( 0.33 * AdaptiveUtils.internalMeasure(roundStrategyPerformance, Metric.TOTAL_EDGE_COST, localGraph) ), "UniqueRandomSetRepeatAdaptable");	
	
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.AdaptiveGraphTraverser#getAdaptiveWeightings()
	 */
	@Override
	public AdaptiveWeightings getAdaptiveWeightings() {
		
		return new AdaptiveWeightings(0.0, 0.0, 1.0);
	
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.singleshot.random.RandomSet#endOfRound()
	 */
	public void endOfRound() {
		
		uniqueHideLocations().addAll(hideLocations());
		
		Utils.talk(toString(), "uniqueHideLocations.size() " + uniqueHideLocations().size());

		super.endOfRound();
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.AdaptiveGraphTraverser#stopStrategy()
	 */
	@Override
	public void stopStrategy() { }

}