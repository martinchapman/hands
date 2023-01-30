package org.kclhi.hands;

import org.kclhi.hands.graph.StringVertex;

/**
* Implement to have a player (hider or seeker) who 
* can vary the traversal strategy they use.
* 
* @author Martin
*
*/
public interface VariableTraversalStrategy {
  
  public OpenTraverserStrategy getExplorationMechanism(GraphTraverser responsibleAgent);
  
  public void atStart(StringVertex startNode);
  
  public void atNode();
  
  public void atNextNode(StringVertex nextNode);
  
  public void endOfRound();
  
  public void endOfGame();
  
}
