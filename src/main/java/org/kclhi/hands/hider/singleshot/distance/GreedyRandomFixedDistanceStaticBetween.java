package org.kclhi.hands.hider.singleshot.distance;

import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
* @author Martin
*
*/
public class GreedyRandomFixedDistanceStaticBetween extends GreedyRandomFixedDistance {
  
  /**
  * @param graph
  */
  public GreedyRandomFixedDistanceStaticBetween(GraphController <StringVertex, StringEdge> graphController, int hideLocations) {
    
    super(graphController, hideLocations);
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.GraphTraverser#startNode()
  */
  @Override
  public StringVertex startNode() {
    
    return currentNode();
    
  }
  
}
