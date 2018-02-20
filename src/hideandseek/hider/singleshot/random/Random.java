package hideandseek.hider.singleshot.random;

import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;
import hideandseek.hider.HidingAgent;

/**
 * 
 * Random hider, who starts at a random location each time, moves randomly,
 * and selects, with 50% (changeable) chance, to hide at a node when he lands on it.
 * 
 * @author Martin
 *
 */
public class Random extends HidingAgent {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 */
	public Random(GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations) {
		super(graphController, numberOfHideLocations);
	}

	/**
	 * 
	 */
	protected double hidePotential = 0.5;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#hideHere(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public boolean hideHere(StringVertex vertex) {
		
		if (Math.random() < hidePotential) { return true; }
		
		else { return false; }
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public StringVertex nextNode(StringVertex currentNode) {
		
		return connectedNode(currentNode);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	public StringVertex startNode() {
		
		return randomNode();
		
	}

}
