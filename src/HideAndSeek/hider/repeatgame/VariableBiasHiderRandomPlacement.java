package HideAndSeek.hider.repeatgame;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;

/**
 * Same as parent class, except only plants items with 50% probability
 * 
 * @author Martin
 */
public class VariableBiasHiderRandomPlacement extends VariableBiasHider {

	/**
	 * As parent, except has percentage chance to hide in each location
	 * 
	 * @param graph
	 * @param numberOfHideLocations
	 * @param bias
	 */
	public VariableBiasHiderRandomPlacement(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations, double bias) {
		super(graphController, numberOfHideLocations, bias);
		
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
		
		if (Math.random() > HIDEPOTENTIAL) { return true; }
		return false;
		
	}
	
}
