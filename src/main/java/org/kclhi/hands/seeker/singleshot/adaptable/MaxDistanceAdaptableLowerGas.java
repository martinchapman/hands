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
public class MaxDistanceAdaptableLowerGas extends MaxDistanceAdaptable implements LowerGasGraphTraverser {

  public MaxDistanceAdaptableLowerGas(GraphController<StringVertex, StringEdge> graphController) {
    super(graphController, 1.0);
  }

  @Override
  public boolean useGas() {
    return true;
  }
    
}
