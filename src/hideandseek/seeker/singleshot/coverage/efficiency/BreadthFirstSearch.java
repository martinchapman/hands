package hideandseek.seeker.singleshot.coverage.efficiency;

import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;

/**
 * @author Martin
 */
public class BreadthFirstSearch extends hideandseek.seeker.singleshot.coverage.BreadthFirstSearch {

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