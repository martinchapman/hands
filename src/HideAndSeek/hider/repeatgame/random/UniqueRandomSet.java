package HideAndSeek.hider.repeatgame.random;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.singleshot.random.RandomSet;
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

	/**
	 * @param graphController
	 * @param name
	 * @param numberOfHideLocations
	 */
	public UniqueRandomSet(GraphController<StringVertex, StringEdge> graphController, String name, int numberOfHideLocations) {
		
		super(graphController, name, numberOfHideLocations);
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public UniqueRandomSet(GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations) {
		
		this(graphController, "", numberOfHideLocations);
		
	}
	
	/**
	 * @param size
	 * @param ignoreSet NB: Parameter ignored here. Used simply for override.
	 * @return
	 */
	protected ArrayList<StringVertex> createRandomSet(int size, TreeSet<StringVertex> ignoreSet) {
		
		ArrayList<StringVertex> hideSet = super.createRandomSet(size, new TreeSet<StringVertex>(uniqueHideLocations()));
		
		return hideSet;
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.singleshot.random.RandomSet#endOfRound()
	 */
	@Override
	public void endOfRound() {

		super.endOfRound();
		
	}

}
