package org.kclhi.hands.hider.repeatgame.bias.automatic;

import org.kclhi.hands.utility.Utils;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.hider.repeatgame.bias.VariableBias;

/**
* The presence of lower cost edges may cause Hiders to exhibit bias
* in their edge selection. The more bias a Hider is, the more likely
* they are to select lower cost edges.
* 
* Depending on the game configuration, selecting lower cost edges repeatedly
* may result in behaviour that can be predicted. 
* 
* This hider only focuses on the performance of the seeker to adjust its bias:
* only downwards if the performance of the opposition is too good.
* 
* @author Martin
*
*/
public class PerformanceSensitiveHider extends VariableBias {
  
  /**
  * @param graph
  * @param numberOfHideLocations
  */
  public PerformanceSensitiveHider(
  GraphController <StringVertex, StringEdge> graphController,
  int numberOfHideLocations) {
    
    super(graphController, numberOfHideLocations, 1.0);
    
  }
  
  /**
  * 
  */
  private static int SIGNIFICANCELEVEL = 50;
  
  /* (non-Javadoc)
  * @see HideAndSeek.hider.Hider#endOfRound()
  */
  @Override
  public void endOfRound() {
    
    adjustBias();
    
    super.endOfRound();
    
  }
  
  /**
  * 
  */
  protected void adjustBias() {
    
    // // ~MDC 2/7 Needs changing to account for deprecated information source
    double seekerPerformanceChange = 0.0; //( ( graphController.averageSeekersRoundPerformance(roundsPassed) - graphController.averageSeekersRoundPerformance(roundsPassed - 1) ) / graphController.averageSeekersRoundPerformance(roundsPassed - 1) ) * 100;
    
    Utils.talk(toString(), "Seeker performance change: " + seekerPerformanceChange);
    
    if (seekerPerformanceChange > SIGNIFICANCELEVEL) {
      
      decrementBias();
      
    }
    
    Utils.talk(toString(), "Bias: " + tendencyToBias);
    
  }
  
  
  
}
