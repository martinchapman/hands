package HideAndSeek.hider.repeatgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.Hider;
import Utility.Utils;

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
			HiddenObjectGraph<StringVertex, StringEdge> graph,
			int numberOfHideLocations) {
		
		super(graph, numberOfHideLocations, (int)(Math.random() * numberOfHideLocations));
		
	}

}
