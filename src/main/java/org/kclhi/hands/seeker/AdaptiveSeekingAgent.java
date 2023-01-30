package org.kclhi.hands.seeker;

import java.util.ArrayList;
import java.util.HashSet;

import org.kclhi.hands.utility.Pair;
import org.kclhi.hands.AdaptiveGraphTraverser;
import org.kclhi.hands.AdaptiveGraphTraversingAgent;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
* @author Martin
*/
public class AdaptiveSeekingAgent<E extends Seeker & AdaptiveGraphTraverser> extends AdaptiveGraphTraversingAgent<E> implements Seeker {
  
  public AdaptiveSeekingAgent(
  GraphController<StringVertex, StringEdge> graphController,
  ArrayList<Pair<E, Double>> strategyPortfolio, int totalRounds,
  double cueThreshold, boolean canReuse) {
    super(graphController, strategyPortfolio, totalRounds, cueThreshold, canReuse);
    // TODO Auto-generated constructor stub
  }
  
  public AdaptiveSeekingAgent(
  GraphController<StringVertex, StringEdge> graphController,
  ArrayList<Pair<E, Double>> strategyPortfolio, int totalRounds,
  String initialStrategy, double cueThreshold, boolean canReuse) {
    super(graphController, strategyPortfolio, totalRounds, initialStrategy,
    cueThreshold, canReuse);
    // TODO Auto-generated constructor stub
  }
  
  public AdaptiveSeekingAgent(
  GraphController<StringVertex, StringEdge> graphController,
  ArrayList<Pair<E, Double>> strategyPortfolio, int totalRounds,
  String currentStrategy) {
    super(graphController, strategyPortfolio, totalRounds, currentStrategy);
    // TODO Auto-generated constructor stub
  }
  
  public AdaptiveSeekingAgent(
  GraphController<StringVertex, StringEdge> graphController,
  ArrayList<Pair<E, Double>> strategyPortfolio, int totalRounds) {
    super(graphController, strategyPortfolio, totalRounds);
    // TODO Auto-generated constructor stub
  }
  
  public AdaptiveSeekingAgent(
  GraphController<StringVertex, StringEdge> graphController,
  String name, ArrayList<Pair<E, Double>> strategyPortfolio,
  int totalRounds, double cueTriggerThreshold, boolean canReuse) {
    super(graphController, name, strategyPortfolio, totalRounds,
    cueTriggerThreshold, canReuse);
    // TODO Auto-generated constructor stub
  }
  
  public AdaptiveSeekingAgent(
  GraphController<StringVertex, StringEdge> graphController,
  String name, ArrayList<Pair<E, Double>> strategyPortfolio,
  int totalRounds, String initialStrategy,
  double cueTriggerThreshold, boolean canReuse) {
    super(graphController, name, strategyPortfolio, totalRounds, initialStrategy,
    cueTriggerThreshold, canReuse);
    // TODO Auto-generated constructor stub
  }
  
  public AdaptiveSeekingAgent(
  GraphController<StringVertex, StringEdge> graphController,
  String name, ArrayList<Pair<E, Double>> strategyPortfolio,
  int totalRounds, String initialStrategy) {
    super(graphController, name, strategyPortfolio, totalRounds, initialStrategy);
    // TODO Auto-generated constructor stub
  }
  
  public AdaptiveSeekingAgent(
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
  * 
  */
  @Override
  public void search() {
    
    currentStrategy.search();
    
  }
  
  /* (non-Javadoc)
  * @see org.kclhi.hands.seeker.Seeker#searchCriteria()
  */
  public boolean searchCriteria() {
    
    return currentStrategy.searchCriteria();
    
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
    
    return "s" + getName();
    
  }
  
  /* (non-Javadoc)
  * @see org.kclhi.hands.seeker.Seeker#allHideLocations()
  */
  @Override
  public ArrayList<StringVertex> allHideLocations() {
    
    return currentStrategy.allHideLocations();
    
  }
  
  /* (non-Javadoc)
  * @see org.kclhi.hands.seeker.Seeker#uniqueHideLocations()
  */
  @Override
  public HashSet<StringVertex> uniqueHideLocations() {
    
    return currentStrategy.uniqueHideLocations();
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.AdaptiveGraphTraversingAgent#changeToOtherStrategy(HideAndSeek.GraphTraverser)
  */
  public void changeToOtherStrategy(E strategy) {
    
    super.changeToOtherStrategy(strategy);
    
    currentStrategy.mergeOtherTraverser(previousStrategy);
    
  }
  
}
