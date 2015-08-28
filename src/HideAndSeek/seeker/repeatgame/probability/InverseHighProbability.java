package HideAndSeek.seeker.repeatgame.probability;

import java.util.ArrayList;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import Utility.Utils;

/**
 * Does not explore potential hide locations based upon frequency,
 * but instead tracks those nodes which have *not yet been chosen*
 * by a hider as hide locations, and heads to those first.
 * 
 * Again, a form of Bayesian approach to seeking, as opposed to
 * simply relying on frequency.
 * 
 * @author Martin
 *
 */
public class InverseHighProbability extends VariableNodesHighProbability {

	/**
	 * 
	 */
	private int likelyNodesSize = 0;
	
	/**
	 * 
	 */
	private static boolean LIKELY_NODES_SIZE = false;
	
	/**
	 * @param graphController
	 * @param name
	 */
	public InverseHighProbability(GraphController<StringVertex, StringEdge> graphController, String name) {
		
		//graphController.numberOfHideLocations()
		 
		super(graphController, name, Integer.MAX_VALUE, false);
		
		likelyNodes = new ArrayList<StringVertex>(graphController.vertexSet());
		
	}
	
	/**
	 * @param graphController
	 */
	public InverseHighProbability(GraphController<StringVertex, StringEdge> graphController) {
		
		this(graphController, "");
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	public StringVertex startNode() {
		
		StringVertex newNode = super.startNode();
		
		/* 
		 * At the start of each round, order the likely nodes according to their 
		 * proximity to the start node. 
		 */
		this.likelyNodes = orderNodesByProximity(newNode, Integer.MAX_VALUE, likelyNodes);
		
		//likelyNodes.addAll(uniqueHideLocations());
		
		likelyNodesSize = likelyNodes.size();
		
		if ( likelyNodes.size() >= predictiveNodes ) likelyNodes = new ArrayList<StringVertex>(likelyNodes.subList(0, predictiveNodes));
		
		Utils.talk(toString(), "Likely nodes (" + likelyNodes.size() + "): " + likelyNodes);
		
		return newNode;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.repeatgame.probability.HighProbability#printRoundStats()
	 */
	@Override
	public String printRoundStats() {
	
		String roundStats = "";
		
		if ( LIKELY_NODES_SIZE ) roundStats += ",likelyNodesSize," + likelyNodesSize + ",";
		
		return super.printRoundStats() + roundStats;
	
	}
	

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.Seeker#endOfRound()
	 */
	@Override
	public void endOfRound() {
		
		super.endOfRound();
		
		likelyNodes.clear();
		
		//this.likelyNodes = new ArrayList<StringVertex>(graphController.vertexSet());
		
		if (allHideLocations().size() >= ( graphController.vertexSet().size() )) uniqueHideLocations().clear();
		
		for ( StringVertex vertex : graphController.vertexSet() ) {
			
			// If this vertex has not been visited before
			if ( !uniqueHideLocations().contains(vertex) ) {
				
				this.likelyNodes.add(vertex);
				
			}
		
		}
		
		// ~MDC Operation is slow.
		//this.likelyNodes.removeAll(uniqueHideLocations());
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.Seeker#endOfGame()
	 */
	@Override
	public void endOfGame() {
		
		super.endOfGame();
		
		likelyNodes.clear();
		
		likelyNodes = new ArrayList<StringVertex>(graphController.vertexSet());
		
	}
	
}
