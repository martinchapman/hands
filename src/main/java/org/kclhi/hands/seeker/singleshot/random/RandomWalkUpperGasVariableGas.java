package org.kclhi.hands.seeker.singleshot.random;

import org.json.JSONObject;
import org.kclhi.hands.Gas;
import org.kclhi.hands.Gas.UpperGasGraphTraverser;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.utility.Utils;

/**
* 
* 
* @author Martin
*
*/
public class RandomWalkUpperGasVariableGas extends RandomWalk implements UpperGasGraphTraverser {

  public RandomWalkUpperGasVariableGas(GraphController<StringVertex, StringEdge> graphController) {
    super(graphController);
  }

  @Override
  public boolean useGas() {
    JSONObject plugin = Utils.getPlugin();
    double usageUpper = plugin!=null ? plugin.getJSONObject("seekers").getJSONObject("variablesByType").getJSONObject("behaviour").getJSONObject("Gas").getDouble("leverageProbability") : Gas.DEFAULT_USAGE_UPPER;
    return Math.random() < usageUpper;
  }
    
}
