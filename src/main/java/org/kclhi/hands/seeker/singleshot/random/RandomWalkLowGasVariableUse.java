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
public class RandomWalkLowGasVariableUse extends RandomWalk implements LowGasGraphTraverser {

  private double USAGE_UPPER = 0.6;

  public RandomWalkLowGasVariableUse(GraphController<StringVertex, StringEdge> graphController) {
    super(graphController);
  }

  @Override
  public boolean useGas() {
    return Math.random() < USAGE_UPPER;
  }
    
}
