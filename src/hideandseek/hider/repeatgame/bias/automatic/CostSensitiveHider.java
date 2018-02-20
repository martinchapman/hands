package hideandseek.hider.repeatgame.bias.automatic;

import Utility.Utils;
import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;
import hideandseek.hider.repeatgame.bias.VariableBias;

/**
 * The presence of lower cost edges may cause Hiders to exhibit bias
 * in their edge selection. The more bias a Hider is, the more likely
 * they are to select lower cost edges.
 * 
 * Depending on the game configuration, selecting lower cost edges repeatedly
 * may result in behaviour that can be predicted. 
 * 
 * This hider only focuses on its own cost; increases bias if cost is too
 * high.
 * 
 * @author Martin
 *
 */
public class CostSensitiveHider extends VariableBias {

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
		
		double costChange = ( ( graphController.roundCost(roundsPassed, this) - graphController.roundCost(roundsPassed - 1, this) ) / graphController.roundCost(roundsPassed - 1, this) )	* 100;
		
		Utils.talk(toString(), "Cost change: " + costChange);
		
		if (costChange > SIGNIFICANCELEVEL) {
			
			incrementBias();
			
		}
		
		Utils.talk(toString(), "Bias: " + tendencyToBias);
		
	}

}
