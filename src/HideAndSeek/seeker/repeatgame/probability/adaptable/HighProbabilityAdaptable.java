package HideAndSeek.seeker.repeatgame.probability.adaptable;

import java.util.ArrayList;
import java.util.HashSet;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.AdaptiveSeeker;
import HideAndSeek.seeker.repeatgame.probability.HighProbability;
import Utility.AdaptiveUtils;
import Utility.ScoreMetric;
import Utility.Utils;

/**
 * @author Martin
 *
 */
public class HighProbabilityAdaptable extends HighProbability implements AdaptiveSeeker {

	/**
	 * 
	 */
	HashSet<StringVertex> uniqueHideLocations;
	
	/**
	 * 
	 */
	ArrayList<Integer> uniqueHideLocationsProgression;
	
	/**
	 * Denotes number of rounds for which the same number of 
	 * new locations can be recorded before it is clear that
	 * trend detection is not appropriate.
	 */
	private final int ROUND_CONSISTENCY_THRESHOLD = 3;
	
	/**
	 * Level at which individual performance is deemed poor.
	 */
	private final double INDIVIDUAL_PERFORMANCE_THRESHOLD = 0.5;
	
	/** 
	 * Denotes number of rounds for which performance can be poor before 
	 * a strategy change is indicated.
	 */
	private final int INDIVIDUAL_PERFORMANCE_ROUND_THRESHOLD = 5;
	
	/**
	 * @param graphController
	 */
	public HighProbabilityAdaptable(GraphController<StringVertex, StringEdge> graphController) {
		
		super(graphController);
		
		uniqueHideLocations = new HashSet<StringVertex>();
		
		uniqueHideLocationsProgression = new ArrayList<Integer>();
		
		percentageChanges = new ArrayList<Double>();
		
		this.name = "HighProbability";
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.repeatgame.probability.HighProbability#addHideLocation(HideAndSeek.graph.StringVertex)
	 */
	public void addHideLocation(StringVertex location) {
		
		super.addHideLocation(location);
		
		uniqueHideLocations.add(location);
		
	}
	
	/* 
	 * (non-Javadoc)
	 * @see HideAndSeek.AdaptiveGraphTraverserStrategy#relevanceOfStrategy()
	 * 
	 * The lower the number of *different* hide locations that have been used,
	 * the more relevant HighProbability is, as it suggests that patterns have
	 * emerged. Obviously, this is subject to sufficient experience, as low 
	 * number of different hide locations may simply be due to a limited time of
	 * play, as opposed to a trend in behaviour.
	 * 
	 * If the increments in hide location are consistently unique, instantly 
	 * indicate that a switch is necessary. In other words, if, for a given 
	 * number of rounds, the same number of new unique hiding locations are played, 
	 * assume that this indicates systematic unique play is being attempted, and thus
	 * this strategy is void.
	 */
	public double relevanceOfStrategy() {
		
		System.out.println("uniqueHideLocations.size() " + uniqueHideLocations.size());
		
		double relevance = 1 - ((uniqueHideLocations.size() / (double)(graphController.vertexSet().size())));
		
		if ( uniqueHideLocationsProgression.size() >= ROUND_CONSISTENCY_THRESHOLD ) {
			
			int lastDifference = uniqueHideLocationsProgression.get( uniqueHideLocationsProgression.size() - ( ROUND_CONSISTENCY_THRESHOLD - 1) ) - 
								 uniqueHideLocationsProgression.get( uniqueHideLocationsProgression.size() - ( ROUND_CONSISTENCY_THRESHOLD) );
			
			for ( int i = uniqueHideLocationsProgression.size() - ( ROUND_CONSISTENCY_THRESHOLD - 2); i < uniqueHideLocationsProgression.size(); i++ ) {
				
				if ( uniqueHideLocationsProgression.get(i) - uniqueHideLocationsProgression.get(i - 1) != lastDifference ) {
					
					return relevance;
					
				}
				
			}
			
			Utils.talk(this.toString(), "Increments are consistent, strategy change needed for this.");
			
			return 0.0;
			
		}
		
		return relevance;
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.AdaptiveSeekerStrategy#performanceOfOpponent()
	 */
	@Override
	public double performanceOfOpponent() {

		return 0.0;
		
	}

	/**
	 * 
	 */
	private ArrayList<Double> percentageChanges;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.AdaptiveSeekerStrategy#performanceOfSelf()
	 */
	@Override
	public double performanceOfSelf() {
		
		percentageChanges.add(graphController.latestTraverserRoundPerformance(responsibleAgent, ScoreMetric.COST_CHANGE_SCORE));
		
		if (AdaptiveUtils.containsLowPerformance(percentageChanges, INDIVIDUAL_PERFORMANCE_THRESHOLD, INDIVIDUAL_PERFORMANCE_ROUND_THRESHOLD)) {
			
			Utils.talk(responsibleAgent.toString(), "Consecutive low performance detected.");
			
			return 0.0;
			
		} else {
			
			return 1.0;
			
		}

	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.repeatgame.probability.HighProbability#endOfRound()
	 */
	public void endOfRound() {
		
		uniqueHideLocationsProgression.add(uniqueHideLocations.size());
	
		super.endOfRound();
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.repeatgame.probability.InverseHighProbability#endOfGame()
	 */
	@Override
	public void endOfGame() {
		
		uniqueHideLocations.clear();
		
		uniqueHideLocationsProgression.clear();
		
		super.endOfGame();
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.AdaptiveGraphTraverser#stopStrategy()
	 */
	@Override
	public void stopStrategy() {
		
		percentageChanges.clear();
		
	}

}
