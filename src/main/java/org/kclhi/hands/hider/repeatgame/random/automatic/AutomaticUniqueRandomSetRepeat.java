package org.kclhi.hands.hider.repeatgame.random.automatic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

import org.kclhi.hands.utility.Utils;
import org.kclhi.hands.utility.adaptive.AdaptiveUtils;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.hider.repeatgame.random.UniqueRandomSetRepeat;

/**
* Alters the size of the unique hide set if it believes this style
* of play is being anticipated too easily by an opponent.
* 
* @author Martin
*
*/
public class AutomaticUniqueRandomSetRepeat extends UniqueRandomSetRepeat {
  
  /**
  * 
  */
  private ArrayList<Double> costChangeValues;
  
  /**
  * 
  */
  private int setSize;
  
  /**
  * Number of rounds for which a Seeker has exhibited
  * good performance
  */
  private int goodPerformanceRounds;
  
  /**
  * Number of rounds for which a Seeker should exhibit
  * good performance before this strategy changes the number
  * of unique nodes used
  */
  private int goodPerformanceRoundsThreshold;
  
  /**
  * @param graphController
  * @param numberOfHideLocations
  * @param goodPerformanceRounds
  */
  public AutomaticUniqueRandomSetRepeat(GraphController<StringVertex, StringEdge> graphController, String name, int numberOfHideLocations, int goodPerformanceRoundsThreshold) {
    
    super(graphController, numberOfHideLocations);
    
    this.costChangeValues = new ArrayList<Double>();
    
    this.setSize = numberOfHideLocations;
    
    this.goodPerformanceRoundsThreshold = goodPerformanceRoundsThreshold;
    
  }
  
  /**
  * @param graphController
  * @param numberOfHideLocations
  * @param goodPerformanceRoundsThreshold
  */
  public AutomaticUniqueRandomSetRepeat(GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations, int goodPerformanceRoundsThreshold) {
    
    this(graphController, "", numberOfHideLocations, goodPerformanceRoundsThreshold);
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.hider.repeatgame.random.UniqueRandomSetRepeat#createRandomSet(int, java.util.TreeSet)
  */
  protected ArrayList<StringVertex> createRandomSet(int size, TreeSet<StringVertex> ignoreSet) {
    
    Utils.talk(toString(), "setSize: " + setSize);
    
    ArrayList<StringVertex> nonUniqueNodes = nonUniqueNodes(numberOfHideLocations - setSize);
    
    ArrayList<StringVertex> uniqueNodes = super.createRandomSet(setSize, new TreeSet<StringVertex>(uniqueHideLocations()));
    
    uniqueNodes.addAll(nonUniqueNodes);
    
    return uniqueNodes;
    
  }
  
  /**
  * @return
  */
  private ArrayList<StringVertex> nonUniqueNodes(int size) {
    
    if ( uniqueHideLocations().size() >= size ) {
      
      ArrayList<StringVertex> uniqueHideLocations = new ArrayList<StringVertex>(uniqueHideLocations());
      
      Collections.shuffle(uniqueHideLocations);
      
      System.out.println("!!" + new ArrayList<StringVertex>(uniqueHideLocations.subList(0, size)));
      
      return new ArrayList<StringVertex>(uniqueHideLocations.subList(0, size));
      
    } else {
      
      return super.createRandomSet(numberOfHideLocations - setSize, new TreeSet<StringVertex>());
      
    }
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.hider.repeatgame.random.UniqueRandomSetRepeat#endOfRound()
  */
  public void endOfRound() {
    
    super.endOfRound();
    
    if ( roundsPassed % 3 == 0 ) {
      
      setSize = numberOfHideLocations - 1;
      
    } else {
      
      setSize = numberOfHideLocations;
      
    }
    
  }
  
  /**
  * 
  */
  public void endOfRound_org() {
    
    super.endOfRound();
    
    // ~MDC 2/7 Needs changing to account for deprecated information source
    //costChangeValues.add(graphController.averageSeekersPerformance(Metric.COST_CHANGE));
    
    if ( AdaptiveUtils.containsHighPerformance(costChangeValues, 0.5, goodPerformanceRounds) ) {
      
      goodPerformanceRounds++;
      
    } else if ( AdaptiveUtils.containsLowPerformance(costChangeValues, 0.5, goodPerformanceRounds) ) {
      
      goodPerformanceRounds = goodPerformanceRounds - 1 >= 0 ? goodPerformanceRounds - 1 : 0;
      
    }
    
    /* 
    * If the opponent is playing too well, alter the number
    * of unique hide locations to counter any strategies that may be 
    * tuned to deal with unique play. If they then do not play well, 
    * the number of unique hide locations can likely be increased again.
    */
    if ( goodPerformanceRounds > goodPerformanceRoundsThreshold ) {  
      
      setSize = setSize > 0 ? setSize - 1 : 0;
      
      /* 
      * Reset the number of good performance rounds, once the number 
      * that corresponds to the threshold have been found
      */
      goodPerformanceRounds = 0;
      
      Utils.talk(toString(), "Changing unique hide set size to: " + setSize);
      
    } else if ( goodPerformanceRounds < goodPerformanceRoundsThreshold ) {
      
      setSize = setSize < numberOfHideLocations ? setSize + 1 : numberOfHideLocations;
      
      Utils.talk(toString(), "Changing unique hide set size to: " + setSize);
      
    }
    
    ArrayList<StringVertex> uniqueNodes = createRandomSet(setSize, new TreeSet<StringVertex>(uniqueHideLocations()));
    
    ArrayList<StringVertex> randomNodes = createRandomSet(numberOfHideLocations - setSize, new TreeSet<StringVertex>());
    
    uniqueNodes.addAll(randomNodes);
    
    populateHideSet(uniqueNodes);
    
  }
  
}
