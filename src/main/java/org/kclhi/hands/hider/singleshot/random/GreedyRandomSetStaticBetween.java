package org.kclhi.hands.hider.singleshot.random;

import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
 * Same as parent, except does not move between rounds.
 * 
 * @author Martin
 *
 */
public class GreedyRandomSetStaticBetween extends GreedyRandomSet {

	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public GreedyRandomSetStaticBetween(GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations) {
		
		super(graphController, numberOfHideLocations);
		
	}
  
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.singleshot.random.RandomSet#startNode()
	 */
	public StringVertex startNode() {

		return currentNode();
		
	}

}
