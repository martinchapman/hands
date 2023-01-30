package org.kclhi.hands.seeker.repeatgame.probability;

import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
* @author Martin
*
*/
public class HighProbabilityStart extends HighProbability {
  
  public HighProbabilityStart(GraphController<StringVertex, StringEdge> graphController) {
    
    super(graphController);
    
  }
  
  /* (non-Javadoc)
  * @see org.kclhi.hands.seeker.repeatgame.probability.HighProbability#startNode()
  */
  @Override
  public StringVertex startNode() {
    
    return likelyNodes.remove(0);
    
  }
  
}
