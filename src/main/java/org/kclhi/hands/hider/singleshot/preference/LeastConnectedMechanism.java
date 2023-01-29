package org.kclhi.hands.hider.singleshot.preference;

import org.kclhi.hands.GraphTraverser;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.hider.OpenHiderStrategy;

/**
 * Exposes the LeastConnected mechanism for use by other
 * strategies.
 * 
 * @author Martin
 *
 */
public class LeastConnectedMechanism extends LeastConnected implements OpenHiderStrategy {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 */
	public LeastConnectedMechanism(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations, GraphTraverser responsibleAgent) {
		
		super(graphController, numberOfHideLocations, responsibleAgent);
		
	}

	/**
	 * @param vertex
	 * @return
	 */
	public boolean hideHere(StringVertex vertex) {
		
		return hideHere(vertex);
		
	}

}
