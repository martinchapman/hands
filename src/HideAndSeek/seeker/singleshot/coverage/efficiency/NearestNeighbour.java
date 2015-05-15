package HideAndSeek.seeker.singleshot.coverage.efficiency;

import java.util.ArrayList;

import org.jgrapht.alg.DijkstraShortestPath;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.SeekingAgent;

/**
 * @author Martin
 *
 */
public class NearestNeighbour extends HideAndSeek.seeker.singleshot.coverage.BacktrackGreedy {

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