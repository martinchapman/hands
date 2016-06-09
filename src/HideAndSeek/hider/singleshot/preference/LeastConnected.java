package HideAndSeek.hider.singleshot.preference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import HideAndSeek.GraphTraverser;
import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import Utility.Utils;

/**
 * Attempts to hide nodes with the least possible connectivity.
 * 
 * Best is thus leaf nodes.
 * 
 * NB. Could consider including richer notions of degree centrality, 
 * such as betweenness centrality. Mechanisms to compute these
 * metrics are available in the library jar 'jgraph-sna'.
 * 
 * @author Martin
 *
 */
public class LeastConnected extends PreferenceHider {
	
	/**
	 * 
	 */
	private LinkedHashSet<StringVertex> leastConnectedNodes;
	
	/**
	 * @return
	 */
	public ArrayList<StringVertex> getMinimumConnectivityNodes() {
		
		return new ArrayList<StringVertex>(leastConnectedNodes);
	
	}

	/**
	 * The standard definition of an minimally connected node: 1,
	 * or leaf.
	 */
	protected static final int MIN_CONNECTIONS = 1;
	
	/**
	 * The *estimated* connectivity of the least connected nodes
	 * in the graph (may be greater than 1).
	 */
	protected int estimatedMinConnections = 1;
	
