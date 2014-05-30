package HideAndSeek.seeker.repeatgame;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;

import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.Seeker;
import Utility.BehaviourPrediction;
import Utility.Utils;

/**
 * Extends the standard High Probability Seeker adding the capacity
 * to specify the number of high probability nodes to use to direct
 * a search
 * 
 * @author Martin
 *
 */
public class VariableHighProbabilitySeeker extends HighProbabilitySeeker {
	
	public VariableHighProbabilitySeeker(
			HiddenObjectGraph<StringVertex, StringEdge> graph, int predictiveNodes) {
		super(graph);
		
		this.predictiveNodes = predictiveNodes;
		
	}
	
}
