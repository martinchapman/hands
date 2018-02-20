package hideandseek.hider.singleshot.random;

import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;
import hideandseek.hider.HidingAgent;

/**
 * 
 * Truly random hider, who starts at a random location each time, moves randomly,
 * and selects, with 50% (changeable) chance, to hide at a node when he lands on it.
 * 
 * @author Martin
 *
 */
public class RandomStaticBetween extends Random {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 */
	public RandomStaticBetween(GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations) {
		super(graphController, numberOfHideLocations);
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	public StringVertex startNode() {
		
		return currentNode();
		
	}

}
