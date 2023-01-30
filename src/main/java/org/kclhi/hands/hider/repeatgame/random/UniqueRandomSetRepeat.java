package org.kclhi.hands.hider.repeatgame.random;

import java.util.ArrayList;
import java.util.TreeSet;

import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
* Same as grandparent, but never produces sets with the
* same random nodes AND does this on loop.
* 
* Attempts to capitalise on the idea that learning is effectively reset
* after the maximum number of unique hide locations has been played.
* 
* @author Martin
*
*/
public class UniqueRandomSetRepeat extends UniqueRandomSet {
  
  /**
  * @param graphController
  * @param numberOfHideLocations
  */
  public UniqueRandomSetRepeat(GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations) {
    
    this(graphController, "", numberOfHideLocations);
    
  }
  
  
  /**
  * @param graphController
  * @param name
  * @param numberOfHideLocations
  */
  public UniqueRandomSetRepeat(GraphController<StringVertex, StringEdge> graphController, String name, int numberOfHideLocations) {
    
    super(graphController, name, numberOfHideLocations);
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.hider.repeatgame.random.UniqueRandomSet#createRandomSet(int, java.util.TreeSet)
  */
  protected ArrayList<StringVertex> createRandomSet(int size, TreeSet<StringVertex> ignoreSet) {
    
    if ( uniqueHideLocations().size() > ( graphController.vertexSet().size() - numberOfHideLocations() ) ) uniqueHideLocations().clear();
    
    ArrayList<StringVertex> hideSet = super.createRandomSet(size, ignoreSet);
    
    return hideSet;
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.hider.singleshot.random.RandomSet#endOfRound()
  */
  @Override
  public void endOfRound() {
    
    super.endOfRound();
    
  }
  
}
