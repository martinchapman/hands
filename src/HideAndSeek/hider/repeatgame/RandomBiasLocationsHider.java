package HideAndSeek.hider.repeatgame;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;

/**
 * Traverses bias edges up until a random number of object have been
 * hidden, and then hides randomly.
 * 
 * @author Martin
 */
public class RandomBiasLocationsHider extends VariableBiasLocationsHider {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 * @param bias
	 */
	public RandomBiasLocationsHider(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations) {
		
		super(graphController, numberOfHideLocations, (int)(Math.random() * numberOfHideLocations));
		
	}

}
