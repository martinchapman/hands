package org.kclhi.hands.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.jgrapht.EdgeFactory;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.kclhi.hands.utility.Metric;
import org.kclhi.hands.utility.Utils;
import org.kclhi.hands.Gas;
import org.kclhi.hands.GraphTraverser;
import org.kclhi.hands.Gas.GasGraphTraverser;
import org.kclhi.hands.Gas.HighGasGraphTraverser;
import org.kclhi.hands.Gas.MediumGasGraphTraverser;
import org.kclhi.hands.hider.Hider;
import org.kclhi.hands.seeker.Seeker;

/**
* Emphasis is put on the graph as the controller, with whom seekers and hiders
* must register, and whom tracks the performance of each, reporting it at the end.
* 
* @author Martin
*
* @param <V>
* @param <E>
*/
public class HiddenObjectGraph<V, E extends DefaultWeightedEdge> extends SimpleWeightedGraph<V, E> {
  
  /**
  * 
  */
  private boolean RECORD_PATH_DATA = false;
  
  /**
  * 
  */
  private static final long serialVersionUID = 1L;
  
  /**
  * 
  */
  protected HashSet<V> hideLocations;
  
  /**
  * 
  */
  public void clearHideLocations() {
    
    hideLocations.clear();
    
  }
  
  /**
  * 
  */
  private int roundNumber = 0;
  
  /**
  * Maps each round to its participating traversers, and their traversal
  * payoffs.
  */
  private Hashtable<Integer, Hashtable<GraphTraverser, Double>> roundCosts;
  
  /**
  * Maps each round to its participating traversers, and their traversal
  * paths.
  */
  private Hashtable<Integer, Hashtable<GraphTraverser, ArrayList<V>>> roundPaths;
  
  /**
  * 
  */
  public void notifyEndOfRound() {
    
    calculateRealtimeHiderPayoffs();
    
    calculateRealtimeSeekerPayoffs();
    
    // Calculate average seeker round performance for access by adaptive Hiders (~MDC Deprecated)
    recordAverageSeekersRoundPerformance(Metric.COST);
    
    roundNumber++;
    
    roundCosts.put(roundNumber, new Hashtable<GraphTraverser, Double>());
    
    roundPaths.put(roundNumber, new Hashtable<GraphTraverser, ArrayList<V>>());
    
    clearHideLocations();
    
  }
  
  /**
  * @param traverser
  * @return
  */
  public double latestRoundCosts(GraphTraverser traverser, boolean normalised) {
    
    /* 
    * Find most recent non-null set of costs, and if this set contains the given 
    * traverser, return the value associated with the traverser. 
    */
    for ( int i = roundCosts.size() - 1; i >= 0; i-- ) {
      
      if ( roundCosts.get(i).keySet().size() > 0 ) {
        
        if ( roundCosts.get(i).keySet().contains(traverser) ) {
          
          return roundCost(i, normalised, traverser);
          
        } else {
          
          return 0.0;
          
        }
        
      }
      
    }
    
    return 0.0;
    
  }

  /**
   * @param traverser
   * @return
   */
  public double successfulRoundTraversal(GraphTraverser traverser) {

    return latestRoundCosts(traverser, false) < totalEdgeCosts(traverser) ? 1 : 0;

  }
  
  /**
  * @param traverser
  * @return
  */
  public ArrayList<V> latestRoundPaths(GraphTraverser traverser) {
    
    if (roundPaths.get(roundPaths.size() - 1).get(traverser) == null && roundPaths.get(roundPaths.size() - 2).get(traverser) == null) { return new ArrayList<V>(); }
    
    if (roundPaths.get(roundPaths.size() - 1).get(traverser) == null && roundPaths.get(roundPaths.size() - 2).get(traverser) != null) return roundPaths.get(roundPaths.size() - 2).get(traverser);
    
    return roundPaths.get(roundPaths.size() - 1).get(traverser);
    
  }
  
