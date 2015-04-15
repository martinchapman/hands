package HideAndSeek.seeker.singleshot.random.efficiency;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.SeekingAgent;

/**
 * @author Martin
 *
 */
public class SelfAvoidingRandomWalk extends HideAndSeek.seeker.singleshot.random.SelfAvoidingRandomWalk {

	/**
	 * @param graphController
	 */
	public SelfAvoidingRandomWalk(GraphController<StringVertex, StringEdge> graphController) {
		
		super(graphController);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.SeekingAgent#searchCriteria()
	 */
	public boolean searchCriteria() {
		
		return uniquelyVisitedNodes().size() != graphController.vertexSet().size();
		
	}

}
