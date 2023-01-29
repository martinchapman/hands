package org.kclhi.hands.hider.singleshot.random;

import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.hider.HidingAgent;

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
