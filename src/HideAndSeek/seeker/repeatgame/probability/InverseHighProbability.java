package HideAndSeek.seeker.repeatgame.probability;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.SeekerLocalGraph;
import Utility.BehaviourPrediction;
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
public class InverseHighProbability extends HighProbability {

	/**
	 * @param graphController
	 * @param name
	 */
	public InverseHighProbability(GraphController<StringVertex, StringEdge> graphController, String name) {
		
		super(graphController, name);
		
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
		
		return newNode;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.Seeker#endOfRound()
	 */
	@Override
	public void endOfRound() {
		
		super.endOfRound();
		
		this.likelyNodes = new ArrayList<StringVertex>(graphController.vertexSet());
		
		if (allHideLocations.size() >= graphController.vertexSet().size()) allHideLocations.clear();
		
		this.likelyNodes.removeAll(allHideLocations);
		
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