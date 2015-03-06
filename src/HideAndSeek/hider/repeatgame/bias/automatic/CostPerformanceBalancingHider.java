package HideAndSeek.hider.repeatgame.bias.automatic;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.repeatgame.bias.VariableBias;
import Utility.Utils;

/**
 * The presence of lower cost edges may cause Hiders to exhibit bias
 * in their edge selection. The more bias a Hider is, the more likely
 * they are to select lower cost edges.
 * 
 * Depending on the game configuration, selecting lower cost edges repeatedly
 * may result in behaviour that can be predicted. 
 * 
 * This hider attempts to balance the its own cost and the performance of an 
 * opponent to determine the correct amount of bias.
 * 
 * @author Martin
 *
 */
public class CostPerformanceBalancingHider extends VariableBias {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 */
	public CostPerformanceBalancingHider(
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
		
		double seekerPerformanceChange = ( ( graphController.requestAverageSeekersRoundPerformance(roundsPassed) - graphController.requestAverageSeekersRoundPerformance(roundsPassed - 1) ) / graphController.requestAverageSeekersRoundPerformance(roundsPassed - 1) ) * 100;
        
		Utils.talk(toString(), "Cost change: " + costChange);
		
		Utils.talk(toString(), "Seeker performance change: " + seekerPerformanceChange);
		
		if (seekerPerformanceChange > SIGNIFICANCELEVEL) {
			
			decrementBias();
			
		}
		
		if (costChange > SIGNIFICANCELEVEL) {
			
			incrementBias();
			
		}
		
		Utils.talk(toString(), "Bias: " + tendencyToBias);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#printRoundStats()
	 */
	@Override
	public String printRoundStats() {
		
		return super.printRoundStats() + "," + tendencyToBias;
	
	}


	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#printGameStats()
	 */
	@Override
	public String printGameStats() {
		// TODO Auto-generated method stub
		return super.printGameStats();
	}
	
}
