package org.kclhi.hands.seeker.singleshot.random;

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
public class RandomWalkStationaryChanceUpperGas extends RandomWalkStationaryChance implements UpperGasGraphTraverser {

  public RandomWalkStationaryChanceUpperGas(GraphController<StringVertex, StringEdge> graphController) {
    super(graphController);
  }

  @Override
  public boolean useGas() {
    return true;
  }
   
}
