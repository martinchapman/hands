package org.kclhi.hands.hider.singleshot.preference;

import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
* Attempts to hide nodes with the least possible connectivity.
* 
* Best is thus leaf nodes.
* 
* @author Martin
*
*/
public class LeastConnectedStaticBetween extends LeastConnected {
  
  /**
  * @param graph
  * @param numberOfHideLocations
  */
  public LeastConnectedStaticBetween(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations) {
    
    super(graphController, numberOfHideLocations);
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.GraphTraverser#startNode()
  */
  @Override
  public StringVertex startNode() {
    
    return currentNode();
    
  }
  
  
}