  /**
  * @param traverser
  * @return
  */
  public double latestHiderRoundPayoffs(GraphTraverser traverser) {
    
    return hiderRoundPayoffs.get(hiderRoundPayoffs.size() - 1).get(traverser);
    
  }
  
  public double latestSeekerRoundPayoffs(GraphTraverser traverser) {
    
    return seekerRoundPayoffs.get(seekerRoundPayoffs.size() - 1).get(traverser);
    
  }
  
  /**
  * Individual round payoffs for each hider
  */
  private Hashtable<Integer, Hashtable<GraphTraverser, Double>> hiderRoundPayoffs;
  
  /**
  * Individual round payoffs for each seeker
  */
  private Hashtable<Integer, Hashtable<GraphTraverser, Double>> seekerRoundPayoffs;
  
  /**
  * The effectiveness of a hide strategy is judged from two perspectives: the performance
  * of the seeker when this strategy is employed, and the performance of the hider under
  * this strategy. Commonly, the performance of a hider is inversely proportional to their
  * traversal cost (so the higher the cost, the lower their performance, and vice-versa).
  * 
  * The lower the performance of the seeker the better the strategy is perceived to be.
  * The higher the performance of the hider the better the strategy is perceived to be.
  * 
  * Hider payoff is therefore calculated as the performance of the hider, minus the 
  * performance of the seeker. Thus, if performance is too low or a seeker's performance
  * is too good, payoff is likely to be lower.
  * 
  * These payoffs are normalised as best possible, according to existing records.
  * 
  */
  private void calculateRealtimeHiderPayoffs() {
    
    hiderRoundPayoffs.put(roundNumber, new Hashtable<GraphTraverser, Double>());
    
    for ( GraphTraverser agent : traversers ) {
      
      double payoffAgainstEach = 0.0;
      
      int opponents = 0;
      
      if ( agent instanceof Hider ) {
        
        // We do not play all registered Hiders at once, so not all will be payoff
        if ( !((Hider)agent).isPlaying() ) { continue; }
        
        for ( GraphTraverser seeker : traversers ) {
          
          if ( seeker instanceof Seeker ) {
            
            opponents++;
            
            /* This is equivalent to Seeker Cost - Hider cost due to negation in
            * latestTraverserRoundPerformance.
            */
            payoffAgainstEach += latestTraverserRoundPerformance((Hider)agent, Metric.COST) -
            latestTraverserRoundPerformance((Seeker)seeker, Metric.COST);
            
            Utils.talk(toString(), (Hider)agent + " cost = " + latestTraverserRoundPerformance((Hider)agent, Metric.COST));
            
            Utils.talk(toString(), (Seeker)seeker + " cost = " + latestTraverserRoundPerformance((Seeker)seeker, Metric.COST));
            
          }
          
        }
        
        hiderRoundPayoffs.get(hiderRoundPayoffs.size() - 1).put(agent, (payoffAgainstEach / opponents));
        
      }
      
    }
    
  }
  
  /**
  * Seeker payoff is currently more simplistic, simply their own performance.
  * 
  * @param hider
  */
  private void calculateRealtimeSeekerPayoffs() {
    
    seekerRoundPayoffs.put(roundNumber, new Hashtable<GraphTraverser, Double>());
    
    for ( GraphTraverser traverser : traversers ) {
      
      if ( traverser instanceof Seeker ) {
        
        seekerRoundPayoffs.get(roundNumber).put(traverser, latestTraverserRoundPerformance((Seeker)traverser, Metric.COST));
        
      }
      
    }
    
  }
  
  /**
  * @param traverser
  * @param metric
  * @return
  */
  public double latestTraverserRoundPerformance(GraphTraverser traverser, Metric metric) {
    
    return latestTraverserRoundPerformance(traverser, metric, false);
    
  }
  
