package org.kclhi.hands.seeker.singleshot.adaptable;

import java.util.ArrayList;

import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.seeker.AdaptiveSeeker;
import org.kclhi.hands.seeker.singleshot.random.RandomWalkStationaryChance;
import org.kclhi.hands.utility.adaptive.AdaptiveMeasure;
import org.kclhi.hands.utility.adaptive.AdaptiveWeightings;

public class RandomWalkStationaryChanceAdaptable extends RandomWalkStationaryChance implements AdaptiveSeeker {

    public RandomWalkStationaryChanceAdaptable(GraphController<StringVertex, StringEdge> graphController) {
        super(graphController);
    }

    @Override
    public AdaptiveMeasure environmentalMeasure() {
        return new AdaptiveMeasure(0.0);
    }

    @Override
    public AdaptiveMeasure socialMeasure() {
        return new AdaptiveMeasure(0.0);
    }

    @Override
    public AdaptiveMeasure internalMeasure(ArrayList<Double> roundStrategyPerformance) {
        return new AdaptiveMeasure(0.0);
    }

    @Override
    public AdaptiveWeightings getAdaptiveWeightings() {
        return new AdaptiveWeightings(.0, 0.0, 0.0);
    }

    @Override
    public void stopStrategy() {}
    
}
