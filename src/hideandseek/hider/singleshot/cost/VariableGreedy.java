package hideandseek.hider.singleshot.cost;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;
import hideandseek.hider.HidingAgent;

/**
 * A hider who's tendency to choose cheap edges over random edges
 * is manually set.
 * 
 * Relies on edges being of variable weight.
 * 
 * 
 * @author Martin
 */
public class VariableGreedy extends HidingAgent {

	/**
	 * @param graphController
	 * @param name
	 * @param numberOfHideLocations
	 * @param bias
	 */
	public VariableGreedy(
			GraphController <StringVertex, StringEdge> graphController, String name,
			int numberOfHideLocations, double bias) {
		super(graphController, numberOfHideLocations);
		
		setBias(bias);
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 * @param bias
	 */
	public VariableGreedy(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations, double bias) {
		this(graphController, "", numberOfHideLocations, bias);
		
	}

	

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#hideHere(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public boolean hideHere(StringVertex vertex) {
		
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
	public StringVertex nextNode(StringVertex currentNode) {
		
		return connectedNode(currentNode);
		
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

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#getConnectedEdges(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public List<StringEdge> getConnectedEdges(StringVertex currentNode) {
		
		ArrayList<StringEdge> edges = new ArrayList<StringEdge>(super.getConnectedEdges(currentNode));
		
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
		// TODO Auto-generated method stub
		return super.printGameStats(); //+ ",Payoff," + graphController.requestAverageHiderPayoff(this);
	}

}