  /**
  * 
  * 
  * @param traverser
  * @return
  */
  public double latestTraverserRoundPerformance(GraphTraverser traverser, Metric metric, boolean normalised) {
    
    if ( metric == Metric.COST ) {
      
      return performanceMetricCost(traverser, normalised);
      
    } else if ( metric == Metric.PATH ) {
      
      return performanceMetricPath(traverser);
      
    } else if ( metric == Metric.COST_CHANGE ) {
      
      return performanceMetricCostChange(traverser);
      
    } else if ( metric == Metric.COST_CHANGE_PAYOFF ) {
      
      return performanceMetricCostChangePayoff(traverser); 
      
    } else if ( metric == Metric.RELATIVE_COST ) {
      
      return performanceMetricRelativeCost(traverser); 
      
    } else if ( metric == Metric.PAYOFF ) {
      
      calculateRealtimeHiderPayoffs();
      
      calculateRealtimeSeekerPayoffs();
      
      if ( traverser instanceof Hider ) {
        
        return latestHiderRoundPayoffs(traverser);
        
      } else if ( traverser instanceof Seeker ) {
        
        return latestSeekerRoundPayoffs(traverser);
        
      } else {
        
        throw new UnsupportedOperationException("Unable to classify traverser supplied for performance.");
      }
      
      
    } else {
      
      return 0.0;
      
    }
    
  }
  
  /**
  * Uses traversal cost as a performance metric. Therefore, negates
  * this cost, as the lower the cost the better.
  * 
  * @param traverser
  * @return
  */
  private double performanceMetricCost(GraphTraverser traverser, boolean normalised) {
    
    return latestRoundCosts(traverser, normalised) * -1;
    
  }
  
  /**
  * Uses the number of edges traversed as a portion of the total number
  * of edges as a metric for performance. Attempts to address potential
  * imbalances introduced by purely looking at edge costs. 
  * 
  * As lower number of traversals is better, reverse percentage
  * 
  * @param traverser
  * @return
  */
  private double performanceMetricPath(GraphTraverser traverser) {
    
    return 100 - (latestRoundPaths(traverser).size() / ((double)edgeSet().size()) * 100);
    
  }
  
  /**
  * Uses the most recent change in cost as an indicator of current 
  * performance. 
  * 
  * @param traverser
  * @return
  */
  private double performanceMetricCostChange(GraphTraverser traverser) {
    
    if ( roundNumber > 0 ) {
      
      // Because any decrease in cost (i.e. -10%) is actually a good thing for the player, and any increase is bad, flip the sign.
      return -1 * ( ( ( roundCost(roundNumber, false, traverser) - roundCost(roundNumber - 1, false, traverser) ) / (double)(Math.abs(roundCost(roundNumber - 1, false, traverser))) ) * 100);
      
    } else {
      
      return -1.0;
      
    }
    
  }
  
  /**
  * For a given traverser, generates a number between 0.0 (poor) and 1.0 (best) denoting
  * the performance of this traverser based upon the reduction in cost ('cost payoff') 
  * they have achieved between this round and the last. 
  * 
  * @return
  */
  public double performanceMetricCostChangePayoff(GraphTraverser traverser) {
    
    // -1 indicates that not enough information is available to calculate change (for example, if it is an early round).
    if ( latestTraverserRoundPerformance(traverser, Metric.COST_CHANGE) == -1 ) return 1.0; 
    
    // Any improvement in cost payoff is seen as wholly positive
    if ( latestTraverserRoundPerformance(traverser, Metric.COST_CHANGE) > 0 ) { 
      
      return 1.0;
      
      /* Any loss in cost payoff, greater than -100%, is taken as an absolute value, and inverted, as an
      * indicator of performance. For example, -40%, would indicated a 60% (0.6) performance, as decreasing
      * by 40% is not as bad as, say, -60%, which would indicate 40% (0.4) performance.
      */
      
    } else if ( latestTraverserRoundPerformance(traverser, Metric.COST_CHANGE) < 0 &&
    latestTraverserRoundPerformance(traverser, Metric.COST_CHANGE) > -100) {
      
      return 1.0 - (Math.abs(latestTraverserRoundPerformance(traverser, Metric.COST_CHANGE)) / 100);
      
      // Anything else must be greater than an increase in double of cost, and should thus indicate that change is necessary.
    } else {
      
      return 0.0;
      
    }
    
  }
  
