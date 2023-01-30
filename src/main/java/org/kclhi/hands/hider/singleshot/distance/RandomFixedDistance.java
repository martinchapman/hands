package org.kclhi.hands.hider.singleshot.distance;

import org.kclhi.hands.utility.Utils;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.hider.HidingAgent;

/**
* 
* Employ a random (0 to N) fixed distance between each hide location
* 
* @author Martin
*
*/
public class RandomFixedDistance extends HidingAgent {
  
  
  /**
  * @param graph
  */
  public RandomFixedDistance(GraphController <StringVertex, StringEdge> graphController, String name, int numberOfHideLocations) {
    
    super(graphController, name, numberOfHideLocations);
    
    setRandomMinHideDistance();
    
  }
  
  /**
  * @param graph
  */
  public RandomFixedDistance(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations) {
    
    this(graphController, "", numberOfHideLocations);
    
  }
  
  /**
  * 
  */
  private void setRandomMinHideDistance() {
    
    minHideDistance = ((int)(Math.random() * graphController.vertexSet().size()));
    
  }
  
  /**
  * 
  */
  protected int minHideDistance;
  
  /* (non-Javadoc)
  * @see HideAndSeek.hider.Hider#hideHere(HideAndSeek.graph.StringVertex)
  */
  public boolean hideHere(StringVertex vertex) {
    
    Utils.talk(toString(), "exploredNodes.size() " + exploredNodesSize() + " minHideDistance: " + minHideDistance );
    
    if (minHideDistance == 0) { return true; }
    
    if ( exploredNodesSize() > 0 && ( exploredNodesSize() % minHideDistance == 0 ) ) { 
      
      return true;
      
    }
    
    return false;
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.hider.Hider#nextNode(HideAndSeek.graph.StringVertex)
  */
  @Override
  public StringVertex nextNode(StringVertex currentNode) {
    
    return connectedNode(currentNode);
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.hider.Hider#startNode()
  */
  @Override
  public StringVertex startNode() {
    
    return randomNode();
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.hider.Hider#endOfRound()
  */
  @Override
  public void endOfRound() {
    
    super.endOfRound();
    
    setRandomMinHideDistance();
    
  }
  
}
