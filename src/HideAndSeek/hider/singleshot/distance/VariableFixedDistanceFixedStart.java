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
public class VariableFixedDistanceFixedStart extends VariableFixedDistance {
	
	/**
	 * @param graph
	 */
	public VariableFixedDistanceFixedStart(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations, int minHideDistance) {
	
		super(graphController, numberOfHideLocations, minHideDistance);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#startNode()
	 */
	@Override
	public StringVertex startNode() {
		
		return firstNode();
		
	}
	
}
