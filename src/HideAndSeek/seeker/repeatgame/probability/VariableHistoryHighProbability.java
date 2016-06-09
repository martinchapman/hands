package HideAndSeek.seeker.repeatgame.probability;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;

/**
 * Extends the standard High Probability Seeker adding the capacity
 * to specify the *number of initial items of learning to discount*.
 * Commonly, this is specified in terms of a 'back range', which varies
 * per round (e.g. 'include patterns that have been recorded from two rounds ago'), 
 * but can also be specified as a fixed point (e.g. 'only include learning that
 * happened after round 5').
 * 
 * @author Martin
 *
 */
public class VariableHistoryHighProbability extends HighProbability {
	
	private int backRange = 0;
	
	/**
	 * As well as the most recent data, how many more rounds BACK
	 * of learning do we wish to include.
	 * 
	 * E.g. if backRange = 1, will include most recent learning PLUS
	 * learning from one round before (two sets of data).
	 * 
	 * @param graphController
	 * @param backRange
	 */
	public VariableHistoryHighProbability(
			GraphController <StringVertex, StringEdge> graphController, int backRange) {
		super(graphController);
		
		this.backRange = backRange;
		
		
		
		// ~MDC Change back to true when parameters are varied per game
		strategyOverRounds = false;
		
	}
	
	/**
	 * @param recordStartIndex
	 */
	public void setRecordStartIndex(int recordStartIndex) {
		
		behaviourPrediction.setRecordStartIndex(recordStartIndex);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.repeatgame.HighProbability#endOfRound()
	 */
	public void endOfRound() {
		
		super.endOfRound();
		
		if ( backRange > 0 ) {
			
			// roundsPassed - 1 = most recent data.
			behaviourPrediction.setRecordStartIndex((roundsPassed - 1) - backRange);
		
		} 
		
	}
	
}
