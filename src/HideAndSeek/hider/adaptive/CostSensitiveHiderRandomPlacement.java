package HideAndSeek.hider.adaptive;

import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.repeatgame.VariableBiasHider;
import Utility.Utils;

/**
 * H&S is a multiple objective optimisation problem for the hider,
 * as they must balance bias edges (cheaper but yielding less inconspicuous
 * locations) with explorative edges (more expensive, but yielding more
 * inconspicuous locations). Reduce cost; increase anonymity. 
 * 
 * This hider only focuses on its own cost; increases bias if cost is too
 * high.
 * 
 * @author Martin
 *
 */
public class CostSensitiveHiderRandomPlacement extends CostSensitiveHider {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 */
	public CostSensitiveHiderRandomPlacement(
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
