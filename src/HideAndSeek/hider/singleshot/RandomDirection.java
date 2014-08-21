package HideAndSeek.hider.singleshot;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.Hider;

/**
 * 
 * Weaker version of truly random hider, which moves randomly, but 
 * hides in the first K nodes moved to.
 * 
 * @author Martin
 *
 */
public class RandomDirection extends Random {

	public RandomDirection(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations) {
		super(graphController, numberOfHideLocations);
		
	}

	@Override
	protected boolean hideHere(StringVertex vertex) {
		
		return true;
		
	}

}
