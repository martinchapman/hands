package HideAndSeek.seeker.repeatgame.probability;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.SeekingAgent;
import Utility.BehaviourPrediction;
import Utility.Utils;

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
