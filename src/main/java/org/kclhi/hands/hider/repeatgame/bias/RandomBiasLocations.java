package org.kclhi.hands.hider.repeatgame.bias;

import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
 * Traverses bias edges up until a random number of object have been
 * hidden, and then hides randomly.
 * 
 * @author Martin
 */
public class RandomBiasLocations extends VariableBiasLocations {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 * @param bias
	 */
	public RandomBiasLocations(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations) {
		
		super(graphController, numberOfHideLocations, (int)(Math.random() * numberOfHideLocations));
		
	}

}
