package HideAndSeek.hider.singleshot;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;

/**
 * 
 * 
 * @author Martin
 *
 */
public class VariableFixedDistance extends RandomFixedDistance implements Runnable {
	
	/**
	 * @param graph
	 */
	public VariableFixedDistance(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations, int minHideDistance) {
	
		super(graphController, numberOfHideLocations);
		
		this.minHideDistance = minHideDistance;

	}
	
}
