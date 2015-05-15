package HideAndSeek.seeker.singleshot.coverage.efficiency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.SeekingAgent;
import Utility.Utils;

/**
 * @author Martin
 *
 */
public class BreadthFirstSearchGreedy extends HideAndSeek.seeker.singleshot.coverage.BreadthFirstSearchGreedy {

	/**
	 * @param graphController
	 */
	public BreadthFirstSearchGreedy(GraphController<StringVertex, StringEdge> graphController) {
		
		super(graphController);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.SeekingAgent#searchCriteria()
	 */
	public boolean searchCriteria() {
		
		return uniquelyVisitedNodes().size() != graphController.vertexSet().size();
		
	}
	
}


