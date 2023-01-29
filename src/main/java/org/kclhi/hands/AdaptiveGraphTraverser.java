package org.kclhi.hands;

import java.util.ArrayList;

import org.kclhi.hands.utility.adaptive.AdaptiveMeasure;
import org.kclhi.hands.utility.adaptive.AdaptiveWeightings;

public interface AdaptiveGraphTraverser {
	
	/**
	 * Adaptive strategies must provide 
	 * a mechanism that reports on their ability to 
	 * operate effectively under their given strategy,
	 * based upon game conditions, and the performance/attributes
	 * of their opponent.
	 * 
	 * This information guides the decision as to whether adaptation
	 * should take place. 
	 * 
	 * @return
	 */
	
	/**
	 * The relevance of a strategy to the environment.
	 *  
	 * A lower value here signals that adaptation should occur. 
	 * 
	 * @return
	 */
	public AdaptiveMeasure environmentalMeasure();
	
	/**
	 * The relevance of a strategy to the (perception of an) opponent's strategy
	 * 
	 * A lower value here signals that adaptation should occur. 
	 * 
	 * @return
	 */
	public AdaptiveMeasure socialMeasure();
	
	/**
	 * The performance of a strategy itself.
	 * 
	 * A lower value here signals that adaptation should occur. 
	 * 
	 * @return
	 */
	public AdaptiveMeasure internalMeasure(ArrayList<Double> roundStrategyPerformance);
	
	/**
	 * @return
	 */
	public AdaptiveWeightings getAdaptiveWeightings();
	
	/**
	 * In the event that a strategy is not being played, but it 
	 * remains in the portfolio, it is necessary for it to keep
	 * up to date by performing routine operations per round,
	 * such as recording appropriate information. In this way,
	 * if it is used again, it will continue to be effective.
	 * 
	 * @return
	 */
	//public abstract void whileDormant();
	
	/**
	 * Actions to be taken once the decision has been made to adapt
	 */
	public void stopStrategy();

}