	/**
	 * 
	 */
	protected int estimatedMaxConnections = Integer.MIN_VALUE;

	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public LeastConnected(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations) {
		
		this(graphController, "", numberOfHideLocations, 1.0, null);
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 * @param graphPortion
	 */
	public LeastConnected(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations, double graphPortion) {
		
		this(graphController, "", numberOfHideLocations, graphPortion, null);
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public LeastConnected(GraphController <StringVertex, StringEdge> graphController, String name, int numberOfHideLocations) {
		
		this(graphController, name, numberOfHideLocations, 1.0, null);
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 * @param name
	 */
	public LeastConnected(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations, GraphTraverser responsibleAgent) {
		
		this(graphController, "", numberOfHideLocations, 1.0, responsibleAgent);
	
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 * @param graphPortion
	 */
	public LeastConnected(GraphController <StringVertex, StringEdge> graphController, String name, int numberOfHideLocations, double graphPortion) {
		
		this(graphController, name, numberOfHideLocations, graphPortion, null);
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 * @param graphPortion
	 * @param name
	 */
	public LeastConnected( GraphController<StringVertex, StringEdge> graphController, String name, int numberOfHideLocations, double graphPortion, GraphTraverser responsibleAgent) {
		
		super(graphController, name, numberOfHideLocations, graphPortion);
		
		triedNodes = new HashSet<StringVertex>();
		
		leastConnectedNodes = new LinkedHashSet<StringVertex>();
		
	}
	
	private int lastEdgeCount = -1;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.singleshot.preference.PreferenceHider#computeTargetNodes()
	 */
	@Override
	public LinkedHashSet<StringVertex> computeTargetNodes() {
		
		if ( localGraph.edgeSet().size() == lastEdgeCount ) return new LinkedHashSet<StringVertex>(leastConnectedNodes);
		
		TreeMap<Integer, ArrayList<StringVertex>> nodesToConnections = new TreeMap<Integer, ArrayList<StringVertex>>();
		
		for ( StringVertex potentialNode : localGraph.vertexSet() ) {
			
			Utils.add(nodesToConnections, localGraph.edgesOf(potentialNode).size(), potentialNode, new ArrayList<StringVertex>(), true);
		
		}
		
		HashSet<StringVertex> cumulativeConnectedNodes = new HashSet<StringVertex>();
		
		// Go through each
		for ( Map.Entry<Integer, ArrayList<StringVertex>> nodeToConnections : nodesToConnections.entrySet() ) {
			
			if ( nodeToConnections.getKey() > 0 ) {
				
				for ( StringVertex connectedNode : nodeToConnections.getValue() ) {
					
					if ( !hideLocations().contains(connectedNode) ) {
						
						cumulativeConnectedNodes.add(connectedNode);
					
					}
					
				}
				
			}
			
			/* 
			 * We only need to find enough minimally connected nodes for the remaining hide locations.
			 * Other minimally connected nodes may have been identified in 'real time'.
			 */
			if ( cumulativeConnectedNodes.size() >= ( numberOfHideLocations - graphController.numberOfHideLocations(responsibleAgent) ) ) {
				
				for ( StringVertex minimallyConnectedNode : cumulativeConnectedNodes ) {
						
					leastConnectedNodes.add(minimallyConnectedNode);
					
				}
				
				estimatedMinConnections = nodeToConnections.getKey();
				
				Utils.talk(toString(), "Selecting " + leastConnectedNodes + " of size " + nodeToConnections.getKey() + " as minimal.");
				
				break;
			
			}
			
		}
		
		// If we have not been able to find K nodes greater than 0 connectivity, return random.
		if ( leastConnectedNodes.size() == 0 ) {
			
			Utils.talk(toString(), "No suitable connectivity information, selecting randomly.");
			
			for ( StringVertex randomNode : randomSet.createRandomSet(numberOfHideLocations, new TreeSet<StringVertex>(hideLocations())) ) {
				
				leastConnectedNodes.add(randomNode);
				
			}
			
		}
		
		ArrayList<StringVertex> shuffledLeastConnectedNodes = new ArrayList<StringVertex>(leastConnectedNodes);
		
		Collections.shuffle(shuffledLeastConnectedNodes);
		
		leastConnectedNodes.clear();
		
		leastConnectedNodes.addAll(shuffledLeastConnectedNodes);
		
		lastEdgeCount = localGraph.edgeSet().size();
		
		return new LinkedHashSet<StringVertex>(leastConnectedNodes);
		
	}
	
	/**
	 * 
	 */
	public void endOfRound() {
		
		super.endOfRound();
		
		maxConnections = 1;
		
	}
	
	/**
	 * @param vertex
	 * @return
	 */
	public boolean hideHere(StringVertex vertex) {
		
		// Ensure the check for edge connectivity is only made when *at* a node
		if ( currentNode == vertex && getConnectedEdges(vertex).size() == MIN_CONNECTIONS ) { 
			
			//if ( !leastConnectedNodes.contains(vertex) ) leastConnectedNodes.add(vertex);
			
			// Will trigger super method to hide immediately
			addTargetVertex(vertex);
			
		} 
		
		/* 
		 * NOTE: Should LeastConnected do something if its target nodes have connectivity
		 * significantly higher than what has been estimated?
		 */
		return super.hideHere(vertex);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public StringVertex nextNode(StringVertex currentNode) {
		
		if ( graphController.degreeOf(currentNode) > estimatedMaxConnections ) estimatedMaxConnections = graphController.degreeOf(currentNode);
		
		return super.nextNode(currentNode);
	
	}

	/**
	 * @deprecated
	 */
	protected int maxConnections = 1;
	
	/**
	 * @deprecated
	 */
	private int minConnections = Integer.MAX_VALUE;
	
	/**
	 * @deprecated
	 */
	private HashSet<StringVertex> triedNodes;
	
	/**
	 * @deprecated
	 * @param vertex
	 * @return
	 */
	public boolean estimateLeastConnected(StringVertex vertex) {
		
		triedNodes.add(vertex);
		
		// If all nodes have been tried, and we still need more hide locations
		if (triedNodes.size() == (graphController.vertexSet().size() - graphController.numberOfHideLocations(responsibleAgent))) { 
			
			// Increase max connections if not enough exist at pre-set maximum (e.g. we set out 
			// to find connections of degree 1, but not enough (or none) of these exist).
			maxConnections = minConnections;
			
			triedNodes.clear();
			
			return true; 
				
		}
		
		if (getConnectedEdges(vertex).size() < minConnections) minConnections = graphController.degreeOf(vertex);
		
		if (getConnectedEdges(vertex).size() == MIN_CONNECTIONS) { 
			
			triedNodes.clear(); 
			
			if ( !leastConnectedNodes.contains(vertex) ) leastConnectedNodes.add(vertex);
			
			return true; 
			
		}
		
		return false;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#printGameStats()
	 */
	@Override
	public String printGameStats() {
		// TODO Auto-generated method stub
		return super.printGameStats() + 
				 ", AverageDegreeOfNodes, " + graphController.getTopologyProperties().averageDegreeOfNodes() +
				 ", DegreeOfLeastConnectedNodes, " + graphController.getTopologyProperties().degreeOfLeastConnectedNode() + 
				 ", NumberOfLeastConnectedNodes, " + graphController.getTopologyProperties().numberOfLeastConnectedNodes() + 
				 ", DegreeOfMostConnectedNodes, " + graphController.getTopologyProperties().degreeOfMostConnectedNode() + 
				 ", NumberOfMostConnectedNodes, " + graphController.getTopologyProperties().numberOfMostConnectedNodes() +
				 "";
	}

}
