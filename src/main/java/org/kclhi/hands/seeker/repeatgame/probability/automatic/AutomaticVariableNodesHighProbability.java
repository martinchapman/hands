package org.kclhi.hands.seeker.repeatgame.probability.automatic;

import org.kclhi.hands.utility.Utils;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.seeker.repeatgame.probability.VariableNodesHighProbability;

/**
* @author Martin
*/
public class AutomaticVariableNodesHighProbability extends VariableNodesHighProbability {
  
  public AutomaticVariableNodesHighProbability(
  GraphController <StringVertex, StringEdge> graphController) {
    
    super(graphController, Integer.MAX_VALUE, true);
    
  }
  
  /**
  * 
  */
  private static double CONFIDENCELEVEL = 0.5;
  
  /* (non-Javadoc)
  * @see org.kclhi.hands.seeker.repeatgame.HighProbabilitySeeker#endOfRound()
  */
  @Override
  public void endOfRound() {
    
    super.endOfRound();
    
    likelyNodes = behaviourPrediction.rankLikelyHideLocations(Integer.MAX_VALUE);
    
    for ( int i = 1; i < likelyNodes.size(); i++ ) {
      
      // Guess the number of nodes hidden with bias according to the disparity
      // between the probability of the 'last' node hidden with bias and the next
      // (NB. Do Syso. behaviourPrediction to see)
      if ( behaviourPrediction.getProbability(likelyNodes.get(i)) < 
      ( behaviourPrediction.getProbability(likelyNodes.get(i-1)) * CONFIDENCELEVEL ) )  {
        
        Utils.talk(toString(), "Assuming " + i + " objects hidden with bias");
        
        predictiveNodes = i;
        
        likelyNodes = behaviourPrediction.rankLikelyHideLocations(i);
        
        return;
        
      }
      
    }
    
    Utils.talk(toString(), "Cannot discern number of nodes hidden with bias");
    
    likelyNodes.clear();
    
  }
  
  /**
  * @return
  */
  @Override
  public String printRoundStats() {
    
    return super.printRoundStats() + " " + getPredictiveNodes();
    
    
  }
  
}
