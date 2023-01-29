package org.kclhi.hands.hider.singleshot.preference;

import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
 * Attempts to hide nodes with a maximum possible distance 
 * between them.
 * 
 * @author Martin
 *
 */
public class MaxDistanceStaticBetween extends MaxDistance {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 */
	public MaxDistanceStaticBetween(GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations) {
		super(graphController, numberOfHideLocations);
	}
    
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	public StringVertex startNode() {
		
		return currentNode();
		
	}


}
