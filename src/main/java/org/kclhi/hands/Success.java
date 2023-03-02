package org.kclhi.hands;

import java.util.HashMap;

public class Success {

  public static final double RESOURCE_IMMUNE_PROPORTION = 0.8;

  public interface ResourceImmuneTraverser {}
  
  // ~MDC Ideally this would be held within each immune seeker instance, like variable gas usage (as opposed to centrally)
  public static boolean LEVERAGE_IMMUNITY(String traverserType) {

    HashMap<String, Double> usageUppers = new HashMap<String, Double>() {{
      put("sRandomWalkLowGasResourceImmuneVariableImmune", 0.6);
    }};

    if(usageUppers.keySet().contains(traverserType)) return Math.random() < usageUppers.get(traverserType);
    return true;    

  }

}
