package org.kclhi.hands.seeker.singleshot.random;

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
public class RandomWalkStationaryChanceLowerGas extends RandomWalkStationaryChance implements LowerGasGraphTraverser {

  public RandomWalkStationaryChanceLowerGas(GraphController<StringVertex, StringEdge> graphController) {
    super(graphController);
  }

  @Override
  public boolean useGas() {
    return true;
  }
    
}
