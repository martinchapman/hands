package hideandseek.seeker.singleshot.random.efficiency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;
import hideandseek.seeker.SeekingAgent;

/**
 * @author Martin
 *
 */
public class SelfAvoidingRandomWalkGreedy extends hideandseek.seeker.singleshot.random.SelfAvoidingRandomWalkGreedy {

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