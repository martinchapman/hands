package org.kclhi.hands.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.jfree.ui.RefineryUtilities;

import org.kclhi.hands.utility.output.BarGraph;
import org.kclhi.hands.graph.StringVertex;

/**
 * 
 * Implements check for hiding locations between 
 * consecutive rounds that match. This is an attempt
 * to detect and remove data which has been used in an
 * attempt to fool the frequency algorithm.
 * 
 * Can specify the number of consecutive appearances of a 
 * set of nodes that must appear, and how many nodes must match.
 * 
 * 
 * @author Martin
 *
 */
public class BehaviourPredictionRepetitionCheck extends BehaviourPrediction  {

	/**
	 * 
	 */
	private ArrayList<ArrayList<StringVertex>> hideLocations;
	
	/**
	 * 
	 */
	private int numberOfConsecutiveMatches;
	
	/**
	 * 
	 */
	private int nodesToCompare;
	
	/**
	 * 
	 */
	public BehaviourPredictionRepetitionCheck(int numberOfConsecutiveMatches, int nodesToCompare) {
		
		super();
		
		resetData();
		
		this.numberOfConsecutiveMatches = numberOfConsecutiveMatches;
		
		this.nodesToCompare = nodesToCompare;
		
	}
	
	protected void resetData() {
		
		super.resetData();
		
		hideLocations = new ArrayList<ArrayList<StringVertex>>();
		
		hideLocations.add(new ArrayList<StringVertex>());
		
	}
	
	/**
	 * @param hideLocation
	 */
	public void recordHideLocation(StringVertex hideLocation) {
		
		hideLocations.get(hideLocations.size() - 1).add(hideLocation);
		
	}
	
	/**
	 * Should happen after the end of each round 
	 */
	public synchronized void endRound() {
		
		Collections.sort(hideLocations.get(hideLocations.size() - 1));
		
		int startIndex = ((hideLocations.size() - 1) - (numberOfConsecutiveMatches - 1)) > 0 ? (hideLocations.size() - 1) - (numberOfConsecutiveMatches - 1) : 0;
		
		boolean matches = true;
		/* e.g. indexes: [0, 1, 2]. Size = 3. Number of consecutive matches = 3.
		 * Size - 1 = 2 (i2). Matches - 1 = 2. (Size - 1) - 2 = 1 (i0). Compare i0 and i1, and i1 and i2. 
		 */
		
		System.out.println("Looking for " + numberOfConsecutiveMatches + " conseuctive matches");
		
		System.out.println("Most recent round data: " + hideLocations.get(hideLocations.size() - 1));
		
		System.out.println("Start index: " + startIndex);
		
		for ( int i = startIndex; i < hideLocations.size() - 1; i++) {
			
			Utils.talk("BehaviourPrediction", "Comparing " + hideLocations.get(i) + " with " + hideLocations.get(i + 1) + ". Index " + i + " with " + (i + 1));
			
			for ( int j = 0; j < nodesToCompare; j++) {
				
				Utils.talk("BehaviourPrediction", "Comparing " + hideLocations.get(i).get(j) + " with " + hideLocations.get(i + 1).get(j));
				
				if (hideLocations.get(i).get(j) != hideLocations.get(i + 1).get(j)) matches = false;
				
			}
			
		}
		
		
		/* If the current hide locations are EXACTLY the same as the last,
		 * remove the last and avoid adding this new information.*/
		if ( startIndex != hideLocations.size() - 1 && matches ) {
			
			Utils.talk("BehaviourPrediction", "Matches found, removing inaccurate data");
			
			// Reset all 'bad' learning back to last reliable place, which was prior to the first match
			int lastReliableData = startIndex - 1; 
			
			// If the first N sets of locations match.
			if (lastReliableData < 0) {
				
				resetData();
				
				return;
				
			}
			
			for ( int i = startIndex; i < hideLocationsList.size(); i++ ) {
				
				/* By overwriting the information we gained in the last round, with information from the round before,
				 * we erase the latest learning.*/
				
				hideLocationsList.remove(i);
				hideLocationsList.add(i, hideLocationsList.get(lastReliableData));
				
				hideProbabilitiesList.remove(i);
				hideProbabilitiesList.add(i, hideProbabilitiesList.get(lastReliableData));
			
			}
			
		} else {
			
			for ( StringVertex discoveredVertex : hideLocations.get(hideLocations.size() - 1)) {
				
				super.recordHideLocation(discoveredVertex);
				
			}
			
			super.endRound();

		}
		
		hideLocations.add(new ArrayList<StringVertex>());
		
	}
	
}
