package org.kclhi.hands.seeker.singleshot.preference;

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
public class MaxDistanceLowGas extends MaxDistance implements LowGasGraphTraverser {

  public MaxDistanceLowGas(GraphController<StringVertex, StringEdge> graphController) {
    super(graphController, 1.0);
  }

  @Override
  public boolean useGas() {
    return true;
  }
    
}
