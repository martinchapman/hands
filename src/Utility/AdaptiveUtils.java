package Utility;

import java.util.ArrayList;

import HideAndSeek.GraphTraversingAgent;
import HideAndSeek.graph.GraphController;

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

}
