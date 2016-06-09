package HideAndSeek.hider.singleshot.preference.adaptable;

import java.util.ArrayList;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.AdaptiveHider;
import HideAndSeek.hider.singleshot.preference.LeastConnected;
import Utility.Metric;
import Utility.adaptive.AdaptiveMeasure;
import Utility.adaptive.AdaptiveUtils;
import Utility.adaptive.AdaptiveWeightings;

/**
 * 
 * Attempts to work out how many minimum connectivity nodes are in a graph, and thus 
 * if it is worth attempting to hide in them.
 * 
 * @author Martin
 *
 */
public class LeastConnectedAdaptable extends LeastConnected implements AdaptiveHider {

	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public LeastConnectedAdaptable( GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations) {
	
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
	public AdaptiveMeasure environmentalMeasure() {
		
		double cumulativeConnectivityLeastConnected = 0.0;
		
		for ( StringVertex vertex : getMinimumConnectivityNodes() ) {
			
			cumulativeConnectivityLeastConnected += graphController.degreeOf(vertex);
			
		}
		
		return new AdaptiveMeasure(( cumulativeConnectivityLeastConnected / getMinimumConnectivityNodes().size() ) / ((double)estimatedMaxConnections));
	
	}

	/**
	 * Emits a social cue.
	 * 
	 * @return
	 */
	@Override
	public AdaptiveMeasure socialMeasure() {
		
		return new AdaptiveMeasure(0.0);
	
	}

	/**
	 * @param roundStrategyPerformance
	 * @return
	 */
	@Override
	public AdaptiveMeasure internalMeasure(ArrayList<Double> roundStrategyPerformance) {
		
		return new AdaptiveMeasure(AdaptiveUtils.internalMeasure(roundStrategyPerformance, Metric.TOTAL_EDGE_COST, localGraph));
	
	}

	/**
	 * @return
	 */
	@Override
	public AdaptiveWeightings getAdaptiveWeightings() {
		
		return new AdaptiveWeightings(1.0, 0.0, 0.0);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.AdaptiveGraphTraverser#stopStrategy()
	 */
	@Override
	public void stopStrategy() {}
	
}
