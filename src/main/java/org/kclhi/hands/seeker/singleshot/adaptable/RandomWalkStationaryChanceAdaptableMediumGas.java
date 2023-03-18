package org.kclhi.hands.seeker.singleshot.adaptable;

import org.kclhi.hands.Gas.MediumGasGraphTraverser;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
* 
* 
* @author Martin
*
*/
public class RandomWalkStationaryChanceAdaptableMediumGas extends RandomWalkStationaryChanceAdaptable implements MediumGasGraphTraverser {

  public RandomWalkStationaryChanceAdaptableMediumGas(GraphController<StringVertex, StringEdge> graphController) {
    super(graphController);
  }

  @Override
  public boolean useGas() {
    return true;
  }
    
}
