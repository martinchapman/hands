package HideAndSeek.hider.repeatgame.deceptive;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.singleshot.random.RandomSet;
import Utility.BinomialProbability;
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
public class Deceptive extends RandomSet {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 * @param numberOfBiasLocations
	 * @param deceptiveNodes
	 * @param deceptionDuration
	 */
	public Deceptive(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations, int deceptiveNodes, int deceptionDuration) {
		
		super(graphController, numberOfHideLocations);
		
		// ~MDC Change back to true for more reliable results
		strategyOverRounds = false;
		
		this.deceptiveNodes = deceptiveNodes <= numberOfHideLocations ? deceptiveNodes : numberOfHideLocations;
		
		this.deceptionDuration = deceptionDuration > 0 ? deceptionDuration : 1;
		
		nodesUsedDeceptively = new TreeSet<StringVertex>();
		
		createDeceptiveSet(deceptiveNodes);
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 * @param deceptiveNodes
	 * @param deceptionDuration
	 * @param repeatInterval
	 * @param repeatDuration
	 * @param startRound
	 * @param refreshDeceptiveSet
	 */
	public Deceptive(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations, int deceptiveNodes, int deceptionDuration, 
			int repeatInterval, int repeatDuration, boolean refreshDeceptiveSet) {
		
		this(graphController, numberOfHideLocations, deceptiveNodes, deceptionDuration);
		
		this.repeatInterval = repeatInterval;
	
		this.repeatDuration = repeatDuration;
		
		this.refreshDeceptiveSet = refreshDeceptiveSet;
		
	}
	
	public Deceptive(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations, int deceptiveNodes, int deceptionDuration, 
			int repeatInterval, int repeatDuration, boolean refreshDeceptiveSet,
			boolean doNotRevisit) {
		
		this(graphController, numberOfHideLocations, deceptiveNodes, deceptionDuration, repeatInterval, repeatDuration, refreshDeceptiveSet);
		
		this.doNotRevisit = doNotRevisit;
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 * @param deceptiveNodes
	 * @param deceptionDuration
	 * @param repeatInterval
	 * @param repeatDuration
	 * @param deceptiveSets
	 */
	public Deceptive(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations, int deceptiveNodes, int deceptionDuration, 
			int repeatInterval, int repeatDuration, int deceptiveSets) {
		
		this(graphController, numberOfHideLocations, deceptiveNodes, deceptionDuration, repeatInterval, repeatDuration, false);
		
		this.deceptiveSets = deceptiveSets;
		
		createDeceptiveSet(deceptiveNodes);
		
	}
		
	
	/**
	 * Number of nodes to hide deceptively
	 */
	protected int deceptiveNodes;
	
	/**
	 * Number of rounds for which to hide objects deceptively.
	 * If is equal to one, then deception does not come in the form
	 * of over training, but as never visiting those nodes again. 
	 */
	protected int deceptionDuration;
	
	
	/**
	 * In the presence of repetition, how many rounds to keep repeating for.
	 */
	protected int repeatDuration;
	
	
	/**
	 * Round at which to start hiding deceptively
	 */
	protected int startRound = 0;
	
	/**
	 * Interval between repeats. If = 0, no repeats. 
	 */
	protected int repeatInterval = -1;
	
	/**
	 * Number of deceptive sets
	 */
	protected int deceptiveSets = 1;
	
	/**
	 * Whether to use a new deceptive set per repeat. 
	 */
	protected boolean refreshDeceptiveSet = false;
	
	/**
	 * Set containing repeated, 'deceptive' nodes
	 */
	protected ArrayList<StringVertex> deceptiveSet;
	
	/**
	 * List of deceptive sets, if multiple ones appear
	 */
	protected ArrayList<ArrayList<StringVertex>> deceptiveSetList;
	
	/**
	 * A record of those nodes which have been used 
	 * priorly for deception.
	 */
	protected TreeSet<StringVertex> nodesUsedDeceptively;
	
	/**
	 * 
	 */
	protected boolean doNotRevisit = true;
	
	/**
	 * Populates a portion of the hide set with 'deceptive nodes' - nodes
	 * that will be replayed for a given duration in subsequent rounds, or 
	 * that are currently being replayed - and the remaining portion with random nodes.
	 * 
	 * @param deceptiveSet
	 */
	public ArrayList<StringVertex> populateDeceptiveSet(ArrayList<StringVertex> deceptiveSet) {
		
		nodesUsedDeceptively.addAll(deceptiveSet);
		
		ArrayList<StringVertex> hideSet;
		
		/* ~MDC Perhaps unnecessary check. In case nodesUsedDeceptively is full, 
		 * and random part clashes which deceptive part */ 
		while (true) {
			
			hideSet = new ArrayList<StringVertex>(deceptiveSet);
			
			hideSet.addAll(createRandomSet(numberOfHideLocations - deceptiveSet.size(), nodesUsedDeceptively));
		
			if ( deceptiveSet.size() > 0 ) {
			
				List<StringVertex> deceptive = hideSet.subList(0, deceptiveSet.size() );
				List<StringVertex> random = hideSet.subList(deceptiveSet.size() , hideSet.size());
			
				Utils.talk(this.toString(), "Complete set: " + deceptive + "|" + random);
				
				List<StringVertex> common = new ArrayList<StringVertex>(deceptive);
				common.retainAll(random);
				
				if (common.size() == 0) break;
			
			} else {
				
				break;
				
			}
			
		}
		
		populateHideSet(hideSet);
		
		return hideSet;
		
	}
	
	/**
	 * @param deceptiveSet
	 * @return
	 */
	public void createDeceptiveSet(int deceptiveNodes) {
		
		if ( deceptiveSetList == null ) { deceptiveSetList = new ArrayList<ArrayList<StringVertex>>(); }
		
		deceptiveSetList.clear();
		
		for ( int i = 0; i < deceptiveSets; i++ ) {
			
			deceptiveSetList.add(createRandomSet(deceptiveNodes, nodesUsedDeceptively));
			
			populateDeceptiveSet(deceptiveSetList.get(i));
			
		}
		
		System.out.println(deceptiveSetList);
		
		setDeceptiveSet(deceptiveSetList.get(0));
		
	}
	
	/**
	 * @param deceptiveSet
	 */
	public void setDeceptiveSet(ArrayList<StringVertex> deceptiveSet) {
		
		this.deceptiveSet = deceptiveSet;
		
		populateDeceptiveSet(this.deceptiveSet);
		
	}
	
	/**
	 * @return
	 */
	protected boolean shouldPlayDeceptive() {
		
		if ( deceptionDuration == 0 ) return false;
		
		/* Update only that portion of the hide set that we aren't using
		   to be deceptive i.e. the portion not defined by the number of deceptive nodes.
		   If all nodes are deceptive, this function call will do nothing. */
		/* OR, if we are within a pre-determined repeating segment */
		if (roundsPassed < deceptionDuration || ( (repeatInterval > -1) && (roundsPassed < repeatDuration) && ((roundsPassed % (deceptionDuration + repeatInterval)) < deceptionDuration) )) {
			
			if ( ( refreshDeceptiveSet && (roundsPassed % (deceptionDuration + repeatInterval) == 0) ) ) {
				
				Utils.talk(this.toString(), "Refreshing Deceptive Set.");
				
				createDeceptiveSet(deceptiveNodes);
				
			}
			
			if ( deceptiveSetList != null && deceptiveSetList.size() > 1 ) {
				
				Utils.talk(this.toString(), "Selecting from deceptive sets: " + deceptiveSetList);
				
				setDeceptiveSet(deceptiveSetList.get((int)(Math.random() * deceptiveSet.size())));
				
			}
			
			return true;
			
		/* Otherwise, if the 'deception period' (the period we think is sufficient 
		   to 'fool' the opposition's learning algorithm) is over, simply hide randomly */
		} else {
			
			return false;
			
		}
		
	}
	
	/**
	 * 
	 */
	@Override
	public void endOfRound() {
		
		super.endOfRound();
		
		if (shouldPlayDeceptive()) {
			
			Utils.talk(this.toString(), "Round: " + roundsPassed + " -- Hiding Deceptively");
			
			Utils.talk(this.toString(), "Deceptive Nodes: " + deceptiveSet);
			
			populateDeceptiveSet(deceptiveSet);
		
		} else {
			
			Utils.talk(this.toString(), "End of deception duration");
			
			if ( doNotRevisit ) {
				
				// This will override above (except new deceptive set), which it should do in some instances.
				populateHideSet(createRandomSet(numberOfHideLocations, nodesUsedDeceptively));
			
			} else {
				
				populateHideSet(createRandomSet(numberOfHideLocations, new TreeSet<StringVertex>()));
				
			}
			
		}
	
	}
	
	/**
	 * 
	 */
	public void endOfGame() {
		
		super.endOfGame();
	
		nodesUsedDeceptively = new TreeSet<StringVertex>();
		
		createDeceptiveSet(deceptiveNodes);
		
	}
	
}
