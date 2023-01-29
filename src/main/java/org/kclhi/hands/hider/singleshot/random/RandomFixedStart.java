package org.kclhi.hands.hider.singleshot.random;

import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.hider.HidingAgent;

/**
 * 
 * Truly random hider, who starts at a fixed location each time, moves randomly,
 * and selects, with 50% (changeable) chance, to hide at a node when he lands on it.
 * 
 * @author Martin
 *
 */
public class RandomFixedStart extends Random {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 */
	public RandomFixedStart(GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations) {
		super(graphController, numberOfHideLocations);
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	public StringVertex startNode() {
		
		StringVertex[] vertices = new StringVertex[graphController.vertexSet().size()];
		
		return graphController.vertexSet().toArray(vertices)[0];
		
	}

}
