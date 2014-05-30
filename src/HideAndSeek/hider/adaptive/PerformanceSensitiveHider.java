package HideAndSeek.hider.adaptive;

import HideAndSeek.graph.HiddenObjectGraph;
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
 * This hider only focuses on the performance of the seeker to adjust its bias:
 * only downwards if the performance of the opposition is too good
 * 
 * @author Martin
 *
 */
public class PerformanceSensitiveHider extends VariableBiasHider {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 */
	public PerformanceSensitiveHider(
			HiddenObjectGraph<StringVertex, StringEdge> graph,
			int numberOfHideLocations) {
	
		super(graph, numberOfHideLocations, 1.0);
	
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
		
		double seekerPerformanceChange = ( ( graph.requestAverageSeekerPerformance(roundsPassed) - graph.requestAverageSeekerPerformance(roundsPassed - 1) ) / graph.requestAverageSeekerPerformance(roundsPassed - 1) ) * 100;
        
		Utils.talk(toString(), "Seeker performance change: " + seekerPerformanceChange);
		
		if (seekerPerformanceChange > SIGNIFICANCELEVEL) {
			
			decrementBias();
			
		}
		
		Utils.talk(toString(), "Bias: " + tendencyToBias);
		
	}
	
	
	
}
