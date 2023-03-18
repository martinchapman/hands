package org.kclhi.hands.seeker.singleshot.adaptable;

import org.kclhi.hands.Gas.LowerGasGraphTraverser;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
* 
* 
* @author Martin
*
*/
public class RandomWalkAdaptableLowerGas extends RandomWalkAdaptable implements LowerGasGraphTraverser {

  public RandomWalkAdaptableLowerGas(GraphController<StringVertex, StringEdge> graphController) {
    super(graphController);
  }

  @Override
  public boolean useGas() {
    return true;
  }
    
}
