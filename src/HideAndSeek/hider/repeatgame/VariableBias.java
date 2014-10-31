package HideAndSeek.hider.repeatgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.singleshot.VariableLowEdgeCost;

/**
 * A hider who's tendency to choose pre-explored edges (cheaper)
 * over unexplored edges is manually set.
 * 
 * Relies on the % cost reduction for exploring an edge to be greater than 0.
 * 
 * @author Martin
 */
public class VariableBias extends VariableLowEdgeCost {

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
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations, double bias) {
		super(graphController, numberOfHideLocations, bias);
		
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
	protected StringVertex nextNode(StringVertex currentNode) {
		
		List<StringEdge> connectedEdges = getConnectedEdges(currentNode);
		
		ArrayList<StringEdge> biasEdges = new ArrayList<StringEdge>();
		
		ArrayList<StringEdge> explorativeEdges = new ArrayList<StringEdge>();
		
		for ( StringEdge edge : connectedEdges ) {
			
			// If no edge traversal decrement has been set 
			if ( graphController.getEdgeTraverselDecrement() == 1.0 ) {
				
				// Simply return a random connected node
				return connectedNode(currentNode);
					
			} else {
			
				if ( graphController.traverserEdgeCost(this, edge.getSource(), edge.getTarget()) < ( graphController.getEdgeWeight(edge) *  WELLTRAVERSEDPERCENTAGE ) ) {
					
					biasEdges.add(edge);
					
				} else {
					
					explorativeEdges.add(edge);
					
				}
				
			}
		
		}
		
		// If there is no information on the proportion of biased edges, select node at random
		if ( biasEdges.size() == 0 ) {
			
			return connectedNode(currentNode);
			
		} else {
			
			if (Math.random() < tendencyToBias) {
				
				// Get *least* weighted unvisited (most bias) edge
				Collections.sort(biasEdges);
				
				return exploreEdges(currentNode, biasEdges);
				
			} else {
				
				// If there is no edge traversal decrement, ordering explorative nodes
				// is also exploitable as strategy will always select most expensive ones
				if ( graphController.getEdgeTraverselDecrement() == 1.0 ) {
					
					Collections.shuffle(explorativeEdges);
					
					return exploreEdges(currentNode, explorativeEdges);
					
				} else {
					
					// Get *most* weighted unvisited (most explorative) edge
					Collections.sort(explorativeEdges);
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
	protected StringEdge getConnectedEdge(StringVertex currentNode, List<StringEdge> connectedEdges) {
		
		for (StringEdge edge : connectedEdges ) {

			if ( uniquelyVisitedNodes().contains(edgeToTarget(edge, currentNode)) ) continue;
			
			return edge;
			
		}
		
		return connectedEdges.get((int)(Math.random() * connectedEdges.size()));
		
	}

	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#getConnectedEdges(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected List<StringEdge> getConnectedEdges(StringVertex currentNode) {
		
		ArrayList<StringEdge> edges = new ArrayList<StringEdge>(graphController.edgesOf(currentNode));
		
		Collections.sort(edges);
		
		return edges;
		
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
	protected StringVertex startNode() {

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
		// TODO Auto-generated method stub
		return super.printGameStats(); //+ ",Score," + graphController.requestAverageHiderScore(this);
	}

}
