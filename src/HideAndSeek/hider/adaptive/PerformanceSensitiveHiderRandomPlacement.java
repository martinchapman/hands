package HideAndSeek.hider.adaptive;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;

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
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations) {
	
		super(graphController, numberOfHideLocations);
	
	}

	protected boolean hideHere(StringVertex vertex) {
		
		if (Math.random() > 0.5) { return true; }
		return false;
		
	}
	
}
