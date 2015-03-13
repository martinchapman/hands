package HideAndSeek.hider.adaptive;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.AdaptiveHiderStrategy;
import HideAndSeek.hider.singleshot.preference.LeastConnected;

/**
 * 
 * Attempts to work out how many minimum connectivity nodes are in a graph, and thus 
 * if it is worth attempting to hide in them.
 * 
 * @author Martin
 *
 */
public class AdaptiveLeastConnected extends LeastConnected implements AdaptiveHiderStrategy {

	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public AdaptiveLeastConnected(GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations) {
		
		super(graphController, numberOfHideLocations);

	}

	@Override
	public double abilityToPerform() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
