package org.kclhi.hands.hider.singleshot.distance;

import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
* 
* 
* @author Martin
*
*/
public class VariableFixedDistanceStaticBetween extends VariableFixedDistance implements Runnable {
  
  /**
  * @param graph
  */
  public VariableFixedDistanceStaticBetween(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations, int minHideDistance) {
    
    this(graphController, "", numberOfHideLocations, minHideDistance);
    
  }
  
  /**
  * @param graph
  */
  public VariableFixedDistanceStaticBetween(GraphController <StringVertex, StringEdge> graphController, String name, int numberOfHideLocations, int minHideDistance) {
    
    super(graphController, name, numberOfHideLocations, minHideDistance);
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.hider.singleshot.distance.RandomFixedDistance#startNode()
  */
  public StringVertex startNode() {
    
    return currentNode();
    
  }
  
}
