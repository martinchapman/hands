package HideAndSeek.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;

import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import HideAndSeek.GraphTraverser;
import HideAndSeek.hider.Hider;
import HideAndSeek.seeker.Seeker;
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
		
		calculateHiderScores();
		
		// Calculate average seeker round performance for access by adaptive Hiders
		averageSeekersRoundPerformance();
		
		roundNumber++;
		
    	roundCosts.put(roundNumber, new Hashtable<GraphTraverser, Double>());
    	
    	roundPaths.put(roundNumber, new Hashtable<GraphTraverser, ArrayList<V>>());
    	
		clearHideLocations();
		
	}
	
	/**
	 * @param traverser
	 * @return
	 */
	public double latestRoundCosts(GraphTraverser traverser) {
		
		if (roundCosts.get(roundCosts.size() - 1).get(traverser) == null) { return 0.0; }
		
		return roundCosts.get(roundCosts.size() - 1).get(traverser);
		
	}
	
	/**
	 * @param traverser
	 * @return
	 */
	public ArrayList<V> latestRoundPaths(GraphTraverser traverser) {
		
		if (roundPaths.get(roundPaths.size() - 1).get(traverser) == null) { return new ArrayList<V>(); }
		
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
	private Hashtable<Integer, Hashtable<GraphTraverser, Double>> hiderRoundScores;
	
	/**
	 * Individual round scores for each seeker
	 */
	private Hashtable<Integer, Hashtable<GraphTraverser, Double>> seekerRoundScores;
	
	/**
	 * Hider scores are awarded in terms of two conflicting metrics: the cost of graph traversal
	 * and the performance of the seeker.
	 * 
	 * An hider can reduce cost by reusing the same paths through the graph, but in doing
	 * so leaves themselves open to predictable behaviour.
	 * 
	 * The best scoring hider is thus one who is able to balance the exploration of used and 
	 * explorative paths, in order to balance cost and predictability of behaviour.
	 * 
	 * Note that score is not exempt from features of nature, such as the topology, or the
	 * number of items to be hidden
	 */
	private void calculateHiderScores() {
		
    	hiderRoundScores.put(roundNumber, new Hashtable<GraphTraverser, Double>());
    
		for ( GraphTraverser agent : traversers ) {
			
			double scoreAgainstEach = 0.0;
			
			int opponents = 0;
			
			if ( agent instanceof Hider ) {
				
				// We do not play all registered Hiders at once, so not all will be scored
				if ( !((Hider)agent).isPlaying() ) { continue; }
				
				for ( GraphTraverser seeker : traversers ) {
					
					if ( seeker instanceof Seeker ) {
						
						opponents++;
						
						/* To balance, scores take into account the performance of the seeker MINUS the 
						   costs of the hider. Thus, if costs are too high or a seeker's performance
						   is too good, score is likely to be lower. */
										    // 
						scoreAgainstEach += requestLatestSeekerRoundPerformance((Seeker)seeker); //- 
										    /* The hiders cost on their hiding path as a portion of the full cost
											   of this path i.e. their cost, lowered if they take pre-traversed roots
											   as a portion of full cost path */
											//((latestRoundCosts(agent) / totalPathCost(latestRoundPaths(agent))) * 100);
						
					}
				
				}
				
				hiderRoundScores.get(hiderRoundScores.size() - 1).put(agent, (scoreAgainstEach / opponents));
				
				calculateSeekerScores((Hider)agent);
				
			}
			
		}
		
	}
	
	/**
	 * @param hider
	 */
	private void calculateSeekerScores(Hider hider) {
		
		seekerRoundScores.put(roundNumber, new Hashtable<GraphTraverser, Double>());
		
		for ( GraphTraverser traverser : traversers ) {
			 
			if ( traverser instanceof Seeker ) {
				 
				 /*
				 * Set Seeker's round score: steps of hider - cost of traversal + designated measure of performance 
				 * (as depicted by requestLatestSeekerRoundPerformance method)
				 * 
				 * i.e. Perfect seeker score should be 0 -> steps taken by hider - steps taken by seeker
				 */
				seekerRoundScores.get(roundNumber).put(traverser, 
													  //requestLatestHiderRoundPerformance(hider) -
													  (//latestRoundCosts(traverser) 
													  //+ 
													  requestLatestSeekerRoundPerformance((Seeker)traverser) * -1));
				
				
			}
			 
		}
		
	}
	
	/**
	 * @param hider
	 * @return
	 */
	public double requestLatestHiderRoundPerformance(Hider hider) {
		
		return latestRoundPaths(hider).size() / ((double)edgeSet().size()) * 100;
		
	}
	
	/**
	 * Designated metric for a Seeker's performance
	 * 
	 * @param seeeker
	 * @return performance as percentage
	 */
	public double requestLatestSeekerRoundPerformance(Seeker seeker) {
		
		/* Higher is worse for seeker: represents steps taken as a portion of
		   all possible steps in graph */
		return latestRoundPaths(seeker).size() / ((double)edgeSet().size()) * 100;
		
	}
	
	/**
	 * 
	 */
	ArrayList<Double> averageSeekersRoundPerformance;
	
	/**
	 * 
	 */
	private void averageSeekersRoundPerformance() {
		
		double performance = 0;
		 
		int seekers = 0;
		 
		for ( GraphTraverser traverser : traversers ) {
			 
			if ( traverser instanceof Seeker ) {
				 
				seekers++;
				
				performance += requestLatestSeekerRoundPerformance((Seeker)traverser);
				 
			}
			 
		}
		 
		averageSeekersRoundPerformance.add(performance / ((double)seekers));
		 
	}
	
	/**
	 * @return
	 */
	public double requestAverageSeekersRoundPerformance(int round) {
		 
		 if (round < 0) { return 0.0; }
		 
		 return averageSeekersRoundPerformance.get(round);
		
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
	public double requestHiderRoundScore(int roundNumber, GraphTraverser traverser) {
		
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
	public double requestRoundCost(int roundNumber, GraphTraverser traverser) {
		
		if ( roundNumber > -1 ) {
		
			return roundCosts.get(roundNumber).get(traverser);
		
		} else {
			
			return 0.0;
			
		}
		
	}
	
	/**
	 * @param roundNumber
	 * @param traverser
	 * @return
	 */
	public ArrayList<V> requestRoundPath(int roundNumber, GraphTraverser traverser) {
		
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
        
    	traversers = new HashSet<GraphTraverser>();
    	
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
        
    	traversers = new HashSet<GraphTraverser>();
    	
    	nodeTypes = new HashMap<V, Character>();
    	
    	hideLocations = new ArrayList<V>();
    	
    }
    
    /**
     * 
     */
    private void setup() {
    	
    	traverserEdgeCosts = new HashMap<GraphTraverser, HashMap<E, Double>>();
    	
    	//
    	
    	traverserCost = new HashMap<GraphTraverser, Double>();
    	
    	traverserPathLength = new HashMap<GraphTraverser, Integer>();
    	
    	//
    	
    	roundCosts = new Hashtable<Integer, Hashtable<GraphTraverser, Double>>();
    	
    	roundCosts.put(0, new Hashtable<GraphTraverser, Double>());
    	
    	roundPaths = new Hashtable<Integer, Hashtable<GraphTraverser, ArrayList<V>>>();
    	
    	roundPaths.put(0, new Hashtable<GraphTraverser, ArrayList<V>>());
    	
    	hiderRoundScores = new Hashtable<Integer, Hashtable<GraphTraverser, Double>>();
    	
    	seekerRoundScores = new Hashtable<Integer, Hashtable<GraphTraverser, Double>>();
    	
    	averageSeekersRoundPerformance = new ArrayList<Double>();
    	
    }
    
    /**
     * 
     */
    public void newGame() {
    	
    	roundNumber = 0;
    	
    	setup();
    	
    	for (GraphTraverser traverser : traversers) {
    	
	    	registerTraversingAgent(traverser);
		
    	}
    	
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
			double uniqueCost = traverserEdgeCosts.get( traverser ).get( traversedEdge );
			
			// The new cost, based upon the edge that is currently being traversed
		    double newCost = uniqueCost * edgeTraversalDecrement;
		    
			// Update round costs
		    
			Hashtable<GraphTraverser, Double> thisRoundCostData = roundCosts.get(roundCosts.size() - 1);
			
			if (thisRoundCostData.containsKey(traverser)) {
				
				thisRoundCostData.put(traverser, thisRoundCostData.get(traverser) + uniqueCost);
				
			} else {
			
				thisRoundCostData.put(traverser, uniqueCost);
				
			}
			
			// Update round path
			
			Hashtable<GraphTraverser, ArrayList<V>> thisRoundPathData = roundPaths.get(roundPaths.size() - 1);
			
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
			
			traverserCost.put( traverser, traverserCost.get(traverser) + uniqueCost );
			
			traverserPathLength.put( traverser, traverserPathLength.get(traverser) + 1 );
		
			// Update future weights
			
			traverserEdgeCosts.get( traverser ).put( traversedEdge , newCost );
			
			return true;
			
		} 
		
		return false;
		
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
	public double requestAverageGameCosts(GraphTraverser traverser) {
		
		return traverserCost.get(traverser) / roundNumber;
		
	}
	
	/**
	 * 
	 */
	private HashMap<GraphTraverser, Integer> traverserPathLength;
	
	/**
	 * @param traverser
	 * @return
	 */
	public double requestAveragePathLength(GraphTraverser traverser) {
		
		return traverserPathLength.get(traverser) / roundNumber;
		
	}
	
	/**
	 * @param hider
	 * @return
	 */
	public double requestAverageHiderScore(GraphTraverser hider) {
		
		Double cumulativeScores = 0.0;
		
		for (Entry<Integer, Hashtable<GraphTraverser, Double>> entry : hiderRoundScores.entrySet()) {
			
			cumulativeScores += entry.getValue().get(hider);
			
		}
		
		return cumulativeScores / roundNumber;
		
	}
	
	/**
	 * @param seeker
	 * @return
	 */
	public double requestAverageSeekerScore(GraphTraverser seeker) {
		
		Double cumulativeScores = 0.0;
		
		for (Entry<Integer, Hashtable<GraphTraverser, Double>> entry : seekerRoundScores.entrySet()) {
			
			cumulativeScores += entry.getValue().get(seeker);
			
		}
		
		return cumulativeScores / roundNumber;
		
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
	 * @param traverser
	 */
	public void registerTraversingAgent(GraphTraverser traverser) {
		
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
		
		this.setEdgeWeight(this.getEdge(source, target), weight);
		
	}
	
}
