package org.kclhi.hands.hider.singleshot.distance;

import org.kclhi.hands.utility.Utils;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.hider.HidingAgent;

/**
 * 
 * Employ a random (0 to N) fixed distance between each hide location,
 * starting at a fixed location.
 * 
 * @author Martin
 *
 */
public class RandomFixedDistanceFixedStart extends RandomFixedDistance {
	
	/**
	 * @param graph
	 */
	public RandomFixedDistanceFixedStart(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations) {
	
		super(graphController, numberOfHideLocations);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#startNode()
	 */
	@Override
	public StringVertex startNode() {
		
		return firstNode();
		
	}
	
}
