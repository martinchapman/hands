package org.kclhi.hands.hider.repeatgame.bias;

import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
 * Agent exhibiting a partner property to Variable Bias hider:
 * Traverses bias edges up until a specified number of object have been
 * hidden, and then hides randomly.
 * 
 * Hypothesis: If all objects are hidden randomly this will yield a cheaper 
 * traversal cost for the hider, although this will make their
 * behaviour more obvious to an observing seeker.
 * 
 * Assumes that this hider always chooses bias edges over explorative ones.
 * Hypothesis: Higher level of bias control than Variable Bias Hider.
 * 
 * @author Martin
 */
public class VariableBiasLocations extends VariableBias {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 * @param bias
	 */
	public VariableBiasLocations(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations, int numberOfBiasLocations) {
		super(graphController, numberOfHideLocations, 1.0);
		
		if ( numberOfBiasLocations > numberOfHideLocations ) {
			
			this.numberOfBiasLocations = numberOfHideLocations;
			
		} else {
			
			this.numberOfBiasLocations = numberOfBiasLocations;
			
		}
		
	}

	/**
	 * 
	 */
	protected int numberOfBiasLocations;

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public StringVertex nextNode(StringVertex currentNode) {
		
		if ( graphController.numberOfHideLocations(responsibleAgent) >= numberOfBiasLocations ) {
			
			return connectedNode(currentNode);
			
		} else {
			
			return super.nextNode(currentNode);
			
		}
		
	}

}
