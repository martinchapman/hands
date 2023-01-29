package org.kclhi.hands.hider.repeatgame.random;

import java.util.ArrayList;
import java.util.TreeSet;

import org.kclhi.hands.utility.Utils;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.hider.singleshot.random.RandomSet;

/**
 * Chooses the number of nodes that are hidden uniquely at random
 * 
 * @author Martin
 *
 */
public class UniqueRandomSetRepeatRandomNodes extends UniqueRandomSetRepeat {

	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public UniqueRandomSetRepeatRandomNodes(GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations) {
		
		super(graphController, numberOfHideLocations);
		
	}
	
	/**
	 * 
	 */
	protected ArrayList<StringVertex> createRandomSet(int size, TreeSet<StringVertex> ignoreSet) {
		
		int setSize = ((int)(Math.random() * numberOfHideLocations)) + 1;
		
		ArrayList<StringVertex> uniqueNodes = super.createRandomSet(size, new TreeSet<StringVertex>(uniqueHideLocations()));
		
		ArrayList<StringVertex> randomNodes = super.createRandomSet(numberOfHideLocations - setSize, new TreeSet<StringVertex>());
		
		uniqueNodes.addAll(randomNodes);
			
		uniqueHideLocations().addAll(uniqueNodes);
		
		return uniqueNodes;
		
	}

}
