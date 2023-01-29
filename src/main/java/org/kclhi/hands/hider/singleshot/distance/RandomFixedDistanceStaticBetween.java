package org.kclhi.hands.hider.singleshot.distance;

import org.kclhi.hands.utility.Utils;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.hider.HidingAgent;

/**
 *
 * 
 *
 * @author Martin
 *
 */
public class RandomFixedDistanceStaticBetween extends RandomFixedDistance implements Runnable {

	/**
	 * @param graph
	 */
	public RandomFixedDistanceStaticBetween(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations) {

		super(graphController, numberOfHideLocations);

	}

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#startNode()
	 */
	@Override
	public StringVertex startNode() {

		return currentNode();

	}

}
