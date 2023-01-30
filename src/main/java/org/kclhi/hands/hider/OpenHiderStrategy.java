package org.kclhi.hands.hider;

import org.kclhi.hands.graph.StringVertex;

/**
* Implement to ensure that a strategy exposes
* its hide mechanism for use by other strategies.
* 
* @author Martin
*
*/
public interface OpenHiderStrategy {
  
  public boolean hideHere(StringVertex vertex);
  
}
