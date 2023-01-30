package org.kclhi.hands.hider;

import java.util.ArrayList;

import org.kclhi.hands.utility.Pair;
import org.kclhi.hands.AdaptiveGraphTraverser;
import org.kclhi.hands.AdaptiveGraphTraversingAgent;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
* @author Martin
*
*/
public class AdaptiveHidingAgent<E extends Hider & AdaptiveGraphTraverser> extends AdaptiveGraphTraversingAgent<E> implements Hider {
  
  public AdaptiveHidingAgent(
  GraphController<StringVertex, StringEdge> graphController,
  ArrayList<Pair<E, Double>> strategyPortfolio, int totalRounds,
  double cueThreshold, boolean canReuse) {
    super(graphController, strategyPortfolio, totalRounds, cueThreshold, canReuse);
    // TODO Auto-generated constructor stub
  }
  
  public AdaptiveHidingAgent(
  GraphController<StringVertex, StringEdge> graphController,
  ArrayList<Pair<E, Double>> strategyPortfolio, int totalRounds,
  String initialStrategy, double cueThreshold, boolean canReuse) {
    super(graphController, strategyPortfolio, totalRounds, initialStrategy,
    cueThreshold, canReuse);
    // TODO Auto-generated constructor stub
  }
  
  public AdaptiveHidingAgent(
  GraphController<StringVertex, StringEdge> graphController,
  ArrayList<Pair<E, Double>> strategyPortfolio, int totalRounds,
  String currentStrategy) {
    super(graphController, strategyPortfolio, totalRounds, currentStrategy);
    // TODO Auto-generated constructor stub
  }
  
  public AdaptiveHidingAgent(
  GraphController<StringVertex, StringEdge> graphController,
  ArrayList<Pair<E, Double>> strategyPortfolio, int totalRounds) {
    super(graphController, strategyPortfolio, totalRounds);
    // TODO Auto-generated constructor stub
  }
  
  public AdaptiveHidingAgent(
  GraphController<StringVertex, StringEdge> graphController,
  String name, ArrayList<Pair<E, Double>> strategyPortfolio,
  int totalRounds, double cueTriggerThreshold, boolean canReuse) {
    super(graphController, name, strategyPortfolio, totalRounds,
    cueTriggerThreshold, canReuse);
    // TODO Auto-generated constructor stub
  }
  
  public AdaptiveHidingAgent(
  GraphController<StringVertex, StringEdge> graphController,
  String name, ArrayList<Pair<E, Double>> strategyPortfolio,
  int totalRounds, String initialStrategy,
  double cueTriggerThreshold, boolean canReuse) {
    super(graphController, name, strategyPortfolio, totalRounds, initialStrategy,
    cueTriggerThreshold, canReuse);
    // TODO Auto-generated constructor stub
  }
  
  public AdaptiveHidingAgent(
  GraphController<StringVertex, StringEdge> graphController,
  String name, ArrayList<Pair<E, Double>> strategyPortfolio,
  int totalRounds, String initialStrategy) {
    super(graphController, name, strategyPortfolio, totalRounds, initialStrategy);
    // TODO Auto-generated constructor stub
  }
  
  public AdaptiveHidingAgent(
  GraphController<StringVertex, StringEdge> graphController,
  String name, ArrayList<Pair<E, Double>> strategyPortfolio,
  int totalRounds) {
    super(graphController, name, strategyPortfolio, totalRounds);
    // TODO Auto-generated constructor stub
  }
  
  /**
  * @param location
  */
  @Override
  public void addHideLocation(StringVertex location) {
    
    currentStrategy.addHideLocation(location);
    
  }
  
  /**
  * @param vertex
  * @return
  */
  @Override
  public boolean hideHere(StringVertex vertex) {
    
    return currentStrategy.hideHere(vertex);
    
  }
  
  /**
  * @param currentNode
  * @return
  */
  @Override
  public StringVertex nextNode(StringVertex currentNode) {
    
    return currentStrategy.nextNode(currentNode);
    
  }
  
  /**
  * @return
  */
  @Override
  public StringVertex startNode() {
    
    return currentStrategy.startNode();
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.GraphTraversingAgent#toString()
  */
  public String toString() {
    
    return "h" + getName();
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.hider.Hider#numberOfHideLocations()
  */
  @Override
  public int numberOfHideLocations() {
    
    return currentStrategy.numberOfHideLocations();
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.hider.Hider#mergeOtherTraverser(HideAndSeek.hider.Hider)
  */
  @Override
  public void mergeOtherTraverser(Hider traverser) {
    
    traverser.mergeOtherTraverser(traverser);
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.AdaptiveGraphTraversingAgent#changeToOtherStrategy(HideAndSeek.GraphTraverser)
  */
  public void changeToOtherStrategy(E strategy) {
    
    super.changeToOtherStrategy(strategy);
    
    currentStrategy.mergeOtherTraverser(previousStrategy);
    
  }
  
}
