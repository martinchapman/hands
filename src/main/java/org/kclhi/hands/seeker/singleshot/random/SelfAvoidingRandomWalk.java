package org.kclhi.hands.seeker.singleshot.random;

import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.seeker.SeekingAgent;

/**
* @author Martin
*
*/
public class SelfAvoidingRandomWalk extends SeekingAgent {
  
  /**
  * @param graphController
  */
  public SelfAvoidingRandomWalk(
  GraphController <StringVertex, StringEdge> graphController) {
    
    super(graphController);
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.GraphTraversingAgent#nextNode(HideAndSeek.graph.StringVertex)
  */
  @Override
  public StringVertex nextNode(StringVertex currentNode) {
    
    return connectedNode(currentNode);
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.GraphTraversingAgent#startNode()
  */
  @Override
  public StringVertex startNode() {
    
    return randomNode();
    
  }
  
}
