package HideAndSeek.hider.singleshot.distance;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.HidingAgent;
import Utility.Utils;

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
