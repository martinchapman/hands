package HideAndSeek.hider.singleshot.random;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.HidingAgent;

/**
 * 
 * Truly random hider, who starts at a random location each time, moves randomly,
 * and selects, with a specified chance, to hide at a node when he lands on it.
 * 
 * Allows for investigation with varying levels of chance to hide within a node.
 * 
 * @author Martin
 *
 */
public class RandomVariableHidePotential extends Random {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 */
	public RandomVariableHidePotential(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations, double hidepotential) {
	
		super(graphController, numberOfHideLocations);
	
		if (hidepotential == 0) { hidepotential = 0.1; }
		
		else { HIDEPOTENTIAL = hidepotential; }
		
	}

}
