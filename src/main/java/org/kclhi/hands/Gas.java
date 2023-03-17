package org.kclhi.hands;

import org.kclhi.hands.utility.Utils;

import org.json.JSONObject;

public class Gas {

  public static final double DEFAULT_HIGH_GAS_PROPORTION = 1.0;
  public static final double DEFAULT_MEDIUM_GAS_PROPORTION = 2.0;
  public static final double DEFAULT_LOW_GAS_PROPORTION = 3.0;

  public static final double DEFAULT_USAGE_UPPER = 0.6;

  public static double getHighGasProportion() {
    JSONObject plugin = Utils.getPlugin();
    if(plugin!=null) return plugin.getJSONObject("seekers").getJSONObject("variablesByType").getJSONObject("properties").getJSONObject("HighGas").getDouble("gasProportion");
    else return Gas.DEFAULT_HIGH_GAS_PROPORTION;
  }

  public static double getMediumGasProportion() {
    JSONObject plugin = Utils.getPlugin();
    if(plugin!=null) return plugin.getJSONObject("seekers").getJSONObject("variablesByType").getJSONObject("properties").getJSONObject("MediumGas").getDouble("gasProportion");
    else return Gas.DEFAULT_MEDIUM_GAS_PROPORTION;
  }

  public static double getLowGasProportion() {
    JSONObject plugin = Utils.getPlugin();
    if(plugin!=null) return plugin.getJSONObject("seekers").getJSONObject("variablesByType").getJSONObject("properties").getJSONObject("LowGas").getDouble("gasProportion");
    else return Gas.DEFAULT_LOW_GAS_PROPORTION;
  }
  public interface GasGraphTraverser {

    public boolean useGas();

  }

  public interface HighGasGraphTraverser extends GasGraphTraverser {}
  public interface MediumGasGraphTraverser extends GasGraphTraverser {}
  public interface LowGasGraphTraverser extends GasGraphTraverser {}

}
