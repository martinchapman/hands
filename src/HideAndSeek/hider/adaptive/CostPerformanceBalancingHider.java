package HideAndSeek.hider.adaptive;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.repeatgame.VariableBias;
import Utility.Utils;

/**
 * H&S is a multiple objective optimisation problem for the hider,
 * as they must balance bias edges (cheaper but yielding less inconspicuous
 * locations) with explorative edges (more expensive, but yielding more
 * inconspicuous locations). Reduce cost; increase anonymity. 
 * 
 * This hider attempts to balance the two by evaluating its own performance,
 * and the performance of its opponent, via its own costs, and the performance of 
 * its opponent
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
