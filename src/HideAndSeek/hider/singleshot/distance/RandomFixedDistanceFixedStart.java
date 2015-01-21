package HideAndSeek.hider.singleshot.distance;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.Hider;
import Utility.Utils;

/**
 * 
 * Employ a random (0 to N) fixed distance between each hide location,
 * starting at a fixed location.
 * 
 * @author Martin
 *
 */
public class RandomFixedDistanceFixedStart extends RandomFixedDistance implements Runnable {
	
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
	protected StringVertex startNode() {
		
		StringVertex[] vertices = new StringVertex[graphController.vertexSet().size()];
		
		return graphController.vertexSet().toArray(vertices)[0];
		
	}
	
}
