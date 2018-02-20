package hideandseek.seeker.singleshot.coverage.efficiency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;

import Utility.Utils;
import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;
import hideandseek.seeker.SeekingAgent;

/**
 * @author Martin
 *
 */
public class BreadthFirstSearchGreedy extends hideandseek.seeker.singleshot.coverage.BreadthFirstSearchGreedy {

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


