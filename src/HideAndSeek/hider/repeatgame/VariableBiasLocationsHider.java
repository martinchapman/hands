package HideAndSeek.hider.repeatgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.Hider;
import Utility.Utils;

/**
 * Agent exhibiting a partner property to Variable Bias hider:
 * Traverses bias edges up until a specified number of object have been
 * hidden, and then hides randomly.
 * 
 * If all objects are hidden randomly this will yield a cheaper 
 * traversal cost for the hider, although this will make their
 * behaviour more obvious to an observing seeker.
 * 
 * Assumes that this hider always chooses bias edges over explorative ones.
 * Higher level of bias control than Variable Bias Hider.
 * 
 * @author Martin
 */
public class VariableBiasLocationsHider extends VariableBiasHider {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 * @param bias
	 */
	public VariableBiasLocationsHider(
			HiddenObjectGraph<StringVertex, StringEdge> graph,
			int numberOfHideLocations, int numberOfBiasLocations) {
		super(graph, numberOfHideLocations, 1.0);
		
		this.numberOfBiasLocations = numberOfBiasLocations;
		
	}

	/**
	 * 
	 */
	protected int numberOfBiasLocations;

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected StringVertex nextNode(StringVertex currentNode) {
		
		if ( hideLocations.size() == numberOfBiasLocations ) {
			
			return connectedNode(currentNode);
			
		} else {
			
			return super.nextNode(currentNode);
			
		}
		
	}

}
