package HideAndSeek.hider.singleshot.preference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.TreeMap;

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
	 * The imagined number of max connections that a node
	 * can have in order to be considered to have least connectivity.
	 * (1 = leaf).
	 */
	protected static final int MAX_CONNECTIONS = 1;
	
	/**
	 * @param graph
	 * @param numberOfHideLocations
	 */
	public LeastConnected(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations) {
		
		this(graphController, numberOfHideLocations, 1.0);
	
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public LeastConnected( GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations, double graphPortion ) {
		
		super(graphController, numberOfHideLocations, graphPortion);
		
		triedNodes = new HashSet<StringVertex>();
		
		leastConnectedNodes = new LinkedHashSet<StringVertex>();
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.singleshot.preference.PreferenceHider#computeTargetNodes()
	 */
	@Override
	public LinkedHashSet<StringVertex> computeTargetNodes() {
		
		TreeMap<Integer, ArrayList<StringVertex>> nodesToConnections = new TreeMap<Integer, ArrayList<StringVertex>>();
		
		for ( StringVertex potentialNode : localGraph.vertexSet() ) {
			
			Utils.add(nodesToConnections, localGraph.edgesOf(potentialNode).size(), potentialNode, new ArrayList<StringVertex>(), true);
		
		}
		
		for ( Map.Entry<Integer, ArrayList<StringVertex>> nodeToConnections : nodesToConnections.entrySet() ) {
			
			if ( nodeToConnections.getValue().size() >= numberOfHideLocations ) {
				
				leastConnectedNodes.clear();
				
				for ( StringVertex minimallyConnectedNode : nodeToConnections.getValue() ) {
					
					leastConnectedNodes.add(minimallyConnectedNode);
					
				}
				
			}
			
		}

		/* 
		 * If we haven't found a sufficient number of connected edges at our estimated maximum,
		 * relaxed this constraint by increasing the maximum, and try again.
		 */
		
		return leastConnectedNodes;
		
	}
	
	/**
	 * 
	 */
	public void endOfRound() {
		
		super.endOfRound();
		
		leastConnectedNodes.clear();
		
		maxConnections = 1;
		
	}
		
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public StringVertex nextNode(StringVertex currentNode) {
		
		if ( getConnectedEdges(currentNode).size() == MAX_CONNECTIONS && !leastConnectedNodes.contains(currentNode)) leastConnectedNodes.add(currentNode);
		
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
		
		if (getConnectedEdges(vertex).size() == MAX_CONNECTIONS) { 
			
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
