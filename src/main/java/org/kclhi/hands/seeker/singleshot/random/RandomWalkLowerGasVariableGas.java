package org.kclhi.hands.seeker.singleshot.random;

import org.json.JSONObject;
import org.kclhi.hands.Gas;
import org.kclhi.hands.Gas.LowerGasGraphTraverser;
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
public class RandomWalkLowerGasVariableGas extends RandomWalk implements LowerGasGraphTraverser {

  public RandomWalkLowerGasVariableGas(GraphController<StringVertex, StringEdge> graphController) {
    super(graphController);
  }

  @Override
  public boolean useGas() {
    JSONObject plugin = Utils.getPlugin();
    double usageUpper = plugin!=null ? plugin.getJSONObject("seekers").getJSONObject("variablesByType").getJSONObject("behaviour").getJSONObject("Gas").getDouble("leverageProbability") : Gas.DEFAULT_USAGE_UPPER;
    return Math.random() < usageUpper;
  }
    
}
