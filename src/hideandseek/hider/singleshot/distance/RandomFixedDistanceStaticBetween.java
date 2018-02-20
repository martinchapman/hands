package hideandseek.hider.singleshot.distance;

import Utility.Utils;
import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;
import hideandseek.hider.HidingAgent;

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
