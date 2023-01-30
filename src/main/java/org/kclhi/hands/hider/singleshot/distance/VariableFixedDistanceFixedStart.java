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
public class VariableFixedDistanceFixedStart extends VariableFixedDistance {
  
  /**
  * @param graph
  */
  public VariableFixedDistanceFixedStart(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations, int minHideDistance) {
    
    this(graphController, "", numberOfHideLocations, minHideDistance);
    
  }
  
  /**
  * @param graph
  */
  public VariableFixedDistanceFixedStart(GraphController <StringVertex, StringEdge> graphController, String name, int numberOfHideLocations, int minHideDistance) {
    
    super(graphController, name, numberOfHideLocations, minHideDistance);
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.hider.Hider#startNode()
  */
  @Override
  public StringVertex startNode() {
    
    return firstNode();
    
  }
  
}
