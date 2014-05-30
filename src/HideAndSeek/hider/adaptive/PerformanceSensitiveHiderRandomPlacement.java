package HideAndSeek.hider.adaptive;

import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.repeatgame.VariableBiasHider;
import Utility.Utils;

/**
 * Same as parent, except hides according to random proportion.
 * 
 * @author Martin
 *
 */
public class PerformanceSensitiveHiderRandomPlacement extends PerformanceSensitiveHider {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 */
	public PerformanceSensitiveHiderRandomPlacement(
			HiddenObjectGraph<StringVertex, StringEdge> graph,
			int numberOfHideLocations) {
	
		super(graph, numberOfHideLocations);
	
	}

	protected boolean hideHere(StringVertex vertex) {
		
		if (Math.random() > 0.5) { return true; }
		return false;
		
	}
	
}