  /**
  * Uses percentage change from *first* cost as an indicator of performance.
  * 
  * @param traverser
  * @return
  */
  public double performanceMetricRelativeCost(GraphTraverser traverser) {
    
    if ( roundNumber > 0 ) {
      
      return -1 * ( ( ( roundCost(roundNumber, false, traverser) - roundCost(0, false, traverser) ) / (double)(Math.abs(roundCost(0, false, traverser))) ) * 100);
      
    } else {
      
      return -1.0;
      
    }
    
  }
  
  /**
  * 
  */
  ArrayList<Double> averageSeekersRoundPerformance;
  
  /**
  * 
  */
  private void recordAverageSeekersRoundPerformance(Metric metric) {
    
    averageSeekersRoundPerformance.add(averageSeekersPerformance(metric));
    
  }
  
  /**
  * Does not allow to specify metric. Recalls information on average seeker
  * performance from records, which are recorded according to standard metric
  * (24/3 this metric is cost)
  * 
  * @return
  */
  public double averageSeekersRoundPerformance(int round) {
    
    if (round < 0) return 0.0;
    
    if ( round > averageSeekersRoundPerformance.size() - 1) round = averageSeekersRoundPerformance.size() - 1;
    
    return averageSeekersRoundPerformance.get(round);
    
  }
  
  /**
  * Get most recent average seekers performance, with supplied metric.
  * 
  * @param metric
  */
  public double averageSeekersPerformance(Metric metric) {
    
    double performance = 0;
    
    int seekers = 0;
    
    for ( GraphTraverser traverser : traversers ) {
      
      if ( traverser instanceof Seeker ) {
        
        seekers++;
        
        performance += latestTraverserRoundPerformance((Seeker)traverser, metric);
        
      }
      
    }
    
    return performance / ((double)seekers);
    
  }
  
  /**
  * @param latestRoundPaths
  * @return
  */
  public double totalPathCost(ArrayList<V> latestRoundPath) {
    
    Double totalCost = 0.0;
    
    Set<E> edges = this.edgeSet();
    
    for (int i = 1; i < latestRoundPath.size(); i++) {
      
      // Checks for direction of weighting
      if ( getEdgeWeight(getEdge(latestRoundPath.get(i - 1), latestRoundPath.get(i))) == 1.0 ) {
        
        totalCost += getEdgeWeight(getEdge(latestRoundPath.get(i), latestRoundPath.get(i - 1)));
        
      } else {
        
        totalCost += getEdgeWeight(getEdge(latestRoundPath.get(i - 1), latestRoundPath.get(i)));
        
      }
      
    }
    
    return totalCost;
    
  }
  
  /**
  * Get the search payoff awarded to a hider in a particular round
  * 
  * @param traverser
  * @return
  */
  public double hiderRoundRealtimePayoff(int roundNumber, GraphTraverser traverser) {
    
    if ( roundNumber > -1 ) {
      
      return hiderRoundPayoffs.get(roundNumber).get(traverser);
      
    } else {
      
      return 0.0;
      
    }
    
  }
  
  /**
  * @param roundNumber
  * @param traverser
  * @return
  */
  public double roundCost(int roundNumber, boolean normalised, GraphTraverser traverser) {
    
    if ( roundNumber > -1 && roundCosts.get(roundNumber).keySet().contains(traverser) && roundCosts.get(roundNumber).size() != 0) {
      
      if ( normalised ) {
        
        double minInSeries = Double.MAX_VALUE;
        
        double maxInSeries = Double.MIN_VALUE;
        
        for ( Entry<Integer, Hashtable<GraphTraverser, Double>> roundToCosts : roundCosts.entrySet() ) {
          
          for ( Entry<GraphTraverser, Double> traverserToCost : roundToCosts.getValue().entrySet() ) {
            
            if ( traverserToCost.getValue() > maxInSeries) { 
              
              maxInSeries = traverserToCost.getValue();
              
            } else if ( traverserToCost.getValue() < minInSeries) {
              
              minInSeries = traverserToCost.getValue();
              
            }
            
          }
          
        }
        
        double value = roundCosts.get(roundNumber).get(traverser);
        
        return (value - minInSeries) / (double)( maxInSeries - minInSeries);
        
      } else {
        
        return roundCosts.get(roundNumber).get(traverser);
        
      }
      
    } else {
      
      return 0.0;
      
    }
    
  }
  
