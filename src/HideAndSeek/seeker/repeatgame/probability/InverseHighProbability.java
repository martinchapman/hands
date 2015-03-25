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
 * @author Martin
 *
 */
public class InverseHighProbability extends HighProbability {

	/**
	 * @param graphController
	 */
	public InverseHighProbability(GraphController<StringVertex, StringEdge> graphController) {
		
		super(graphController);
		
		likelyNodes = new ArrayList<StringVertex>(graphController.vertexSet());
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	public StringVertex startNode() {
		
		StringVertex newNode = super.startNode();
		
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
