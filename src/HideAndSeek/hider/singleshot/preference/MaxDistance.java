package HideAndSeek.hider.singleshot.preference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.alg.FloydWarshallShortestPaths;
import org.jgrapht.alg.KruskalMinimumSpanningTree;

import HideAndSeek.GraphTraverser;
import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.singleshot.random.RandomSetMechanism;
import Utility.Utils;

/**
 * Attempts to hide nodes with a maximum possible distance 
 * between them.
 * 
 * @author Martin
 *
 */
public class MaxDistance extends PreferenceHider {
    
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public MaxDistance( GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations) {
		
		this(graphController, "", numberOfHideLocations, 1.0, -1, null);
		
	}
	
	/**
	 * @param graphController
	 * @param name
	 * @param numberOfHideLocations
	 */
	public MaxDistance( GraphController<StringVertex, StringEdge> graphController, String name, int numberOfHideLocations) {
		
		this(graphController, name, numberOfHideLocations, 1.0, -1, null);
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public MaxDistance( GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations, GraphTraverser responsibleAgent) {
		
		this(graphController, "", numberOfHideLocations, 1.0, -1, responsibleAgent);
		
	}
	
	/**
	 * @param graphController
	 * @param name
	 * @param numberOfHideLocations
	 * @param responsibleAgent
	 */
	public MaxDistance( GraphController<StringVertex, StringEdge> graphController, String name, int numberOfHideLocations, GraphTraverser responsibleAgent) {
		
		this(graphController, name, numberOfHideLocations, 1.0, -1, responsibleAgent);
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 * @param graphPortion
	 * @param minDistance
	 */
	public MaxDistance( GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations, double graphPortion, int minDistance ) {
		
		this(graphController, "", numberOfHideLocations, graphPortion, minDistance, null);
		
	}
	
	public MaxDistance( GraphController<StringVertex, StringEdge> graphController, String name, int numberOfHideLocations, double graphPortion, int minDistance ) {
		
		this(graphController, name, numberOfHideLocations, graphPortion, minDistance, null);
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public MaxDistance( GraphController<StringVertex, StringEdge> graphController, String name, int numberOfHideLocations, double graphPortion, int minDistance, GraphTraverser responsibleAgent ) {
		
		super(graphController, name, numberOfHideLocations, graphPortion, responsibleAgent);
		
		this.minDistance = minDistance;
		
		maxDistanceNodes = new LinkedHashSet<StringVertex>();

	}
	
	/**
	 * Local copy of max distance nodes
	 */
	private LinkedHashSet<StringVertex> maxDistanceNodes;
	
	/**
	 * The minimum distance to look for between nodes (if -1, default is 
	 * set to local graph diameter)
	 */
	private int minDistance = -1;
	
	@Override
	public LinkedHashSet<StringVertex> computeTargetNodes() {
		
		DijkstraShortestPath<StringVertex, StringEdge> DSP = null;

		LinkedHashSet<StringVertex> targetVertices = new LinkedHashSet<StringVertex>();
		
		/* 
		 * For a node to be considered as a candidate for the kth position in the hide set, it must be at a 
		 * certain distance from the other nodes in the hide set. There may be multiple nodes that fulfil this 
		 * requirement for each kth position, so we store these. As a result of this, this recursively introduces 
		 * another requirement for each kth position: a node must be at a certain given distance, or greater, than all 
		 * previous permutations of all previous potential nodes. 
		 * 
		 * e.g. 
		 * 
		 *      0       1         2       3
		 *     [v1]   [v3]    [v6, v2]
		 *     
		 * This implies that v6 and v2 are at the same given distance from v3 and v1. Thus, for a node to be a candidate
		 * for position 3, it must exist at a sufficient distance from both v1, v3, and v6 or v1, v3 and v2. 
		 * 
		 * This is done to increase the chance that we find suitably spaced nodes, by altering earlier candidate nodes.
		 */
		Hashtable<Integer, ArrayList<StringVertex>> kthPositionCandidates = new Hashtable<Integer, ArrayList<StringVertex>>();
		
		FloydWarshallShortestPaths<StringVertex, StringEdge> FWSP = new FloydWarshallShortestPaths<StringVertex, StringEdge>(localGraph);
		
		// Start by considering two members of the set that always exist: those two nodes on each end of the graph diameter.
		outerGP:
		for ( GraphPath<StringVertex, StringEdge> GP : FWSP.getShortestPaths() ) {
			
			if ( GP.getWeight() == FWSP.getDiameter() ) {
				
				for ( ArrayList<StringVertex> candidates : kthPositionCandidates.values() ) {
					
					for ( StringVertex candidate : candidates ) {
						
						// Prevents adding the reverse of the diameter
						if ( candidate.equals(GP.getStartVertex()) || candidate.equals(GP.getEndVertex())) continue outerGP;
						
					}
					
				}
				
				Utils.add(kthPositionCandidates, 0, GP.getStartVertex(), new ArrayList<StringVertex>(), false);
				
				Utils.add(kthPositionCandidates, 1, GP.getEndVertex(), new ArrayList<StringVertex>(), false);
				
			}
			
		}
		
		// If, for whatever reason, the diameter is empty, cannot proceed with this strategy:
		if ( kthPositionCandidates.size() == 0 ) { 
			
			Utils.talk(toString(), "Cannot determine two nodes at diameter");
			
			targetVertices.addAll(randomSet.createRandomSet(numberOfHideLocations, new TreeSet<StringVertex>()));
			
		}
		
		int diameter = (int)FWSP.getDiameter();
		
		if ( minDistance != -1 ) diameter = minDistance;
	
		// Which position in the hide set we are currently concerned with
		int kth = 2;
		
		// Continue until we have complete nodes for the hideset
		while ( targetVertices.size() < numberOfHideLocations ) {
			
			// For each node in the graph
			outer:
			for ( StringVertex potentialNode : localGraph.vertexSet() ) {
			
				// For all combinations of previous kth nodes
				for ( ArrayList<StringVertex> candidates : Utils.combinations(new ArrayList<ArrayList<StringVertex>>(kthPositionCandidates.values())) ) {
					
					/* 
					 * Check if this node is at a given distance from all nodes
					 * in this potential permutation.
					 */
					for ( StringVertex candidate : candidates ) {
						
						DSP = new DijkstraShortestPath<StringVertex, StringEdge>(localGraph, potentialNode, candidate);
						
						if ( DSP.getPathEdgeList() == null || DSP.getPathEdgeList().size() == 0 || DSP.getPathLength() < diameter ) continue outer;
					
					}
				
					/* If we find a node that is greater than all existing candidates,
					 * and we already have enough k - 1 candidates, then we can finish
					 * and allocate the hide set
					 */
					if ( candidates.size() >= numberOfHideLocations - 1) { 
						
						targetVertices.addAll(candidates);
						
						targetVertices.add(potentialNode);
						
						break outer;
						
					}
					
				}
				
				/* Otherwise, add this as a potential node, if it is greater than all previous
				 * permutations and if we do not already have enough candidates for this position.
				 * 
				 * ~MDC: TODO: relax this constraint such that a potential node is added
				 * if it is greater than just ONE permutation. This will increase the likelihood
				 * of finding a result. However, must track which nodes are added for which
				 * permutations. In current mechanism, we do not need to do this as a node is
				 * only added if it is greater than all permutations. 
				 */
				
				int MAX_CANDIDATES = 5;
				
				if ( !kthPositionCandidates.containsKey(kth) || ( kthPositionCandidates.containsKey(kth) && kthPositionCandidates.get(kth).size() < MAX_CANDIDATES ) ) {
				
					Utils.add(kthPositionCandidates, kth, potentialNode, new ArrayList<StringVertex>(), true);
				
				}
				
			}
			
			// After all nodes have been checked, if any have been found which are suitable, increment kth
			if ( kthPositionCandidates.size() == (kth + 1) ) {
				
				kth++;
			
			// Otherwise, restart check for next suitable node with lower diameter
			} else {
				
				/* If we cannot lower the diameter any further (and thus cannot find suitable max distances),
				 * simply return the set that we have, plus random nodes for the remaining ones required.
				 */
				if ( diameter > 1 ) {
					
					diameter--;
					
					Utils.talk(toString(), "Reducing diameter to: " + diameter);
					
				} else {
					
					Utils.talk(toString(), "Cannot compute max distance, returning " + ( numberOfHideLocations - targetVertices.size() ) + " random.");
					
					targetVertices.addAll(randomSet.createRandomSet(numberOfHideLocations - targetVertices.size(), new TreeSet<StringVertex>(targetVertices)));
					
				}
				
			}
		
		}

		maxDistanceNodes = targetVertices;
		
		return targetVertices;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.HidingAgent#printGameStats()
	 */
	public String printGameStats() {
		
		return super.printGameStats() + ",GraphDiameter," +  Utils.graphDiameter(localGraph);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#printGameStats()
	 */
	@Override
	public String getStatus() {
		
		String stats = super.getStatus() + "\nGraphDiameter: " + Utils.graphDiameter(localGraph);
		
		if ( maxDistanceNodes.size() > 0 ) {
			
			stats += "\nDistance between hide locations:\n";
			
			DijkstraShortestPath<StringVertex, StringEdge> DSP;
			
			for ( StringVertex nodeAAtDistance : maxDistanceNodes ) {
				
				for ( StringVertex nodeBAtDistance : maxDistanceNodes ) {
				
					DSP = new DijkstraShortestPath<StringVertex, StringEdge>(localGraph, nodeAAtDistance, nodeBAtDistance);
					
					if ( DSP.getPathEdgeList() == null || DSP.getPathEdgeList().size() == 0 ) { 
						
						continue;
						
					}
					
					stats += ( nodeAAtDistance + " -> " + nodeBAtDistance + " (" + DSP.getPathLength() + " = " + DSP.getPathEdgeList().size() + " hops)\n" );
					
				}
				
			}
				
		}
		
		return stats;
		
	}
	
	/**
	 * Attempt to use the lead nodes of a Minimal Spanning Tree of the graph
	 * as an indicator of most 'distant' nodes.
	 * 
	 * @deprecated
	 * @param vertex
	 * @return
	 */
	public ArrayList<StringVertex> MSTDistance(StringVertex vertex) {
		
		ArrayList<StringVertex> targetVertices = new ArrayList<StringVertex>();
		
		DijkstraShortestPath<StringVertex, StringEdge> DSP = null;
		
		KruskalMinimumSpanningTree<StringVertex, StringEdge> KMST = new KruskalMinimumSpanningTree<StringVertex, StringEdge>(localGraph);
		
		HiddenObjectGraph<StringVertex, StringEdge> kruksalGraph = new HiddenObjectGraph<StringVertex, StringEdge>(StringEdge.class);
		
		for ( StringEdge edge : KMST.getMinimumSpanningTreeEdgeSet() ) {
		
			kruksalGraph.addVertex(edge.getSource());
			
			kruksalGraph.addVertex(edge.getTarget());
			
			kruksalGraph.addEdge(edge.getSource(), edge.getTarget(), edge);
			
		}
		
		ArrayList<StringVertex> MSTLeaves = new ArrayList<StringVertex>();
		
		for ( StringVertex kruksalVertex : kruksalGraph.vertexSet() ) {
			
			if ( kruksalGraph.degreeOf(kruksalVertex) == 1 ) {
				
				MSTLeaves.add(kruksalVertex);
				
			}
			
		}
		
		if ( MSTLeaves.size() >= numberOfHideLocations) {
			
			targetVertices.addAll(MSTLeaves.subList(0, numberOfHideLocations));
			
		} else {
			
			RandomSetMechanism randomSet = new RandomSetMechanism(graphController, numberOfHideLocations, responsibleAgent);
			
			targetVertices.addAll(MSTLeaves);
			
			targetVertices.addAll(randomSet.createRandomSet(numberOfHideLocations - MSTLeaves.size(), new TreeSet<StringVertex>(MSTLeaves)));
			
		}
		
		return targetVertices;
			
	}
	
	/**
	 * 
	 * Simply try all combinations of K nodes in turn
	 * to see if they exist at a suitably maximal distance from 
	 * each other to be considered well spread.
	 * 
	 * A portion of the entire vertices of the graph, or a 
	 * portion of the real diameter can be specified in order to
	 * reduce the complexity of the search.
	 * 
	 * @deprecated
	 * @param vertex
	 * @return
	 */
	public ArrayList<StringVertex> maxDistanceBruteForce() {
		
		ArrayList<StringVertex> targetVertices = new ArrayList<StringVertex>();
		
		DijkstraShortestPath<StringVertex, StringEdge> DSP = null;
		
	    double portionOfVertices = 0.9;
	    
	    double portionOfDiameter = 1.1;
	   
		ArrayList<StringVertex> graphVertices = graphController.vertexSet();
		
		Collections.shuffle(graphVertices);
		
		while ( targetVertices.size() != numberOfHideLocations ) {
			
			portionOfVertices = portionOfVertices + 0.1;
			
			portionOfDiameter = portionOfDiameter - 0.1;
			
			FloydWarshallShortestPaths<StringVertex, StringEdge> FWSP = new FloydWarshallShortestPaths<StringVertex, StringEdge>(localGraph);
			
			int minWeight = (int)(FWSP.getDiameter()* portionOfDiameter);
			
		    int[] indices = new int[numberOfHideLocations];
		    
		    Set<Integer> testSet = new HashSet<Integer>();
		    
		    outer:
			do {
		    
				Utils.advanceIndices( indices, numberOfHideLocations, (int)(graphVertices.size() * portionOfVertices) );
		        
				testSet.clear();
				
				for ( int index : indices ) {
					
					testSet.add(index);
					
				}
				
				if ( testSet.size() < indices.length ) continue outer;
				
				for ( int vertexA : indices ) {
					
					for ( int vertexB : indices ) {
					
						DSP = new DijkstraShortestPath<StringVertex, StringEdge>(localGraph, graphVertices.get(vertexA), graphVertices.get(vertexB));
					
						if ( graphVertices.get(vertexA) == graphVertices.get(vertexB) ) continue;
						
						if ( DSP.getPathEdgeList().size() < minWeight ) continue outer;
					
					}
					
				}
				
				for ( Integer index : indices ) {
					
					targetVertices.add(graphVertices.get(index));
					
				}
				
			} while( !Utils.allMaxed( indices, numberOfHideLocations, (int)(graphVertices.size() * portionOfVertices) ) );
			
		}
		
		return targetVertices;
	
	}
	
	/**
	 * Similar to @maxDistanceBruteForce, but attempts to brute force 
	 * maximum distance using recursion.
	 * 
	 * @deprecated
	 * @param vertexSet
	 * @param candidateNodes
	 * @param level
	 * @return
	 */
	private StringVertex[] maxDistanceBruteForceRecrusive(ArrayList<StringVertex> vertexSet, StringVertex[] candidateNodes, int level) {
		
	    if ( level == 0 ) { 
	        
	    	for ( int node = 1; node < candidateNodes.length; node ++ ) {
	    		
	    		DijkstraShortestPath<StringVertex, StringEdge> DSP = new DijkstraShortestPath<StringVertex, StringEdge>(localGraph, candidateNodes[node], candidateNodes[node-1]);
	    		
	    		if ( DSP.getPathEdgeList().size() != Utils.graphDiameter(localGraph)) return null;
	    	
	    	}
	    	
	    	return candidateNodes;
	    	
	    } else {
	    
	    	for ( StringVertex node : vertexSet ) {
	        	
	    		candidateNodes[level - 1] = node;
	    		
	    		maxDistanceBruteForceRecrusive(vertexSet, candidateNodes, level - 1 );
	        
	        }
	    	
	    	return candidateNodes;
	
	    }
	
	}

}