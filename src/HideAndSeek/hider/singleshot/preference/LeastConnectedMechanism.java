package HideAndSeek.hider.singleshot.preference;

import HideAndSeek.GraphTraverser;
import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.OpenHiderStrategy;

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
