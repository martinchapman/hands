package HideAndSeek.hider.adaptive;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.repeatgame.VariableBiasHider;
import Utility.Utils;

/**
 * H&S is a multiple objective optimisation problem for the hider,
 * as they must balance bias edges (cheaper but yielding less inconspicuous
 * locations) with explorative edges (more expensive, but yielding more
 * inconspicuous locations). Reduce cost; increase anonymity. 
 * 
 * This hider only focuses on its own cost; increases bias if cost is too
 * high.
 * 
 * @author Martin
 *
 */
public class CostSensitiveHider extends VariableBiasHider {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 */
	public CostSensitiveHider(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations) {
	
		super(graphController, numberOfHideLocations, 0.0);
	
	}

	/**
	 * 
	 */
	private static int SIGNIFICANCELEVEL = 50;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#endOfRound()
	 */
	@Override
	public void endOfRound() {
		
		adjustBias();
		
		super.endOfRound();
		
	}
	
	/**
	 * 
	 */
	protected void adjustBias() {
		
		double costChange = ( ( graphController.requestRoundCost(roundsPassed, this) - graphController.requestRoundCost(roundsPassed - 1, this) ) / graphController.requestRoundCost(roundsPassed - 1, this) )	* 100;
		
		Utils.talk(toString(), "Cost change: " + costChange);
		
		if (costChange > SIGNIFICANCELEVEL) {
			
			incrementBias();
			
		}
		
		Utils.talk(toString(), "Bias: " + tendencyToBias);
		
	}

}