  /**
  * @param roundNumber
  * @param traverser
  * @return
  */
  public ArrayList<V> roundPath(int roundNumber, GraphTraverser traverser) {
    
    if ( roundNumber > 0) {
      
      return roundPaths.get(roundNumber).get(traverser);
      
    } else {
      
      return null;
      
    }
    
  }
  
  /**
  * @param vertex
  */
  public void addVertexIfNonExistent(V vertex) {
    
    if ( !containsVertex(vertex) ) addVertex(vertex);
    
  }
  
  /**
  * @param sourceVertex
  * @param targetVertex
  */
  public void addEdgeIfNonExistent(V sourceVertex, V targetVertex) {
    
    if ( !this.containsEdge(sourceVertex, targetVertex) ) addEdge(sourceVertex, targetVertex);
    
  }
  
  /**
  * 
  */
  protected HashMap<V, Character> nodeTypes;
  
  /**
  * @return
  */
  public int numberOfHideLocations() {
    
    return hideLocations.size();
    
  }
  
  /**
  * The percentage by which to decrement the cost
  * of an edge once it has been traversed by an agent.
  * Intuition: Having traversed an edge once, it is `cheaper'
  * to traverse it again in the future.
  *  
  */
  protected double edgeTraversalDecrement = 1.0;
  
  /**
  * @return
  */
  public double getEdgeTraversalDecrement() {
    
    return edgeTraversalDecrement;
    
  }
  
  /**
  * @param edgeTraversalDecrement (as a percentage)
  */
  public void setEdgeTraversalDecrement(int edgeTraversalDecrement) {
    
    this.edgeTraversalDecrement = 1.0 - (edgeTraversalDecrement / 100.0);
    
  }

  /**
   * Track the amount of 'gas' each traverser has. Gas is subtracted
   * from upon traversal rather than adding to cost.
   * 
   */
  private HashMap<GraphTraverser, Double> traverserGas;

  /**
  * @param ef
  */
  public HiddenObjectGraph(EdgeFactory<V, E> ef) {
    
    super(ef);
    
    setup();
    
    traversers = new HashSet<GraphTraverser>();
    
    nodeTypes = new HashMap<V, Character>();
    
    hideLocations = new HashSet<V>();
    
  }
  
  /**
  * Creates a new simple graph.
  *
  * @param edgeClass class on which to base factory for edges
  */
  public HiddenObjectGraph(Class<? extends E> edgeClass) {
    
    super(new ClassBasedEdgeFactory<V, E>(edgeClass));
    
    setup();
    
    traversers = new HashSet<GraphTraverser>();
    
    nodeTypes = new HashMap<V, Character>();
    
    hideLocations = new HashSet<V>();
    
  }
  
  /**
  * 
  */
  private void setup() {
    
    traverserEdgeCosts = new HashMap<GraphTraverser, HashMap<E, Double>>();

    traverserGas = new HashMap<GraphTraverser, Double>();
    
    //
    
    traverserCost = new HashMap<GraphTraverser, Double>();
    
    traverserPathLength = new HashMap<GraphTraverser, Integer>();
    
    //
    
    roundCosts = new Hashtable<Integer, Hashtable<GraphTraverser, Double>>();
    
    roundCosts.put(0, new Hashtable<GraphTraverser, Double>());
    
    roundPaths = new Hashtable<Integer, Hashtable<GraphTraverser, ArrayList<V>>>();
    
    roundPaths.put(0, new Hashtable<GraphTraverser, ArrayList<V>>());
    
    hiderRoundPayoffs = new Hashtable<Integer, Hashtable<GraphTraverser, Double>>();
    
    seekerRoundPayoffs = new Hashtable<Integer, Hashtable<GraphTraverser, Double>>();
    
    averageSeekersRoundPerformance = new ArrayList<Double>();
    
  }
  
