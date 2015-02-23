package HideAndSeek.hider.reactive;

import java.util.ArrayList;
import java.util.List;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.singleshot.random.RandomSet;
import Utility.Utils;

/**
 * Changes the number of bias hide locations employed in an attempt to deceive
 * the seeker.
 * 
 * Still assumes seeker will choose bias edges going out of a node 100% of the time.
 * 
 * @author Martin
 *
 */
public class DeceptiveHider extends RandomSet {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 * @param numberOfBiasLocations
	 * @param deceptiveNodes
	 * @param deceptionDuration
	 */
	public DeceptiveHider(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations, int deceptiveNodes, int deceptionDuration) {
		
		super(graphController, numberOfHideLocations);
		
		this.deceptiveNodes = deceptiveNodes;
		
		this.deceptionDuration = deceptionDuration;
		
		createDeceptiveSet(deceptiveNodes);

	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 * @param deceptiveNodes
	 * @param deceptionDuration
	 * @param repeatInterval
	 * @param startRound
	 */
	public DeceptiveHider(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations, int deceptiveNodes, int deceptionDuration, int repeatInterval, int startRound, boolean refreshDeceptiveSet) {
		
		this(graphController, numberOfHideLocations, deceptiveNodes, deceptionDuration);
		
		this.repeatInterval = repeatInterval;
	
		this.startRound = startRound;
		
		this.refreshDeceptiveSet = refreshDeceptiveSet;
		
	}
	
	/**
	 * Number of nodes to hide deceptively
	 */
	protected int deceptiveNodes;
	
	/**
	 * Number of rounds for which to hide objects deceptively
	 */
	protected int deceptionDuration;
	
	/**
	 * Round at which to start hiding deceptively
	 */
	protected int startRound = 0;
	
	/**
	 * Interval between repeats. If = 0, no repeats. 
	 */
	protected int repeatInterval = 0;
	
	/**
	 * Whether to use a new deceptive set per repeat. 
	 */
	protected boolean refreshDeceptiveSet = false;
	
	/**
	 * Set containing repeated, 'deceptive' nodes
	 */
	protected ArrayList<StringVertex> deceptiveSet;
	
	/**
	 * Populates a portion of the hide set with 'deceptive nodes' - nodes
	 * that will be replayed for a given duration in subsequent rounds, or 
	 * that are currently being replayed - and the remaining portion with random nodes.
	 * 
	 * @param deceptiveSet
	 */
	private void populateDeceptiveSet(ArrayList<StringVertex> deceptiveSet) {
		
		ArrayList<StringVertex> hideSet = new ArrayList<StringVertex>(deceptiveSet);
		
		hideSet.addAll(createRandomSet(numberOfHideLocations - deceptiveNodes, deceptiveSet));
		
		if ( deceptiveNodes > 0 ) {
		
			List<StringVertex> deceptive = hideSet.subList(0, deceptiveNodes);
			List<StringVertex> random = hideSet.subList(deceptiveNodes, hideSet.size());
		
			Utils.talk(this.toString(), "Complete set: " + deceptive + "|" + random);
		
		}
		
		populateHideSet(hideSet);
		
	}
	
	/**
	 * 
	 */
	private void createDeceptiveSet(int deceptiveNodes) {
		
		deceptiveSet = createRandomSet(deceptiveNodes, new ArrayList<StringVertex>());
		
		// If only a portion of our nodes are deceptive, some must be random
		if (deceptiveNodes < numberOfHideLocations) {
			
			populateDeceptiveSet(deceptiveSet);
		
		// Otherwise populate hide set directly
		} else {
			
			populateHideSet(new ArrayList<StringVertex>(deceptiveSet));
			
		}
		
	}
	
	/**
	 * Refresh only the portion of the hide set that doesn't contain deceptive nodes
	 * 
	 * @param deceptiveNodes
	 * @param deceptiveSet
	 */
	private void updateDeceptiveSet(int deceptiveNodes, ArrayList<StringVertex> deceptiveSet) {
		
		populateDeceptiveSet(deceptiveSet);
			
	}
	
	private int roundNumber = 0;
	
	/**
	 * 
	 */
	@Override
	public void endOfRound() {
		
		super.endOfRound();
		
		/* Update only that portion of the hide set that we aren't using
		   to be deceptive i.e. the portion not defined by the number of deceptive nodes.
		   If all nodes are deceptive, this function call will do nothing. */
		/* OR, if we are within a pre-determined repeating segment, */
		if (roundsPassed < deceptionDuration || ( ( repeatInterval > 0 ) && ((roundsPassed % (deceptionDuration + repeatInterval)) < deceptionDuration) )) {
			
			Utils.talk(this.toString(), "Round: " + roundsPassed + " -- Hiding Deceptively");
			
			
			Utils.talk(this.toString(), "Deceptive Nodes: " + deceptiveSet);
			
			updateDeceptiveSet(deceptiveNodes, deceptiveSet);
			
		/* Otherwise, if the 'deception period' (the period we think is sufficient 
		   to 'fool' the opposition's learning algorithm) is over, simply hide randomly */
		} else {
			
			Utils.talk(this.toString(), "End of deception duration");
			
			if ( refreshDeceptiveSet) deceptiveSet = createRandomSet(deceptiveNodes, new ArrayList<StringVertex>());
			
			populateHideSet(createRandomSet(numberOfHideLocations, deceptiveSet));
			
		}
	
	}
	
}
