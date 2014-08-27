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
public class VariableFixedDistanceFixedStart extends VariableFixedDistance implements Runnable {
	
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
	protected StringVertex startNode() {
		
		StringVertex[] vertices = new StringVertex[graphController.vertexSet().size()];
		
		return graphController.vertexSet().toArray(vertices)[0];
		
	}
	
}
