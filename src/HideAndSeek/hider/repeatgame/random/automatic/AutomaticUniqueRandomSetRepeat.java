package HideAndSeek.hider.repeatgame.random.automatic;

import java.util.ArrayList;
import java.util.TreeSet;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.repeatgame.random.UniqueRandomSetRepeat;
import Utility.AdaptiveUtils;
import Utility.Metric;
import Utility.Utils;

/**
 * Alters the size of the unique hide set if it believes this style
 * of play is being anticipated too easily by an opponent.
 * 
 * ~MDC 29/5 How does this differ from simply taking a probability distribution over HighProb and InverseHighProb?
 * 
 * @author Martin
 *
 */
public class AutomaticUniqueRandomSetRepeat extends UniqueRandomSetRepeat {

	/**
	 * 
	 */
	private ArrayList<Double> costChangeValues;
	
	/**
	 * 
	 */
	private int setSize;
	
	/**
	 * Number of rounds for which a Seeker has exhibited
	 * good performance
	 */
	private int goodPerformanceRounds;
	
	/**
	 * Number of rounds for which a Seeker should exhibit
	 * good performance before this strategy changes the number
	 * of unique nodes used
	 */
	private int goodPerformanceRoundsThreshold;
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 * @param goodPerformanceRounds
	 */
	public AutomaticUniqueRandomSetRepeat(GraphController<StringVertex, StringEdge> graphController, String name, int numberOfHideLocations, int goodPerformanceRoundsThreshold) {
		
		super(graphController, numberOfHideLocations);
		
		this.costChangeValues = new ArrayList<Double>();
		
		this.setSize = numberOfHideLocations;
		
		this.goodPerformanceRoundsThreshold = goodPerformanceRoundsThreshold;
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 * @param goodPerformanceRoundsThreshold
	 */
	public AutomaticUniqueRandomSetRepeat(GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations, int goodPerformanceRoundsThreshold) {
		
		this(graphController, "", numberOfHideLocations, goodPerformanceRoundsThreshold);
		
	}

	public void endOfRound() {
		
		super.endOfRound();
		
		costChangeValues.add(graphController.averageSeekersPerformance(Metric.COST_CHANGE));
		
		if ( AdaptiveUtils.containsHighPerformance(costChangeValues, 0.5, goodPerformanceRounds) ) {
			
			goodPerformanceRounds++;
			
		} else if ( AdaptiveUtils.containsLowPerformance(costChangeValues, 0.5, goodPerformanceRounds) ) {
			
			goodPerformanceRounds = goodPerformanceRounds - 1 >= 0 ? goodPerformanceRounds - 1 : 0;
			
		}
		
		/* 
		 * If the opponent is playing too well, alter the number
		 * of unique hide locations to counter any strategies that may be 
		 * tuned to deal with unique play. If they then do not play well, 
		 * the number of unique hide locations can likely be increased again.
		 */
		if ( goodPerformanceRounds > goodPerformanceRoundsThreshold ) {  
			
			setSize = setSize > 0 ? setSize - 1 : 0;
			
			/* 
			 * Reset the number of good performance rounds, once the number 
			 * that corresponds to the threshold have been found
			 */
			goodPerformanceRounds = 0;
			
			Utils.talk(toString(), "Changing unique hide set size to: " + setSize);
		
		} else if ( goodPerformanceRounds < goodPerformanceRoundsThreshold ) {
			
			setSize = setSize < numberOfHideLocations ? setSize + 1 : numberOfHideLocations;
			
			Utils.talk(toString(), "Changing unique hide set size to: " + setSize);
			
		}
		
		ArrayList<StringVertex> uniqueNodes = createRandomSet(setSize, usedNodes);
		
		ArrayList<StringVertex> randomNodes = createRandomSet(numberOfHideLocations - setSize, new TreeSet<StringVertex>());
		
		uniqueNodes.addAll(randomNodes);
		
		populateHideSet(uniqueNodes);
		
	}

}
