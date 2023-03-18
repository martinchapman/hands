package org.kclhi.hands.seeker.singleshot.random;

import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.seeker.SeekingAgent;

/**
* 
* Truly random walk that picks an outgoing node at random
* from each vertex is comes across.
*
* With a certain probability, this seeker will remain where
* they are
* 
* @author Martin
*
*/
public class RandomWalkStationaryChance extends SeekingAgent {
  
  public static double STATIONARY_CHANCE = 0.75;
  private StringVertex returnNode;

  /**
  * @param graphController
  */
  public RandomWalkStationaryChance( GraphController <StringVertex, StringEdge> graphController ) {
    
    super(graphController);
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
  */
  @Override
  public StringVertex nextNode(StringVertex currentNode) {
    
    uniquelyVisitNodes = false;
    
    if ( returnNode != null ) {
      StringVertex nextNode = returnNode;
      returnNode = null;
      return nextNode;
    } else {
      if ( Math.random() < STATIONARY_CHANCE ) returnNode = currentNode;
    }

    return connectedNode(currentNode);
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.GraphTraverser#startNode()
  */
  @Override
  public StringVertex startNode() {
    
    return randomNode();
    
  }
  
}
