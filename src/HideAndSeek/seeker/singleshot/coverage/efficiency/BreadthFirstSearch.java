package HideAndSeek.seeker.singleshot.coverage.efficiency;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;

/**
 * @author Martin
 */
public class BreadthFirstSearch extends HideAndSeek.seeker.singleshot.coverage.BreadthFirstSearch {

	/**
	 * @param graph
	 */
	public BreadthFirstSearch(GraphController <StringVertex, StringEdge> graphController) {

		super(graphController);

	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.SeekingAgent#searchCriteria()
	 */
	public boolean searchCriteria() {
		
		return uniquelyVisitedNodes().size() != graphController.vertexSet().size();
		
	}
	
}