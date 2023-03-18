package org.kclhi.hands.seeker.singleshot.random;

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
public class RandomWalkStationaryChanceMediumGas extends RandomWalkStationaryChance implements MediumGasGraphTraverser {

  public RandomWalkStationaryChanceMediumGas(GraphController<StringVertex, StringEdge> graphController) {
    super(graphController);
  }

  @Override
  public boolean useGas() {
    return true;
  }
    
}
