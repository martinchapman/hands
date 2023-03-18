package org.kclhi.hands;

import org.kclhi.hands.utility.Utils;

import org.json.JSONObject;

public class Gas {

  public static final double DEFAULT_GAS_PROPORTION = 1.0;
  public static final double DEFAULT_USAGE_UPPER = 0.6;

  public static double getGasProportion(GraphTraverser traverser) {

    JSONObject properties = Utils.getPlugin().getJSONObject("seekers").getJSONObject("variablesByType").getJSONObject("properties");
    if( traverser.getName().contains("Lower") ) {
      return properties.getJSONObject("LowerGas").getDouble("gasProportion");
    } else if( traverser.getName().contains("Low") ) {
      return properties.getJSONObject("LowGas").getDouble("gasProportion");
    } else if( traverser.getName().contains("Medium") ) {
      return properties.getJSONObject("MediumGas").getDouble("gasProportion");
    } else if( traverser.getName().contains("High") ) {
      return properties.getJSONObject("HighGas").getDouble("gasProportion");
    } else if( traverser.getName().contains("Upper") ) {
      return properties.getJSONObject("UpperGas").getDouble("gasProportion");
    }
  
    return Gas.DEFAULT_GAS_PROPORTION;
  
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
