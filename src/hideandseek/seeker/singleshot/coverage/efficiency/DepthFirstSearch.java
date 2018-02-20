package hideandseek.seeker.singleshot.coverage.efficiency;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;
import hideandseek.seeker.SeekingAgent;

/**
 * @author Martin
 *
 */
public class DepthFirstSearch extends hideandseek.seeker.singleshot.coverage.DepthFirstSearch {

	/**
	 * @param graphController
	 */
	public DepthFirstSearch(GraphController<StringVertex, StringEdge> graphController) {
		
		super(graphController);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.SeekingAgent#searchCriteria()
	 */
	public boolean searchCriteria() {
		
		return uniquelyVisitedNodes().size() != graphController.vertexSet().size();
		
	}
	
}
