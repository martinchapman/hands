package HideAndSeek.seeker.repeatgame.probability.adaptable;

import java.util.ArrayList;
import java.util.HashSet;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.AdaptiveSeekerStrategy;
import HideAndSeek.seeker.repeatgame.probability.HighProbability;
import Utility.AdaptiveUtils;

/**
 * @author Martin
 *
 */
public class HighProbabilityAdaptable extends HighProbability implements AdaptiveSeekerStrategy {

	/**
	 * 
	 */
	HashSet<StringVertex> uniqueHideLocations;
	
	/**
	 * 
	 */
	ArrayList<Integer> uniqueHideLocationsProgression;
	
	/**
	 * @param graphController
	 */
	public HighProbabilityAdaptable(GraphController<StringVertex, StringEdge> graphController) {
		
		super(graphController);
		
		uniqueHideLocations = new HashSet<StringVertex>();
		
		uniqueHideLocationsProgression = new ArrayList<Integer>();
		
		percentageChanges = new ArrayList<Double>();
		
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
		
		double relevance = 1 - ((uniqueHideLocations.size() / (double)(graphController.vertexSet().size())));
		
		final int ROUND_CONSISTENCY_THRESHOLD = 5;
		
		if ( uniqueHideLocationsProgression.size() >= ROUND_CONSISTENCY_THRESHOLD ) {
			
			int lastDifference = uniqueHideLocationsProgression.get( uniqueHideLocationsProgression.size() - ( ROUND_CONSISTENCY_THRESHOLD - 1) ) - 
								 uniqueHideLocationsProgression.get( uniqueHideLocationsProgression.size() - ( ROUND_CONSISTENCY_THRESHOLD) );
			
			for ( int i = uniqueHideLocationsProgression.size() - ( ROUND_CONSISTENCY_THRESHOLD - 2); i < uniqueHideLocationsProgression.size(); i++ ) {
				
				if ( uniqueHideLocationsProgression.get(i) - uniqueHideLocationsProgression.get(i - 1) != lastDifference ) {
					
					return relevance;
					
				}
				
			}
			
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

	ArrayList<Double> percentageChanges;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.AdaptiveSeekerStrategy#performanceOfSelf()
	 */
	@Override
	public double performanceOfSelf() {

		return AdaptiveUtils.performanceOfSelfUnderCostChange(graphController, this);
		
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

}
