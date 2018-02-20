package hideandseek.seeker.repeatgame.probability;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;

import Utility.BehaviourPrediction;
import Utility.Utils;
import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;
import hideandseek.seeker.SeekingAgent;

/**
 * @author Martin
 *
 */
public class HighProbabilityStart extends HighProbability {

	public HighProbabilityStart(GraphController<StringVertex, StringEdge> graphController) {
		
		super(graphController);
	
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.repeatgame.probability.HighProbability#startNode()
	 */
	@Override
	public StringVertex startNode() {
		
		return likelyNodes.remove(0);
		
	}

}
