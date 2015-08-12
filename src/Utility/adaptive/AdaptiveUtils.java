package Utility.adaptive;

import java.util.ArrayList;

import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import Utility.Metric;
import Utility.Utils;

/**
 * Various mechanisms for computing whether adaptation should take place,
 * which may be useful for different strategies.
 * 
 * @author Martin
 *
 */
public class AdaptiveUtils {
	
	/**
	 * Takes a set of performance values, and checks if a sequence
	 * of size X exists in which the values in this sequence all fall 
	 * below Y.
	 * 
	 * @param performanceValues
	 * @param valueThreshold (Y)
	 * @param sequenceLength (X)
	 * @return
	 */
	public static boolean containsLowPerformance(ArrayList<Double> performanceValues, double valueThreshold, int sequenceLength) {
		
		if ( performanceValues.size() >= sequenceLength ) {
			
			for ( int i = performanceValues.size() - sequenceLength; i < performanceValues.size(); i++ ) {
				
				if ( performanceValues.get(i) > valueThreshold ) { 
					
					return false;
				
				} 	
				
			}
			
			return true;
			
		}
		
		return false;
				
	}
	
	/**
	 * @param graphController
	 * @param traverser
	 * @param percentageChanges
	 * @return
	 */
	public static boolean containsLowPerformance(ArrayList<Double> performanceValues) {
			
		return containsLowPerformance(performanceValues, 0.5, 3);
		
	}
	
	/**
	 * @param performanceValues
	 * @param valueThreshold
	 * @param sequenceLength
	 * @return
	 */
	public static boolean containsHighPerformance(ArrayList<Double> performanceValues, double valueThreshold, int sequenceLength) {
		
		if ( performanceValues.size() >= sequenceLength ) {
			
			for ( int i = performanceValues.size() - sequenceLength; i < performanceValues.size(); i++ ) {
				
				if ( performanceValues.get(i) < valueThreshold ) { 
					
					return false;
				
				}
					
				
			}
			
			return true;
			
		}
		
		return false;
				
	}
	
	/**
	 * @param performanceValues
	 * @return
	 */
	public static boolean containsHighPerformance(ArrayList<Double> performanceValues) {
		
		return containsHighPerformance(performanceValues, 0.5, 3);
		
	}
	
	/**
	 * @param roundStrategyPerformance
	 * @param metric
	 * @return
	 */
	public static double internalMeasure(ArrayList<Double> roundStrategyPerformance, Metric metric) {
		
		 return internalMeasure(roundStrategyPerformance, metric, null);
		 
	}
	
	/**
	 * @param roundStrategyPerformance
	 * @param metric
	 * @param hiddenObjectGraph
	 * @return
	 */
	public static double internalMeasure(ArrayList<Double> roundStrategyPerformance, Metric metric, HiddenObjectGraph<StringVertex, StringEdge> hiddenObjectGraph) {
		
		// Payoff between first round and this
		if ( metric == Metric.RELATIVE_COST ) {
			
			if ( roundStrategyPerformance.size() < 2) return 0.0;
			
			double difference = Utils.percentageChange(roundStrategyPerformance.get(0), roundStrategyPerformance.get(roundStrategyPerformance.size() - 1)) / 100.0;
			
			Utils.talk("internalMeasure", "Payoff relative (first round): Percentage change " + roundStrategyPerformance.get(0) + " to " + roundStrategyPerformance.get(roundStrategyPerformance.size() - 1));
			
			if ( difference < -1 ) {
				
				Utils.talk("internalMeasure", "Payoff relative: Returning " + 1);
	
				return 1;
				
			} else if ( difference > 0 ) {
				
				Utils.talk("internalMeasure", "Payoff relative: Returning " + 0);
				
				return 0;
				
			} else {
				
				Utils.talk("internalMeasure", "Payoff relative: Returning " + -1 * difference);
				
				return -1 * difference;
				
			}
		
		// Payoff between last round and this
		} else if ( metric == Metric.COST_CHANGE_PAYOFF ) {
		
			if ( roundStrategyPerformance.size() < 2) return 0.0;
			
			double difference = Utils.percentageChange(roundStrategyPerformance.get(roundStrategyPerformance.size() - 2), roundStrategyPerformance.get(roundStrategyPerformance.size() - 1)) / 100.0;
			
			Utils.talk("internalMeasure", "Payoff relative (Last round): Percentage change " + roundStrategyPerformance.get(roundStrategyPerformance.size() - 2) + " to " + roundStrategyPerformance.get(roundStrategyPerformance.size() - 1));
			
			if ( difference < -1 ) {
				
				Utils.talk("internalMeasure", "Payoff relative: Returning " + 1);
	
				return 1;
				
			} else if ( difference > 0 ) {
				
				Utils.talk("internalMeasure", "Payoff relative: Returning " + 0);
				
				return 0;
				
			} else {
				
				Utils.talk("internalMeasure", "Payoff relative: Returning " + -1 * difference);
				
				return -1 * difference;
				
			}
			
		} else if ( metric == Metric.TOTAL_EDGE_COST ) {
			
			Utils.talk("internlMeasure", " Payoff / Total Edge Cost = " + Math.abs(roundStrategyPerformance.get(roundStrategyPerformance.size() - 1)) + " " + Utils.totalEdgeCost(hiddenObjectGraph) + " " + Math.abs(roundStrategyPerformance.get(roundStrategyPerformance.size() - 1)) / Utils.totalEdgeCost(hiddenObjectGraph));
			
			return 1.0 - ( Math.abs(roundStrategyPerformance.get(roundStrategyPerformance.size() - 1)) / Utils.totalEdgeCost(hiddenObjectGraph) );
			
		} else {
			
			return 0.0;
			
		}
		
	}

}