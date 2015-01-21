package HideAndSeek.hider.adaptive;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.singleshot.preference.LeastConnected;

/**
 * 
 * Attempts to work out how many minimum connectivity nodes are in a graph, and thus 
 * if it is worth attempting to hide in them.
 * 
 * @author Martin
 *
 */
public class AdaptiveMinimumConnectivity extends LeastConnected {

	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public AdaptiveMinimumConnectivity(GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations) {
		
		super(graphController, numberOfHideLocations);

	}
	
}
