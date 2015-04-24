package HideAndSeek.hider.repeatgame.bias;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.singleshot.cost.VariableGreedy;
import Utility.Utils;

/**
 * A hider who's tendency to choose pre-explored edges (cheaper)
 * over unexplored edges is manually set.
 * 
 * Relies on the % cost reduction for exploring an edge to be greater than 0.
 * 
 * NB. Best run with FIXED edge costs, as decremented edge cost is used
 * as a metric for how explored a connection is. With variables costs, this may
 * distort this measure.
 * 
 * @author Martin
 */
public class VariableBias extends VariableGreedy {

	/**
	 * Hider in which the tendency to take pre-traversed edges
	 * can be set.
	 * 
	 * Hides in first K locations.
	 * 
	 * 
	 * @param graph
	 * @param numberOfHideLocations
	 * @param bias
	 */
	public VariableBias(
			GraphController <StringVertex, StringEdge> graphController, String name,
			int numberOfHideLocations, double bias) {
		super(graphController, name, numberOfHideLocations, bias);
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 * @param bias
	 */
	public VariableBias(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations, double bias) {
		this(graphController, "", numberOfHideLocations, bias);
		
	}

	/*
	 *  Percentage of original cost that an edge must reach in order
	 *  to be considered 'well traversed'
	 */
	protected final static double WELLTRAVERSEDPERCENTAGE = 0.5;
	
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public StringVertex nextNode(StringVertex currentNode) {
		
		List<StringEdge> connectedEdges = getConnectedEdges(currentNode);
		
		Map<StringEdge, Double> biasEdgesToCost = new HashMap<StringEdge, Double>();
		
		Map<StringEdge, Double> explorativeEdgesToCost = new HashMap<StringEdge, Double>();
		
		for ( StringEdge edge : connectedEdges ) {
			
			// If no edge traversal decrement has been set 
			if ( graphController.getEdgeTraversalDecrement() == 1.0 ) {
				
				// Simply return a random connected node
				return connectedNode(currentNode);
					
			} else {
			
				if ( graphController.traverserEdgeCost(this, edge.getSource(), edge.getTarget()) < ( graphController.getEdgeWeight(edge) *  WELLTRAVERSEDPERCENTAGE ) ) {
					
					// Better to be a set of potential bias edges (as opposed to just one), as may have been traversed before.
					biasEdgesToCost.put(edge, graphController.traverserEdgeCost(this, edge.getSource(), edge.getTarget()));
					
				} else {
					
					explorativeEdgesToCost.put(edge, graphController.traverserEdgeCost(this, edge.getSource(), edge.getTarget()));
					
				}
				
			}
		
		}
		
		
		explorativeEdgesToCost = Utils.sortByValue(explorativeEdgesToCost, true);
		
		biasEdgesToCost = Utils.sortByValue(biasEdgesToCost, true);
		
		// If there is no information on the proportion of biased edges, or no edge traversal decrement 
		// (i.e. no info on explorative) select node at random
		if ( biasEdgesToCost.size() == 0 || graphController.getEdgeTraversalDecrement() == 1.0) {
			
			return connectedNode(currentNode);
		
		// Otherwise proceed to probabilistic selection
		} else {
			
			if (Math.random() < tendencyToBias) {
				
				// Gets *least* weighted unvisited (most bias) edge, by being sorted.
				return exploreEdges(currentNode, new ArrayList<StringEdge>(biasEdgesToCost.keySet()));
				
			} else {
				
				// If there are no immediately explorative edges (i.e. all fall below the threshold), then the most explorative 
				// edges (edge) are those with the highest cost  
				if ( explorativeEdgesToCost.size() == 0 ) {
					
					ArrayList<StringEdge> biasEdges = new ArrayList<StringEdge>(biasEdgesToCost.keySet());
					
					int firstIndexOfMax = new ArrayList<Double>(biasEdgesToCost.values()).indexOf(Collections.max(biasEdgesToCost.values()));
					
					int lastIndexOfMax = new ArrayList<Double>(biasEdgesToCost.values()).lastIndexOf(Collections.max(biasEdgesToCost.values()));
					
					ArrayList<StringEdge> leastBias;
					
					if (firstIndexOfMax == lastIndexOfMax) { // Only one max value
						
						leastBias = new ArrayList<StringEdge>();
						
						leastBias.add(biasEdges.get(firstIndexOfMax));
						
					} else {
					
						// Get first instance of max value to last instance of last value, so all possible max explorative edges.
						leastBias = new ArrayList<StringEdge>(biasEdges.subList(firstIndexOfMax, lastIndexOfMax + 1));
					
					}
					
					return exploreEdges(currentNode, leastBias);
				
				// Otherwise simply return explorative edges in reverse order
				} else {
					
					ArrayList<StringEdge> explorativeEdges = new ArrayList<StringEdge>(explorativeEdgesToCost.keySet());
					
					// Get *most* weighted unvisited (most explorative) edge
					Collections.reverse(explorativeEdges);
					
					return exploreEdges(currentNode, explorativeEdges);
				
				}
				
			}
			
		}
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#getConnectedEdge(HideAndSeek.graph.StringVertex, java.util.List)
	 */
	@Override
	public StringEdge getConnectedEdge(StringVertex currentNode, List<StringEdge> connectedEdges) {
		
		for (StringEdge edge : connectedEdges ) {

			if ( uniquelyVisitedNodes().contains(edgeToTarget(edge, currentNode)) ) continue;
			
			return edge;
			
		}
		
		return connectedEdges.get((int)(Math.random() * connectedEdges.size()));
		
	}

	/**
	 * @param currentNode
	 * @param biasEdges
	 * @return
	 */
	private StringVertex exploreEdges(StringVertex currentNode, ArrayList<StringEdge> edges) {
		
		StringVertex target;
		
		for ( StringEdge edge : edges ) {
			
			if ( !uniquelyVisitedNodes().contains(edgeToTarget(edge, currentNode)) ) {
				
				return edgeToTarget(edge, currentNode);
				
			}
			
		};
		
		return connectedNode(currentNode);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	public StringVertex startNode() {

		return randomNode();
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#toString()
	 */
	public String toString() {
		
		return super.toString(); //+ " " + tendencyToBias;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#printRoundStats()
	 */
	@Override
	public String printRoundStats() {
		
		return super.printRoundStats(); //+ "," + tendencyToBias;
	
	}


	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#printGameStats()
	 */
	@Override
	public String printGameStats() {
		
		return super.printGameStats(); //+ ",Payoff," + graphController.requestAverageHiderPayoff(this);
	
	}

}
