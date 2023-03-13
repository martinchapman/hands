package org.kclhi.hands.seeker.singleshot.random;

import org.kclhi.hands.Gas.MediumGasGraphTraverser;
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
public class RandomWalkMediumGasResourceImmune extends RandomWalk implements MediumGasGraphTraverser, ResourceImmuneTraverser {

  public RandomWalkMediumGasResourceImmune(GraphController<StringVertex, StringEdge> graphController) {
    super(graphController);
  }

  @Override
  public boolean useGas() {
    return true;
  }
    
}
