package HideAndSeek.graph;

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

import HideAndSeek.GraphTraversingAgent;
import HideAndSeek.GraphTraversingAgent;
import HideAndSeek.GraphTraverser;
import HideAndSeek.hider.HidingAgent;
import HideAndSeek.hider.Hider;
import HideAndSeek.seeker.SeekingAgent;
import Utility.ScoreMetric;
import Utility.Utils;

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
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	protected ArrayList<V> hideLocations;
	
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
	 * scores.
	 */
	private Hashtable<Integer, Hashtable<GraphTraversingAgent, Double>> roundCosts;
	
	/**
	 * Maps each round to its participating traversers, and their traversal
	 * paths.
	 */
	private Hashtable<Integer, Hashtable<GraphTraversingAgent, ArrayList<V>>> roundPaths;
	
	/**
	 * 
	 */
	public void notifyEndOfRound() {
		
    	calculateRealtimeHiderScores();
		
		// Calculate average seeker round performance for access by adaptive Hiders
    	recordAverageSeekersRoundPerformance(ScoreMetric.COST);
		
		roundNumber++;
		
		roundCosts.put(roundNumber, new Hashtable<GraphTraversingAgent, Double>());
    	
    	roundPaths.put(roundNumber, new Hashtable<GraphTraversingAgent, ArrayList<V>>());
		
		clearHideLocations();
		
	}
	
	/**
	 * @param traverser
	 * @return
	 */
	public double latestRoundCosts(GraphTraverser traverser, boolean normalised) {
		
		// May have blank records in most recent entry introduced by increment of round number
		if (roundCosts.get(roundCosts.size() - 1).get(traverser) == null && roundCosts.get(roundCosts.size() - 2).get(traverser) == null) return 0.0;
		
		if (roundCosts.get(roundCosts.size() - 1).get(traverser) == null && roundCosts.get(roundCosts.size() - 2).get(traverser) != null) return roundCost(roundCosts.size() - 2, normalised, traverser);
		
		return roundCost(roundCosts.size() - 1, normalised, traverser);
		
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
	public double latestHiderRoundScores(GraphTraverser traverser) {
		
		return hiderRoundScores.get(hiderRoundScores.size() - 1).get(traverser);
		
	}
	
	/**
	 * Individual round scores for each hider
	 */
	private Hashtable<Integer, Hashtable<GraphTraversingAgent, Double>> hiderRoundScores;
	
	/**
	 * Individual round scores for each seeker
	 */
	private Hashtable<Integer, Hashtable<GraphTraversingAgent, Double>> seekerRoundScores;
	
	/**
	 * The effectiveness of a hide strategy is judged from two perspectives: the performance
	 * of the seeker when this strategy is employed, and the performance of the hider under
	 * this strategy. Commonly, the performance of a hider is inversely proportional to their
	 * traversal cost (so the higher the cost, the lower their performance, and vice-versa).
	 * 
	 * The lower the performance of the seeker the better the strategy is perceived to be.
	 * The higher the performance of the hider the better the strategy is perceived to be.
	 * 
	 * Hider score is therefore calculated as the performance of the hider, minus the 
	 * performance of the seeker. Thus, if performance is too low or a seeker's performance
	 * is too good, score is likely to be lower.
	 * 
	 * These scores are normalised as best possible, according to existing records.
	 * 
	 */
	private void calculateRealtimeHiderScores() {
		
    	hiderRoundScores.put(roundNumber, new Hashtable<GraphTraversingAgent, Double>());
    
		for ( GraphTraversingAgent agent : traversers ) {
			
			double scoreAgainstEach = 0.0;
			
			int opponents = 0;
			
			if ( agent instanceof HidingAgent ) {
				
				// We do not play all registered Hiders at once, so not all will be scored
				if ( !((HidingAgent)agent).isPlaying() ) { continue; }
				
				for ( GraphTraverser seeker : traversers ) {
					
					if ( seeker instanceof SeekingAgent ) {
						
						opponents++;
						
						scoreAgainstEach += latestTraverserRoundPerformance((HidingAgent)agent, ScoreMetric.COST) -
											latestTraverserRoundPerformance((SeekingAgent)seeker, ScoreMetric.COST);
										   
					}
				
				}
				
				hiderRoundScores.get(hiderRoundScores.size() - 1).put(agent, (scoreAgainstEach / opponents));
				
				calculateRealtimeSeekerScores((Hider)agent);
				
			}
			
		}
		
	}
	
	/**
	 * Seeker score is currently more simplistic, simply their own performance.
	 * 
	 * @param hider
	 */
	private void calculateRealtimeSeekerScores(Hider hider) {
		
		seekerRoundScores.put(roundNumber, new Hashtable<GraphTraversingAgent, Double>());
		
		for ( GraphTraversingAgent traverser : traversers ) {
			 
			if ( traverser instanceof SeekingAgent ) {
				 
				seekerRoundScores.get(roundNumber).put(traverser, latestTraverserRoundPerformance((SeekingAgent)traverser, ScoreMetric.COST_CHANGE));
				
			}
			 
		}
		
	}
	
	/**
	 * @param traverser
	 * @param metric
	 * @return
	 */
	public double latestTraverserRoundPerformance(GraphTraversingAgent traverser, int metric) {
		
		return latestTraverserRoundPerformance(traverser, metric, false);
		
	}
	
	/**
	 * 
	 * 
	 * @param traverser
	 * @return
	 */
	public double latestTraverserRoundPerformance(GraphTraversingAgent traverser, int metric, boolean normalised) {
		
		if ( metric == ScoreMetric.COST ) {
			
			return performanceMetricCost(traverser, normalised);
			
		} else if ( metric == ScoreMetric.PATH ) {
			
			return performanceMetricPath(traverser);
			
		} else if ( metric == ScoreMetric.COST_CHANGE ) {
			
			return performanceMetricCostChange(traverser);
		
		} else if ( metric == ScoreMetric.RELATIVE_COST ) {
			
			return performanceMetricRelativeCost(traverser); 
			
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
			
			System.out.println(roundCost(roundNumber - 1, false, traverser) + " --> " + roundCost(roundNumber, false, traverser));
			
			// Because any decrease in cost (i.e. -10%) is actually a good thing for the player, and any increase is bad, flip the sign.
			return -1 * ( ( ( roundCost(roundNumber, false, traverser) - roundCost(roundNumber - 1, false, traverser) ) / (double)(Math.abs(roundCost(roundNumber - 1, false, traverser))) ) * 100);
			
		} else {
			
			return -1.0;
			
		}
		
	}
	
	/**
	 * Uses percentage change from *first* cost as an indicator of performance
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
	private void recordAverageSeekersRoundPerformance(int metric) {
		 
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
	 * Get most recent average seekers performance, with supplied metric
	 * 
	 * @param metric
	 */
	public double averageSeekersPerformance(int metric) {
		
		double performance = 0;
		 
		int seekers = 0;
		 
		for ( GraphTraverser traverser : traversers ) {
			 
			if ( traverser instanceof SeekingAgent ) {
				 
				seekers++;
				
				performance += latestTraverserRoundPerformance((SeekingAgent)traverser, metric);
				 
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
	 * Get the search score awarded to a traverser in a particular round
	 * 
	 * @param traverser
	 * @return
	 */
	public double hiderRoundRealtimeScore(int roundNumber, GraphTraverser traverser) {
		
		if ( roundNumber > -1 ) {
			
			return hiderRoundScores.get(roundNumber).get(traverser);
		
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
		
		if ( roundNumber > -1 && roundCosts.get(roundNumber).size() != 0) {
			
			if ( normalised ) {
				
				double minInSeries = Double.MAX_VALUE;
				
				double maxInSeries = Double.MIN_VALUE;
				
				for ( Entry<Integer, Hashtable<GraphTraversingAgent, Double>> roundToCosts : roundCosts.entrySet() ) {
					
					for ( Entry<GraphTraversingAgent, Double> traverserToCost : roundToCosts.getValue().entrySet() ) {
						
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
	 * @param ef
	 */
	public HiddenObjectGraph(EdgeFactory<V, E> ef) {
		
        super(ef);
        
        setup();
        
    	traversers = new HashSet<GraphTraversingAgent>();
    	
    	nodeTypes = new HashMap<V, Character>();
    	
    	hideLocations = new ArrayList<V>();
        
    }

    /**
     * Creates a new simple graph.
     *
     * @param edgeClass class on which to base factory for edges
     */
    public HiddenObjectGraph(Class<? extends E> edgeClass) {
    	
        super(new ClassBasedEdgeFactory<V, E>(edgeClass));
     
        setup();
        
    	traversers = new HashSet<GraphTraversingAgent>();
    	
    	nodeTypes = new HashMap<V, Character>();
    	
    	hideLocations = new ArrayList<V>();
    	
    }
    
    /**
     * 
     */
    private void setup() {
    	
    	traverserEdgeCosts = new HashMap<GraphTraversingAgent, HashMap<E, Double>>();
    	
    	//
    	
    	traverserCost = new HashMap<GraphTraversingAgent, Double>();
    	
    	traverserPathLength = new HashMap<GraphTraversingAgent, Integer>();
    	
    	//
    	
    	roundCosts = new Hashtable<Integer, Hashtable<GraphTraversingAgent, Double>>();
    	
    	roundCosts.put(0, new Hashtable<GraphTraversingAgent, Double>());
    	
    	roundPaths = new Hashtable<Integer, Hashtable<GraphTraversingAgent, ArrayList<V>>>();
    	
    	roundPaths.put(0, new Hashtable<GraphTraversingAgent, ArrayList<V>>());
    	
    	hiderRoundScores = new Hashtable<Integer, Hashtable<GraphTraversingAgent, Double>>();
    	
    	seekerRoundScores = new Hashtable<Integer, Hashtable<GraphTraversingAgent, Double>>();
    	
    	averageSeekersRoundPerformance = new ArrayList<Double>();
    	
    }
    
    /**
     * Resets for experiment with 'next hider'
     */
    public void resetGameEnvironment() {
    	
    	roundNumber = 0;
    	
    	setup();
    	
    	for (GraphTraversingAgent traverser : traversers) {
    	
	    	registerTraversingAgent(traverser);
		
    	}
    	
    	Utils.talk("Graph", traverserEdgeCosts.toString());
    	
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
	public boolean fromVertexToVertex(GraphTraversingAgent traverser, V sourceVertex, V targetVertex) {
		
		if ( containsEdge(sourceVertex, targetVertex) ) {
			
			/* Update the cost for this traverser as their existing cost, plus their own unique cost
			   of traversing this edge (which may have been previously decremented), decremented
			   by the stated factor */
			
			E traversedEdge = getEdge(sourceVertex, targetVertex);
			
			double actualCost = this.getEdgeWeight(traversedEdge);
		    
			// The unique cost to the traverser, based upon their traversals so far
			double uniqueCost = traverserEdgeCosts.get( traverser ).get( traversedEdge );
			
			double newCost;
			
			// ~MDC TEMPORARY: only decrement costs for Hiders
			// if ( !(traverser instanceof Seeker) ) {
				
				// The new cost, based upon the edge that is currently being traversed
			    newCost = uniqueCost * edgeTraversalDecrement;
		    
			//} else {
				
				//newCost = uniqueCost;
				
			//}
			    
			// Update round costs
		    
			Hashtable<GraphTraversingAgent, Double> thisRoundCostData = roundCosts.get(roundCosts.size() - 1);
			
			if (thisRoundCostData.containsKey(traverser)) {
				
				thisRoundCostData.put(traverser, thisRoundCostData.get(traverser) + newCost);
				
			} else {
			
				thisRoundCostData.put(traverser, newCost);
				
			}
			
			// Update round path
			
			Hashtable<GraphTraversingAgent, ArrayList<V>> thisRoundPathData = roundPaths.get(roundPaths.size() - 1);
			
			if (thisRoundPathData.containsKey(traverser)) {
				
				ArrayList<V> traverserPath = thisRoundPathData.get(traverser);
				
				if (traverserPath.get(traverserPath.size() -1).equals(sourceVertex)) {
					
					traverserPath.add(targetVertex);
					
				} else {
					
					traverserPath.add(sourceVertex);
					
				}
				
				thisRoundPathData.put(traverser, traverserPath);
				
			} else {
			
				ArrayList<V> traverserPath = new ArrayList<V>();
				
				traverserPath.add(sourceVertex);
				
				traverserPath.add(targetVertex);
				
				thisRoundPathData.put(traverser, traverserPath);
				
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
	public List<E> pathFromVertexToVertex(GraphTraverser traverser, V sourceVertex, V targetVertex) {
		
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
	private HashMap<GraphTraversingAgent, Double> traverserCost;
	
	/**
	 * @param traverser
	 * @return
	 */
	public double averageGameCosts(GraphTraverser traverser) {
		
		return traverserCost.get(traverser) / roundNumber;
		
	}
	
	/**
	 * 
	 */
	private HashMap<GraphTraversingAgent, Integer> traverserPathLength;
	
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
	public double averageHiderScore(GraphTraverser hider) {
		
		Double cumulativeScores = 0.0;
		
		for (Entry<Integer, Hashtable<GraphTraversingAgent, Double>> entry : hiderRoundScores.entrySet()) {
			
			cumulativeScores += entry.getValue().get(hider);
			
		}
		
		// -1 because first round yields 0 (only under current metric though)
		return cumulativeScores / roundNumber; //(roundNumber - 1);
		
	}
	
	/**
	 * @param seeker
	 * @return
	 */
	public double averageSeekerScore(GraphTraverser seeker) {
		
		Double cumulativeScores = 0.0;
		
		for (Entry<Integer, Hashtable<GraphTraversingAgent, Double>> entry : seekerRoundScores.entrySet()) {
			
			cumulativeScores += entry.getValue().get(seeker);
			
		}
		
		return cumulativeScores / roundNumber;
		
	}
	
	/**
	 * 
	 */
	private HashSet<GraphTraversingAgent> traversers;
	
	/**
	 * For each Traverser, maintain an individual cost value for each
	 * edge which is based on the number of times they have traversed that 
	 * edge before
	 */
	private HashMap<GraphTraversingAgent, HashMap<E, Double>> traverserEdgeCosts;

	/**
	 * @param traverser
	 */
	public void registerTraversingAgent(GraphTraversingAgent traverser) {
		
		// Keep track of who is traversing the graph
		traversers.add(traverser);
		
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
