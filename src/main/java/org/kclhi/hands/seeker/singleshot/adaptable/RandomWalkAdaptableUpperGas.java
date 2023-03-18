package org.kclhi.hands.seeker.singleshot.adaptable;

import org.kclhi.hands.Gas.UpperGasGraphTraverser;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
* 
* 
* @author Martin
*
*/
public class RandomWalkAdaptableUpperGas extends RandomWalkAdaptable implements UpperGasGraphTraverser {

  public RandomWalkAdaptableUpperGas(GraphController<StringVertex, StringEdge> graphController) {
    super(graphController);
  }

  @Override
  public boolean useGas() {
    return true;
  }
   
}
