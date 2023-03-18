package org.kclhi.hands.seeker.singleshot.preference;

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
public class MaxDistanceUpperGas extends MaxDistance implements UpperGasGraphTraverser {

  public MaxDistanceUpperGas(GraphController<StringVertex, StringEdge> graphController) {
    super(graphController, 1.0);
  }

  @Override
  public boolean useGas() {
    return true;
  }
   
}
