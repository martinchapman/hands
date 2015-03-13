package HideAndSeek.hider.repeatgame.random;

import java.util.ArrayList;
import java.util.TreeSet;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.singleshot.random.RandomSet;
import Utility.Utils;

/**
 * Same as grandparent, but never produces sets with the
 * same random nodes AND does this on loop.
 * 
 * Attempts to capitalise on the idea that learning is effectively reset
 * after the maximum number of unique hide locations has been played.
 * 
 * @author Martin
 *
 */
public class UniqueRandomSetRepeat extends UniqueRandomSet {

	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public UniqueRandomSetRepeat(GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations) {
		
		super(graphController, numberOfHideLocations);
		
	}
	
	/**
	 * 
	 */
	protected ArrayList<StringVertex> createRandomSet(int size, TreeSet<StringVertex> ignoreSet) {
		
		if (usedNodes == null) usedNodes = new TreeSet<StringVertex>();
		
		if (usedNodes.size() == graphController.vertexSet().size()) usedNodes.clear();
		
		ArrayList<StringVertex> hideSet = super.createRandomSet(size, ignoreSet);
			
		return hideSet;
		
	}

}
