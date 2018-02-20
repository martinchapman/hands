package hideandseek.hider.singleshot.distance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;
import hideandseek.seeker.SeekingAgent;

/**
 * @author Martin
 *
 */
public class GreedyRandomFixedDistanceStaticBetween extends GreedyRandomFixedDistance {

	/**
	 * @param graph
	 */
	public GreedyRandomFixedDistanceStaticBetween(GraphController <StringVertex, StringEdge> graphController, int hideLocations) {

		super(graphController, hideLocations);

	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	public StringVertex startNode() {

		return currentNode();
		
	}

}