  /**
  * 
  * 
  * @param hideLocation
  */
  public void addHideLocation(V vertex) { 
    
    hideLocations.add(vertex);
    
    Utils.talk("Graph", "Adding Hide Location " + hideLocations + ". All locations: " + hideLocations);
    
  }
  
  /**
  * @param currentNode
  * @return
  */
  public boolean isHideLocation(V currentNode) {
    
    return hideLocations.contains(currentNode);
    
  }
  
  /**
  * 
  */
  private int numberOfNodeTypes;
  
  /**
  * @return
  */
  public int getNumberOfNodeTypes() {
    
    return numberOfNodeTypes;
    
  }
  
  /**
  * @param nodeTypes
  */
  public void setNodeTypes(char[] nodeTypes) {
    
    numberOfNodeTypes = nodeTypes.length;
    
    for ( V vertex : vertexSet() ) {
      
      setNodeType(vertex, nodeTypes[(int)(Math.random() * nodeTypes.length)]);
      
    }
    
  }
  
  /**
  * @param node
  * @param type
  */
  private void setNodeType(V node, char type) {
    
    nodeTypes.put(node, type);
    
  }
  
  /**
  * @param edgeToTarget
  */
  public char getNodeType(V edgeToTarget) {
    
    return nodeTypes.get(edgeToTarget);
    
  }
  
  /**
  * @param traverser
  * @param sourceVertex
  * @param targetVertex
  * @return
  */
  public boolean fromVertexToVertex(GraphTraverser traverser, V sourceVertex, V targetVertex) {
    
    if ( containsEdge(sourceVertex, targetVertex) ) {
      
      /* Update the cost for this traverser as their existing cost, plus their own unique cost
      of traversing this edge (which may have been previously decremented), decremented
      by the stated factor */
      
      E traversedEdge = getEdge(sourceVertex, targetVertex);
      
      double actualCost = this.getEdgeWeight(traversedEdge);
      
      // The unique cost to the traverser, based upon their traversals so far
      double uniqueCost = 0.0;
      
      if ( edgeTraversalDecrement != 1.0 && traverserEdgeCosts.get(traverser).get( traversedEdge ) == null ) {
        
        throw new UnsupportedOperationException("Edge traversal decrement not supported with round reset.");
        
      } else if ( traverserEdgeCosts.get(traverser).get( traversedEdge ) == null ) { 
        
        uniqueCost = actualCost;
        
      } else {
        
        uniqueCost = traverserEdgeCosts.get( traverser ).get( traversedEdge );
        
      }
      
      double newCost;
      
      // ~MDC TEMPORARY: only decrement costs for Hiders
      // if ( !(traverser instanceof Seeker) ) {
        
        // The new cost, based upon the edge that is currently being traversed
        newCost = uniqueCost * edgeTraversalDecrement;
        if(traverser instanceof GasGraphTraverser) {
          this.traverserGas.put(traverser, traverserGas.get(traverser) - newCost);
          newCost = traverserGas.get(traverser) > 0 ? 0 : newCost;
        }

      // } else {
        
        // newCost = uniqueCost;
        
      // }
        
      // Update round costs
      
      Hashtable<GraphTraverser, Double> thisRoundCostData = roundCosts.get(roundCosts.size() - 1);
      
      if (thisRoundCostData.containsKey(traverser)) {
        
        thisRoundCostData.put(traverser, thisRoundCostData.get(traverser) + newCost);
        
      } else {
        
        thisRoundCostData.put(traverser, newCost);
        
      }
      
      // Update round path
      
      Hashtable<GraphTraverser, ArrayList<V>> thisRoundPathData = roundPaths.get(roundPaths.size() - 1);
      
      if ( RECORD_PATH_DATA ) {
        
        if (thisRoundPathData.containsKey(traverser)) {
          
          ArrayList<V> traverserPath = thisRoundPathData.get(traverser);
          
          if (traverserPath.get(traverserPath.size() -1).equals(sourceVertex)) {
            
            traverserPath.add(targetVertex);
            
          } else {
            
            try {
              
              traverserPath.add(sourceVertex);
              
            } catch (java.lang.OutOfMemoryError e) {
              
              throw new UnsupportedOperationException(e.getMessage() + " \n " + e.getCause() + " \n Out of memory error at traverser add to path (Line 880). \n Traverser: " + traverser + " Source Vertex: " + sourceVertex + " Target Vertex: " + targetVertex + " traverserPath.size(): + " + traverserPath.size());
              
            }
            
          }
          
          thisRoundPathData.put(traverser, traverserPath);
          
        } else {
          
          ArrayList<V> traverserPath = new ArrayList<V>();
          
          traverserPath.add(sourceVertex);
          
          traverserPath.add(targetVertex);
          
          thisRoundPathData.put(traverser, traverserPath);
          
        }
        
      } else {
        
        // ~MDC To prevent potential null exceptions
        if (!thisRoundPathData.containsKey(traverser)) {
          
          ArrayList<V> traverserPath = new ArrayList<V>();
          
          thisRoundPathData.put(traverser, traverserPath);
          
        }
        
      }
      
      // Update total costs and path length
      
      traverserCost.put( traverser, traverserCost.get(traverser) + newCost );
      
      traverserPathLength.put( traverser, traverserPathLength.get(traverser) + 1 );
      
      // Update future weights
      
      traverserEdgeCosts.get( traverser ).put( traversedEdge , newCost );
      
      return true;
      
    } 
    
    return false;
    
  }
      
