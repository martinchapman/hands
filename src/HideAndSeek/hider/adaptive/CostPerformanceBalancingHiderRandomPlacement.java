package HideAndSeek.hider.adaptive;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;

/**
 * Same as namesake, although hides according to random placement
 * 
 * @author Martin
 *
 */
public class CostPerformanceBalancingHiderRandomPlacement extends CostPerformanceBalancingHider {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 */
	public CostPerformanceBalancingHiderRandomPlacement(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations) {
	
		super(graphController, numberOfHideLocations);
	
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.repeatgame.VariableBiasHider#hideHere(HideAndSeek.graph.StringVertex)
	 */
	protected boolean hideHere(StringVertex vertex) {
		
		if (Math.random() > 0.5) { return true; }
		return false;
		
	}
	
}
