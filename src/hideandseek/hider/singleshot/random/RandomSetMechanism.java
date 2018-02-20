package hideandseek.hider.singleshot.random;

import java.util.ArrayList;
import java.util.TreeSet;

import hideandseek.GraphTraverser;
import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;

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