  /**
  * @param traverser
  * @param sourceVertex
  * @param targetVertex
  * @return
  */
  public List<E> pathFromVertexToVertex(V sourceVertex, V targetVertex) {
    
    return DijkstraShortestPath.findPathBetween(this, sourceVertex, targetVertex);
    
  }
  
  /**
  * Request the costs associated with a particular edge, *without* actually traversing 
  * that edge
  * 
  * @param traverser
  * @param sourceVertex
  * @param targetVertex
  * @return
  */
  public double traverserEdgeCost(GraphTraverser traverser, V sourceVertex, V targetVertex) {
    
    if ( containsEdge(sourceVertex, targetVertex) ) {
      
      return traverserEdgeCosts.get( traverser ).get( getEdge(sourceVertex, targetVertex) );
      
    } 
    
    return -1;
    
  }
  
  /**
  * Maintain a list of traversers, and the cost they
  * have accrued searching this graph
  */
  private HashMap<GraphTraverser, Double> traverserCost;
  
  /**
  * @param traverser
  * @return
  */
  public double averageGameCosts(GraphTraverser traverser) {
    
    return traverserCost.get(traverser) / roundNumber;
    
  }

  /**
   * @param traverser
   * @return
   */
  public double successfulGameTraversal(GraphTraverser traverser) {

    return averageGameCosts(traverser) < totalEdgeCosts(traverser) ? 1 : 0;

  }
  
  /**
  * 
  */
  private HashMap<GraphTraverser, Integer> traverserPathLength;
  
  /**
  * @param traverser
  * @return
  */
  public double averagePathLength(GraphTraverser traverser) {
    
    return traverserPathLength.get(traverser) / roundNumber;
    
  }
  
  /**
  * @param hider
  * @return
  */
  public double averageHiderPayoff(GraphTraverser hider) {
    
    Double cumulativePayoffs = 0.0;
    
    for (Entry<Integer, Hashtable<GraphTraverser, Double>> entry : hiderRoundPayoffs.entrySet()) {
      
      cumulativePayoffs += entry.getValue().get(hider);
      
    }
    
    // -1 because first round yields 0 (only under current metric though)
    return cumulativePayoffs / roundNumber; //(roundNumber - 1);
    
  }
  
