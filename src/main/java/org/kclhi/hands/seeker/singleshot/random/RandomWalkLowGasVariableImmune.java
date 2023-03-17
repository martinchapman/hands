package org.kclhi.hands.seeker.singleshot.random;

import org.kclhi.hands.Gas.LowGasGraphTraverser;
import org.kclhi.hands.Success.VariableImmuneTraverser;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
* 
* 
* @author Martin
*
*/
public class RandomWalkLowGasVariableImmune extends RandomWalk implements LowGasGraphTraverser, VariableImmuneTraverser {

  public RandomWalkLowGasVariableImmune(GraphController<StringVertex, StringEdge> graphController) {
    super(graphController);
  }

  @Override
  public boolean useGas() {
    return true;
  }
    
}
