package HideAndSeek.hider.singleshot.random;

import java.util.ArrayList;
import java.util.TreeSet;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import Utility.Utils;

/**
 * Same as parent, but never produces sets with the
 * same random nodes (as long as this is possible).
 * 
 * Should be equivalent to the behaviour of Deceptive, with a deception
 * duration of 1, and a refresh interval of 0. 
 * 
 * @author Martin
 *
 */
public class UniqueRandomSet extends RandomSet {

	private TreeSet<StringVertex> usedNodes;
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public UniqueRandomSet(GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations) {
		
		super(graphController, numberOfHideLocations);
		
		if (usedNodes == null) usedNodes = new TreeSet<StringVertex>();
		
	}
	
	/**
	 * 
	 */
	protected ArrayList<StringVertex> createRandomSet(int size, TreeSet<StringVertex> ignoreSet) {
		
		if (usedNodes == null) usedNodes = new TreeSet<StringVertex>();
		
		ArrayList<StringVertex> hideSet = super.createRandomSet(size, usedNodes);
		
		usedNodes.addAll(hideSet);
		
		Utils.talk(this.toString(), "Adding: " + hideSet);
		
		return hideSet;
		
	}

}
