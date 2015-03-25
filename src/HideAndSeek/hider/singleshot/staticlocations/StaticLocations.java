package HideAndSeek.hider.singleshot.staticlocations;

import java.util.ArrayList;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.singleshot.random.RandomSet;
import Utility.Utils;

/**
 * Produces a K size set of random nodes and then
 * heads for those nodes on the graph EACH round.
 * 
 * @author Martin
 *
 */
public class StaticLocations extends RandomSet {

	private ArrayList<StringVertex> staticSet;
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public StaticLocations(GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations) {
		
		super(graphController, numberOfHideLocations);
		
		this.staticSet = new ArrayList<StringVertex>(getHideSet());
		
		Utils.talk(this.toString(), "Constructor static set:" + staticSet.toString());
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	public StringVertex startNode() {
		
		return getCurrentNode();
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#endOfRound()
	 */
	@Override
	public void endOfRound() {
		
		super.endOfRound();
		
		Utils.talk(this.toString(), "Static set:" + this.staticSet.toString());
		
		populateHideSet(new ArrayList<StringVertex>(this.staticSet));
		
		
		
	}

}
