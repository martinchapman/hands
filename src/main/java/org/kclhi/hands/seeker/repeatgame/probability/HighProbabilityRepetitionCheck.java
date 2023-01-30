package org.kclhi.hands.seeker.repeatgame.probability;

import org.kclhi.hands.utility.BehaviourPredictionRepetitionCheck;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
* Extends the standard High Probability Seeker adding the capacity
* to detect potential attempts to exploit the frequency algorithm used.
* 
* Mechanism in @see Utility.BehaviourPredictionRepetitionCheck.
* 
* @author Martin
*
*/
public class HighProbabilityRepetitionCheck extends HighProbability {
  
  /**
  * 
  */
  private int numberOfConsecutiveMatches; 
  
  /**
  * 
  */
  private int nodesToCompare;
  
  /**
  * @param graphController
  * @param numberOfConsecutiveMatches
  * @param nodesToCompare
  */
  public HighProbabilityRepetitionCheck(
  GraphController <StringVertex, StringEdge> graphController, int numberOfConsecutiveMatches, int nodesToCompare) {
    super(graphController);
    
    this.numberOfConsecutiveMatches = numberOfConsecutiveMatches;
    
    this.nodesToCompare = nodesToCompare;
    
    behaviourPrediction = new BehaviourPredictionRepetitionCheck(numberOfConsecutiveMatches, nodesToCompare);
    
  }
  
  /* (non-Javadoc)
  * @see org.kclhi.hands.seeker.repeatgame.HighProbability#endOfGame()
  */
  public void endOfGame() {
    
    super.endOfGame();
    
    behaviourPrediction = new BehaviourPredictionRepetitionCheck(numberOfConsecutiveMatches, nodesToCompare);
    
  }
  
}
