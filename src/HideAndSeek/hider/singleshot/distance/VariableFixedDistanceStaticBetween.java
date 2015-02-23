package HideAndSeek.hider.singleshot.distance;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;

/**
 * 
 * 
 * @author Martin
 *
 */
public class VariableFixedDistanceStaticBetween extends VariableFixedDistance implements Runnable {
	
	/**
	 * @param graph
	 */
	public VariableFixedDistanceStaticBetween(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations, int minHideDistance) {
	
		super(graphController, numberOfHideLocations, minHideDistance);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.singleshot.distance.RandomFixedDistance#startNode()
	 */
	public StringVertex startNode() {
		
		return getCurrentNode();
		
	}
	
}
