package org.kclhi.hands;

import org.kclhi.hands.utility.Utils;

import org.json.JSONObject;

public class Gas {

  public static final double DEFAULT_GAS_PROPORTION = 1.0;
  public static final double DEFAULT_USAGE_UPPER = 0.6;

  public static double getGasProportion(GraphTraverser traverser) {

    double gasProportion = Gas.DEFAULT_GAS_PROPORTION;

    JSONObject properties = Utils.getPlugin().getJSONObject("seekers").getJSONObject("variablesByType").getJSONObject("properties");
    if( traverser.getName().contains("Lower") ) {
      gasProportion = properties.getJSONObject("LowerGas").getDouble("gasProportion");
    } else if( traverser.getName().contains("Low") ) {
      gasProportion = properties.getJSONObject("LowGas").getDouble("gasProportion");
    } else if( traverser.getName().contains("Medium") ) {
      gasProportion = properties.getJSONObject("MediumGas").getDouble("gasProportion");
    } else if( traverser.getName().contains("High") ) {
      gasProportion = properties.getJSONObject("HighGas").getDouble("gasProportion");
    } else if( traverser.getName().contains("Upper") ) {
      gasProportion = properties.getJSONObject("UpperGas").getDouble("gasProportion");
    }
  
    return gasProportion > -1 ? gasProportion : Math.random();
  
  }
  public interface GasGraphTraverser {

    public boolean useGas();

  }

  public interface HighGasGraphTraverser extends GasGraphTraverser {}
  public interface MediumGasGraphTraverser extends GasGraphTraverser {}
  public interface LowGasGraphTraverser extends GasGraphTraverser {}

  public interface UpperGasGraphTraverser extends GasGraphTraverser {}
  public interface LowerGasGraphTraverser extends GasGraphTraverser {}

}
