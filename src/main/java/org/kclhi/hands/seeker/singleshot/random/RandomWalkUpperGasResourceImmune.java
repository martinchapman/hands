package org.kclhi.hands.seeker.singleshot.random;

import org.kclhi.hands.Gas.UpperGasGraphTraverser;
import org.kclhi.hands.Success.ResourceImmuneTraverser;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
* 
* 
* @author Martin
*
*/
public class RandomWalkUpperGasResourceImmune extends RandomWalk implements UpperGasGraphTraverser, ResourceImmuneTraverser {

  public RandomWalkUpperGasResourceImmune(GraphController<StringVertex, StringEdge> graphController) {
    super(graphController);
  }

  @Override
  public boolean useGas() {
    return true;
  }
    
}
