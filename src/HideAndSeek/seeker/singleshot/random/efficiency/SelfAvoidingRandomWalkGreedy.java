package HideAndSeek.seeker.singleshot.random.efficiency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.SeekingAgent;

/**
 * @author Martin
 *
 */
public class SelfAvoidingRandomWalkGreedy extends HideAndSeek.seeker.singleshot.random.SelfAvoidingRandomWalkGreedy {

	/**
	 * @param graphController
	 */
	public SelfAvoidingRandomWalkGreedy(GraphController<StringVertex, StringEdge> graphController) {

		super(graphController);

	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.SeekingAgent#searchCriteria()
	 */
	public boolean searchCriteria() {
		
		return uniquelyVisitedNodes().size() != graphController.vertexSet().size();
		
	}

}