package org.kclhi.hands;

import java.util.HashMap;

import org.json.JSONObject;
import org.kclhi.hands.utility.Utils;

public class Success {

  // For seekers that aren't immune from poor payoff due to failed games, what is the extent of this lack of immunity (resp. for seekers that are immune)
  public static double BASE_NON_RESOURCE_IMMUNITY = 0.0;
  public static double BASE_RESOURCE_IMMUNITY = 1.0;
  
  public static double DEFAULT_LEVERAGE_PROBABILITY = 0.6;

  public interface ResourceImmuneTraverser {}
  public interface VariableImmuneTraverser {}
  
  // ~MDC Ideally this would be held within each immune seeker instance, like variable gas usage (as opposed to centrally)
  public static boolean LEVERAGE_IMMUNITY(String traverserType) {

    JSONObject plugin = Utils.getPlugin();
    double leverageProbability = plugin!=null ? plugin.getJSONObject("seekers").getJSONObject("variablesByType").getJSONObject("behaviour").getJSONObject("VariableImmune").getDouble("leverageProbability") : DEFAULT_LEVERAGE_PROBABILITY;
    HashMap<String, Double> usageUppers = new HashMap<String, Double>() {{
      put("sRandomWalkLowGasVariableImmune", leverageProbability);
      put("sRandomWalkMediumGasVariableImmune", leverageProbability);
      put("sRandomWalkHighGasVariableImmune", leverageProbability);
    }};

    if( usageUppers.keySet().contains(traverserType) ) { 

      return Math.random() < usageUppers.get(traverserType);
    
    } else {
    
      System.out.println("WARN: Leverage immunity lookup did not find " + traverserType);
    
      return true;
    
    }
  
  }

}
