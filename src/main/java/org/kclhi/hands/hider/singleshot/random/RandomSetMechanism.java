package org.kclhi.hands.hider.singleshot.random;

import java.util.ArrayList;
import java.util.TreeSet;

import org.kclhi.hands.GraphTraverser;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

public class RandomSetMechanism extends RandomSet {

	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public RandomSetMechanism( GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations, GraphTraverser responsibleAgent) {
		
		super(graphController, numberOfHideLocations, responsibleAgent);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.singleshot.random.RandomSet#createRandomSet(int, java.util.TreeSet)
	 */
	public ArrayList<StringVertex> createRandomSet(int size, TreeSet<StringVertex> ignoreSet) {
		
		return super.createRandomSet(size, ignoreSet);
		
	}

}
