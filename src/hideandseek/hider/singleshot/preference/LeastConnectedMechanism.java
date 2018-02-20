package hideandseek.hider.singleshot.preference;

import hideandseek.GraphTraverser;
import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;
import hideandseek.hider.OpenHiderStrategy;

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
