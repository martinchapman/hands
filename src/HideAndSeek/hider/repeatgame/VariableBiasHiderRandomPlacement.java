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
	 * @param graph
	 * @param numberOfHideLocations
	 * @param bias
	 */
	public VariableBiasHiderRandomPlacement(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations, double bias) {
		super(graphController, numberOfHideLocations, bias);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#hideHere(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected boolean hideHere(StringVertex vertex) {
		
		if (Math.random() > 0.5) { return true; }
		return false;
		
	}
	
}
