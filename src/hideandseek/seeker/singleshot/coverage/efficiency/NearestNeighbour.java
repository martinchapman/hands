package hideandseek.seeker.singleshot.coverage.efficiency;

import java.util.ArrayList;

import org.jgrapht.alg.DijkstraShortestPath;

import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;
import hideandseek.seeker.SeekingAgent;

/**
 * @author Martin
 *
 */
public class NearestNeighbour extends hideandseek.seeker.singleshot.coverage.BacktrackGreedy {

	/**
	 * @param graphController
	 */
	public NearestNeighbour(GraphController<StringVertex, StringEdge> graphController) {
		
		super(graphController);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.SeekingAgent#searchCriteria()
	 */
	public boolean searchCriteria() {
		
		return uniquelyVisitedNodes().size() != graphController.vertexSet().size();
		
	}

}