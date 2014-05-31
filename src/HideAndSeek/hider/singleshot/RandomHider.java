package HideAndSeek.hider.singleshot;

import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.Hider;

/**
 * 
 * Truly random hider, who starts at a random location each time, moves randomly,
 * and selects, with 50% chance, to hide at a node when he lands on it.
 * 
 * @author Martin
 *
 */
public class RandomHider extends Hider {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 */
	public RandomHider(HiddenObjectGraph<StringVertex, StringEdge> graph,
			int numberOfHideLocations) {
		super(graph, numberOfHideLocations);
	}

	/**
	 * 
	 */
	protected double HIDEPOTENTIAL = 0.5;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#hideHere(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected boolean hideHere(StringVertex vertex) {
		if (Math.random() < HIDEPOTENTIAL) { return true; }
		else { return false; }
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected StringVertex nextNode(StringVertex currentNode) {
		return connectedNode(currentNode);
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	protected StringVertex startNode() {
		return randomNode();
	}

}
