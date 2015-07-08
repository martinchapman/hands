package HideAndSeek.hider.repeatgame.random.adaptable;

import java.util.ArrayList;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.AdaptiveHider;
import HideAndSeek.hider.singleshot.random.RandomSet;
import Utility.Metric;
import Utility.Utils;
import Utility.adaptive.AdaptiveUtils;
import Utility.adaptive.AdaptiveWeightings;

public class RandomSetAdaptable extends RandomSet implements AdaptiveHider {

	public RandomSetAdaptable(
			GraphController<StringVertex, StringEdge> graphController,
			int numberOfHideLocations) {
		super(graphController, numberOfHideLocations);
		// Auto-generated constructor
	}

	public RandomSetAdaptable(
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

	public void endOfRound() {
		
		uniqueHideLocations().addAll(hideLocations());
		
		Utils.talk(toString(), "uniqueHideLocations.size() " + uniqueHideLocations().size());

		super.endOfRound();
		
	}
	
	@Override
	public void stopStrategy() { }

}