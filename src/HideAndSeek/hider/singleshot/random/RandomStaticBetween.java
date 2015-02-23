package HideAndSeek.hider.singleshot.random;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.Hider;

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
	protected StringVertex startNode() {
		
		return getCurrentNode();
		
	}

}
