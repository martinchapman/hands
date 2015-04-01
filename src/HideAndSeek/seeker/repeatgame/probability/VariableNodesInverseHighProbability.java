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
 * If the parent strategy, which is tuned to exploiting hiders that attempt
 * to hide uniquely, is faced with such a hider, it may not always be advantageous
 * to use *all* this information, as it becomes detectable.
 * 
 * @author Martin
 *
 */
public class VariableNodesInverseHighProbability extends InverseHighProbability {
	
	/**
	 * @param graphController
	 */
	public VariableNodesInverseHighProbability(GraphController<StringVertex, StringEdge> graphController, int predictiveNodes) {
		
		super(graphController);
		
		this.predictiveNodes = predictiveNodes;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	public StringVertex startNode() {
		
		StringVertex newNode = super.startNode();
		
		this.likelyNodes = orderNodesByProximity(newNode, predictiveNodes, likelyNodes);
		
		return newNode;
		
	}
	
}
