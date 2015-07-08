package HideAndSeek.hider.repeatgame.random.adaptable;

import java.util.ArrayList;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.AdaptiveHider;
import HideAndSeek.hider.repeatgame.random.UniqueRandomSetRepeat;
import Utility.Metric;
import Utility.adaptive.AdaptiveUtils;
import Utility.adaptive.AdaptiveWeightings;

public class UniqueRandomSetRepeatAdaptable extends UniqueRandomSetRepeat implements AdaptiveHider {

	public UniqueRandomSetRepeatAdaptable(
			GraphController<StringVertex, StringEdge> graphController,
			int numberOfHideLocations) {
		super(graphController, numberOfHideLocations);
		// Auto-generated constructor
	}

	public UniqueRandomSetRepeatAdaptable(
			GraphController<StringVertex, StringEdge> graphController,
			String name, int numberOfHideLocations) {
		super(graphController, name, numberOfHideLocations);
		// Auto-generated constructor
	}

	@Override
	public double environmentalMeasure() {
		return 0;
	}

	@Override
	public double socialMeasure() {
		return 0;
	}

	@Override
	public double internalMeasure(ArrayList<Double> roundStrategyPerformance) {
		
		return AdaptiveUtils.internalMeasure(roundStrategyPerformance, Metric.TOTAL_EDGE_COST, localGraph);
		
	}

	@Override
	public AdaptiveWeightings getAdaptiveWeightings() {
		return new AdaptiveWeightings(0.0, 0.0, 1.0);
	}

	@Override
	public void stopStrategy() { }

}