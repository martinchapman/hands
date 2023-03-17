package org.kclhi.hands.seeker.singleshot.adaptable;

import org.kclhi.hands.Gas.HighGasGraphTraverser;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
* 
* 
* @author Martin
*
*/
public class RandomWalkAdaptableHighGas extends RandomWalkAdaptable implements HighGasGraphTraverser {

  public RandomWalkAdaptableHighGas(GraphController<StringVertex, StringEdge> graphController) {
    super(graphController);
  }

  @Override
  public boolean useGas() {
    return true;
  }
   
}
