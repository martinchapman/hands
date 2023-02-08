package org.kclhi.hands;

public class Gas {

  public static final double HIGH_GAS_PROPORTION = 1.0;
  public static final double MEDIUM_GAS_PROPORTION = 2.0;
  public static final double LOW_GAS_PROPORTION = 3.0;
  
  public interface GasGraphTraverser {

    public boolean useGas();

  }

  public interface HighGasGraphTraverser extends GasGraphTraverser {}
  public interface MediumGasGraphTraverser extends GasGraphTraverser {}
  public interface LowGasGraphTraverser extends GasGraphTraverser {}

}
