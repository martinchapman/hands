package HideAndSeek.hider.adaptive;

import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.repeatgame.VariableBiasHiderRandomPlacement;
import Utility.Utils;

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
			HiddenObjectGraph<StringVertex, StringEdge> graph,
			int numberOfHideLocations) {
	
		super(graph, numberOfHideLocations);
	
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.repeatgame.VariableBiasHider#hideHere(HideAndSeek.graph.StringVertex)
	 */
	protected boolean hideHere(StringVertex vertex) {
		
		if (Math.random() > 0.5) { return true; }
		return false;
		
	}
	
}
