package org.kclhi.hands.hider.singleshot.preference;

import java.util.HashSet;

import org.jgrapht.alg.DijkstraShortestPath;

import org.kclhi.hands.utility.Utils;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.hider.HidingAgent;

/**
 * Attempts to hide nodes with a maximum possible distance 
 * between them.
 * 
 * @author Martin
 *
 */
public class MaxDistanceStaticBetween extends MaxDistance {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 */
	public MaxDistanceStaticBetween(GraphController <StringVertex, StringEdge> graphController,
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
