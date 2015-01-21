package HideAndSeek.hider.singleshot.preference;

import java.util.ArrayList;
import java.util.HashSet;

import org.jgrapht.alg.DijkstraShortestPath;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.HiderLocalGraph;

/**
 * Attempts to hide nodes with the least possible connectivity.
 * 
 * Best is thus leaf nodes.
 * 
 * @author Martin
 *
 */
public class LeastConnected extends HiderLocalGraph {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 */
	public LeastConnected(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations) {
		
		super(graphController, numberOfHideLocations);
		
		triedNodes = new HashSet<StringVertex>();
		
		minimumConnectivityNodes = new ArrayList<StringVertex>();
		
		currentPath = new ArrayList<StringEdge>();
		
	}

	/**
	 * 
	 */
	private int minConnections = Integer.MAX_VALUE;
	
	/**
	 * 
	 */
	private ArrayList<StringVertex> minimumConnectivityNodes;
	
	/**
	 * 
	 */
	private static int MAXCONNECTIONS = 1;
	
	/**
	 * 
	 */
	private HashSet<StringVertex> triedNodes;
	
	/**
	 * 
	 */
	ArrayList<StringEdge> currentPath;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#hideHere(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected boolean hideHere(StringVertex vertex) {
		
		triedNodes.add(vertex);
		
		// If all nodes have been tried, cannot continue, so return true.
		if (triedNodes.size() == (graphController.vertexSet().size() - hideLocations.size())) { 
			
			// Increase max connections if none exist at pre-set maximum (e.g. we set out 
			// to find connections of degree 1, but none of these exist).
			MAXCONNECTIONS = minConnections;
			
			triedNodes.clear();
			
			return true; 
				
		}
		
		if (graphController.edgesOf(vertex).size() < minConnections) minConnections = graphController.degreeOf(vertex);
		
		if (graphController.edgesOf(vertex).size() == MAXCONNECTIONS) { 
			
			triedNodes.clear(); 
			
			minimumConnectivityNodes.add(vertex);
			
			return true; 
			
		}
		
		return false;
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected StringVertex nextNode(StringVertex currentNode) {
		
		// If we're on a path to a node in the random set, follow this first
		if ( currentPath.size() > 0 ) {
			
			return edgeToTarget(currentPath.remove(0), currentNode);
			
		}
		
		// Otherwise, try and calculate a path to the next node in the set
		if ( minimumConnectivityNodes.size() > 0 && !uniquelyVisitedNodes().contains(minimumConnectivityNodes.get(0)) && localGraph.containsVertex( minimumConnectivityNodes.get(0) ) ) {
			
			DijkstraShortestPath<StringVertex, StringEdge> dsp = new DijkstraShortestPath<StringVertex, StringEdge>(localGraph, currentNode, minimumConnectivityNodes.get(0));
	    	
			// If no path available, return random connected node
			if (dsp.getPathEdgeList() == null || dsp.getPathEdgeList().size() == 0) return connectedNode(currentNode);
			
			currentPath = new ArrayList<StringEdge>(dsp.getPathEdgeList());
			
			return edgeToTarget(currentPath.remove(0), currentNode);
			
		}
				
		return connectedNode(currentNode);
		
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

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	protected StringVertex startNode() {
	
		return randomNode();
		
	}


}
