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
public class RandomWalkStationaryChanceAdaptableLowerGas extends RandomWalkStationaryChanceAdaptable implements LowerGasGraphTraverser {

  public RandomWalkStationaryChanceAdaptableLowerGas(GraphController<StringVertex, StringEdge> graphController) {
    super(graphController);
  }

  @Override
  public boolean useGas() {
    return true;
  }
    
}
