package org.kclhi.hands.seeker.singleshot.random.efficiency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.seeker.SeekingAgent;

/**
 * @author Martin
 *
 */
public class SelfAvoidingRandomWalkGreedy extends org.kclhi.hands.seeker.singleshot.random.SelfAvoidingRandomWalkGreedy {

	/**
	 * @param graphController
	 */
	public SelfAvoidingRandomWalkGreedy(GraphController<StringVertex, StringEdge> graphController) {

		super(graphController);

	}
	
	/* (non-Javadoc)
	 * @see org.kclhi.hands.seeker.SeekingAgent#searchCriteria()
	 */
	public boolean searchCriteria() {
		
		return uniquelyVisitedNodes().size() != graphController.vertexSet().size();
		
	}

}
