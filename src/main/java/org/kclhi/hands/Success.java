package org.kclhi.hands;

import java.util.HashMap;

public class Success {

  public static double BASE_NON_RESOURCE_IMMUNITY = 0.5;
  public static double BASE_RESOURCE_IMMUNITY = 0.95;

  public interface ResourceImmuneTraverser {}
  public interface VariableImmuneTraverser {}
  
  // ~MDC Ideally this would be held within each immune seeker instance, like variable gas usage (as opposed to centrally)
  public static boolean LEVERAGE_IMMUNITY(String traverserType) {

    HashMap<String, Double> usageUppers = new HashMap<String, Double>() {{
      put("sRandomWalkLowGasVariableImmune", 0.6);
      put("sRandomWalkMediumGasVariableImmune", 0.6);
      put("sRandomWalkHighGasVariableImmune", 0.6);
    }};

    if( usageUppers.keySet().contains(traverserType) ) { 

      return Math.random() < usageUppers.get(traverserType);
    
    } else {
    
      System.out.println("WARN: Leverage immunity lookup did not find " + traverserType);
    
      return true;
    
    }
  
  }

}
