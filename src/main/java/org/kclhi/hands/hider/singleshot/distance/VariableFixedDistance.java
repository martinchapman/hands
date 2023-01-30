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
public class VariableFixedDistance extends RandomFixedDistance implements Runnable {
  
  /**
  * @param graph
  */
  public VariableFixedDistance(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations, int minHideDistance) {
    
    this(graphController, "", numberOfHideLocations, minHideDistance);
    
  }
  
  /**
  * @param graph
  */
  public VariableFixedDistance(GraphController <StringVertex, StringEdge> graphController, String name, int numberOfHideLocations, int minHideDistance) {
    
    super(graphController, name, numberOfHideLocations);
    
    this.minHideDistance = minHideDistance;
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.hider.Hider#endOfRound()
  */
  @Override
  public void endOfRound() {
    
    int minHideDistance = this.minHideDistance;
    
    super.endOfRound();
    
    // Because super overrides with new random after each round
    this.minHideDistance = minHideDistance;
    
  }
  
}
