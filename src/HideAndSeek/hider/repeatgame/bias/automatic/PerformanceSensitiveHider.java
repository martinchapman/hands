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
 * This hider only focuses on the performance of the seeker to adjust its bias:
 * only downwards if the performance of the opposition is too good.
 * 
 * @author Martin
 *
 */
public class PerformanceSensitiveHider extends VariableBias {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 */
	public PerformanceSensitiveHider(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations) {
	
		super(graphController, numberOfHideLocations, 1.0);
	
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
		
		double seekerPerformanceChange = ( ( graphController.requestAverageSeekersRoundPerformance(roundsPassed) - graphController.requestAverageSeekersRoundPerformance(roundsPassed - 1) ) / graphController.requestAverageSeekersRoundPerformance(roundsPassed - 1) ) * 100;
        
		Utils.talk(toString(), "Seeker performance change: " + seekerPerformanceChange);
		
		if (seekerPerformanceChange > SIGNIFICANCELEVEL) {
			
			decrementBias();
			
		}
		
		Utils.talk(toString(), "Bias: " + tendencyToBias);
		
	}
	
	
	
}
