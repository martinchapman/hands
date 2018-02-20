package hideandseek.hider.repeatgame.bias;

import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;

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
