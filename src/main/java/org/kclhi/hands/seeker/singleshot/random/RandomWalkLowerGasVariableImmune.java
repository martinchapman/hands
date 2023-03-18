package org.kclhi.hands.seeker.singleshot.random;

import org.kclhi.hands.Gas.LowerGasGraphTraverser;
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
public class RandomWalkLowerGasVariableImmune extends RandomWalk implements LowerGasGraphTraverser, VariableImmuneTraverser {

  public RandomWalkLowerGasVariableImmune(GraphController<StringVertex, StringEdge> graphController) {
    super(graphController);
  }

  @Override
  public boolean useGas() {
    return true;
  }
    
}
