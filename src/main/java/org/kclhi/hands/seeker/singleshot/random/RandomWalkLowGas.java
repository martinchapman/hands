package org.kclhi.hands.seeker.singleshot.random;

import org.kclhi.hands.Gas.LowGasGraphTraverser;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
* 
* 
* @author Martin
*
*/
public class RandomWalkLowGas extends RandomWalk implements LowGasGraphTraverser {

  public RandomWalkLowGas(GraphController<StringVertex, StringEdge> graphController) {
    super(graphController);
  }
    
}
