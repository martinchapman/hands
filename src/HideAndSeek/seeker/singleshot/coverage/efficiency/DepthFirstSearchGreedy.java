package HideAndSeek.seeker.singleshot.coverage.efficiency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;

/**
 * @author Martin
 *
 */
public class DepthFirstSearchGreedy extends HideAndSeek.seeker.singleshot.coverage.DepthFirstSearchGreedy {

	/**
	 * @param graphController
	 */
	public DepthFirstSearchGreedy(GraphController<StringVertex, StringEdge> graphController) {
		
		super(graphController);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.SeekingAgent#searchCriteria()
	 */
	public boolean searchCriteria() {
		
		return uniquelyVisitedNodes().size() != graphController.vertexSet().size();
		
	}
	
}
