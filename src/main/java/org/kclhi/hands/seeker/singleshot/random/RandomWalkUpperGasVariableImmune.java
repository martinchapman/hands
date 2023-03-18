package org.kclhi.hands.seeker.singleshot.random;

import org.kclhi.hands.Gas.UpperGasGraphTraverser;
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
public class RandomWalkUpperGasVariableImmune extends RandomWalk implements UpperGasGraphTraverser, VariableImmuneTraverser {

  public RandomWalkUpperGasVariableImmune(GraphController<StringVertex, StringEdge> graphController) {
    super(graphController);
  }

  @Override
  public boolean useGas() {
    return true;
  }
   
}
