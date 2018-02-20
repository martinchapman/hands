package hideandseek.hider.singleshot.random;

import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;
import hideandseek.hider.HidingAgent;

/**
 * 
 * Random hider, who starts at a random location each time, moves randomly,
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
	public RandomVariableHidePotential(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations, double hidePotential) {
	
		super(graphController, numberOfHideLocations);
	
		if (hidePotential == 0) { 
			
			this.hidePotential = 0.1; 
			
		} else { 
			
			this.hidePotential = hidePotential; 
			
		}
		
	}

}