  /**
  * @param seeker
  * @return
  */
  public double averageSeekerPayoff(GraphTraverser seeker) {
    
    Double cumulativePayoffs = 0.0;
    
    for (Entry<Integer, Hashtable<GraphTraverser, Double>> entry : seekerRoundPayoffs.entrySet()) {
      
      cumulativePayoffs += entry.getValue().get(seeker);
      
    }
    
    return cumulativePayoffs / roundNumber;
    
  }
  
  /**
  * 
  */
  private HashSet<GraphTraverser> traversers;
  
  /**
  * For each Traverser, maintain an individual cost value for each
  * edge which is based on the number of times they have traversed that 
  * edge before
  */
  private HashMap<GraphTraverser, HashMap<E, Double>> traverserEdgeCosts;
  
  /**
  * Resets for experiment with 'next hider'
  */
  public void resetGameEnvironment() {
    
    roundNumber = 0;
    
    setup();
    
    HashSet<GraphTraverser> localTraversers = new HashSet<GraphTraverser>(traversers);
    
    for (GraphTraverser traverser : traversers) {
      
      resetTraversingAgent(traverser);
      
    }
    
  }
  
  private void resetTraversingAgent(GraphTraverser traverser) {
    
    // Set initial costs (path length resp.) to 0.0
    traverserCost.put(traverser, 0.0);
    
    // Set initial path lengths to 0
    traverserPathLength.put(traverser, 0);
    
    // Add a new unique edge cost mapping for this traverser
    traverserEdgeCosts.put(traverser, new HashMap<E, Double>());
    
    // To begin with, set each edge mapping to its default weight
    for ( E edge : edgeSet() ) {
      
      traverserEdgeCosts.get(traverser).put(edge, getEdgeWeight(edge));
      
    }
    
    if(traverser instanceof GasGraphTraverser) traverserGas.put(traverser, (traverser instanceof HighGasGraphTraverser ? this.totalEdgeCosts(traverser) / Gas.HIGH_GAS_PROPORTION : traverser instanceof MediumGasGraphTraverser ? this.totalEdgeCosts(traverser) / Gas.MEDIUM_GAS_PROPORTION : this.totalEdgeCosts(traverser) / Gas.LOW_GAS_PROPORTION));

  }
  
  /* (non-Javadoc)
  * @see org.jgrapht.graph.AbstractGraph#toString()
  */
  public String toString() {
    
    return "Graph";
    
  }
  
  /**
  * @param traverser
  */
  public void registerTraversingAgent(GraphTraverser traverser) {
    
    if ( traversers.contains(traverser) ) return; //throw new UnsupportedOperationException("Attempted to register the same traverser twice.");
    
    Utils.talk(toString(), "Registering " + traverser);
    
    // Keep track of who is traversing the graph
    traversers.add(traverser);
    
    resetTraversingAgent(traverser);
    
  }
  
  /**
  * @param traverser
  */
  public void deregisterTraversingAgent(GraphTraverser traverser) {
    
    // Keep track of who is traversing the graph
    traversers.remove(traverser);
    
    // Set initial costs (path length resp.) to 0.0
    traverserCost.remove(traverser);
    
    // Set initial path lengths to 0
    traverserPathLength.remove(traverser);
    
    // Add a new unique edge cost mapping for this traverser
    traverserEdgeCosts.remove(traverser);

    traverserGas.remove(traverser);
    
  }
  
  /**
  * @param traverser
  * @return
  */
  public double totalEdgeCosts(GraphTraverser traverser) {
    
    double totalEdgeCost = 0.0;
    
    for ( Entry<E, Double> edgeWeight : traverserEdgeCosts.get(traverser).entrySet() ) {
      
      totalEdgeCost += edgeWeight.getValue();
      
    }
    
    return totalEdgeCost;
    
  }
  
  /**
  * @param target
  * @param source
  * @param fixedOrUpperValue
  */
  public void addEdgeWithWeight(V source, V target, double weight) {
    
    this.addEdge(source, target);
    
    setEdgeWeight(this.getEdge(source, target), weight);
    
  }
  
}
