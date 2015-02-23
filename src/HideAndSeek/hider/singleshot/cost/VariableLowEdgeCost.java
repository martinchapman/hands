package HideAndSeek.hider.singleshot.cost;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.Hider;

/**
 * A hider who's tendency to choose cheap edges over random edges
 * is manually set.
 * 
 * Relies on edges being of variable weight.
 * 
 * 
 * @author Martin
 */
public class VariableLowEdgeCost extends Hider {

	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 * @param bias
	 */
	public VariableLowEdgeCost(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations, double bias) {
		super(graphController, numberOfHideLocations);
		
		setBias(bias);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#hideHere(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected boolean hideHere(StringVertex vertex) {
		
		return true;
		
	}
	
	/**
	 * A percentage of the maximum value below which an edge weight must 
	 * fall under to be considered a cheap edge
	 */
	protected final static double CHEAPTHRESHOLD = 0.5;
	
	/**
	 * 
	 */
	protected double tendencyToBias = 0.0;
	
	/**
	 * 
	 */
	protected double tendencyToExplore = 1.0;
	
	/**
	 * @param bias
	 */
	public void setBias(double bias) {
		
		if (bias > 1.0) { bias = 1.0; }
		
		else if (bias < 0.0) { bias = 0.0; }
		
		tendencyToBias = bias;
		
		tendencyToExplore = 1.0 - bias;
		
	}
	
	/**
	 * 
	 */
	protected void incrementBias() {
		
		if (tendencyToBias < 1.0 && tendencyToExplore > 0.0) {
			
			tendencyToBias += 0.1;
			
			tendencyToExplore -= 0.1;
			
		}
		
	}
	
	/**
	 * 
	 */
	protected void decrementBias() {

		if (tendencyToBias > 0.0 && tendencyToExplore < 1.0) {
			
			tendencyToBias -= 0.1;
			
			tendencyToExplore += 0.1;
			
		}
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected StringVertex nextNode(StringVertex currentNode) {
		
		return connectedNode(currentNode);
		
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
		
		if (Math.random() < tendencyToBias) {
			
			Collections.sort(edges);
		
		} else {
			
			Collections.shuffle(edges);
			
		}
		
		return edges;
		
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
