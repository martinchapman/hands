package org.kclhi.hands.hider.singleshot.preference.adaptable;

import java.util.ArrayList;

import org.kclhi.hands.utility.Metric;
import org.kclhi.hands.utility.adaptive.AdaptiveMeasure;
import org.kclhi.hands.utility.adaptive.AdaptiveUtils;
import org.kclhi.hands.utility.adaptive.AdaptiveWeightings;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.hider.AdaptiveHider;
import org.kclhi.hands.hider.singleshot.preference.LeastConnected;

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
