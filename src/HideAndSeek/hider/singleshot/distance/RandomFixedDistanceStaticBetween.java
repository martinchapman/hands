package HideAndSeek.hider.singleshot.distance;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.HidingAgent;
import Utility.Utils;

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
