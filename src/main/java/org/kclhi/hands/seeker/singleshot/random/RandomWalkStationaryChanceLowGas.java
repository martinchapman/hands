package org.kclhi.hands.seeker.singleshot.random;

import org.kclhi.hands.Gas.LowGasGraphTraverser;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
* 
* 
* @author Martin
*
*/
public class RandomWalkStationaryChanceLowGas extends RandomWalkStationaryChance implements LowGasGraphTraverser {

  public RandomWalkStationaryChanceLowGas(GraphController<StringVertex, StringEdge> graphController) {
    super(graphController);
  }

  @Override
  public boolean useGas() {
    return true;
  }
    
}
