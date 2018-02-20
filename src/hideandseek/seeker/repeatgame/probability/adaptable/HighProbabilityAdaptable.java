package hideandseek.seeker.repeatgame.probability.adaptable;

import java.util.ArrayList;

import Utility.Metric;
import Utility.Utils;
import Utility.adaptive.AdaptiveMeasure;
import Utility.adaptive.AdaptiveUtils;
import Utility.adaptive.AdaptiveWeightings;
import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;
import hideandseek.seeker.AdaptiveSeeker;
import hideandseek.seeker.repeatgame.probability.HighProbability;

/**
 * @author Martin
 *
 */
public class HighProbabilityAdaptable extends HighProbability implements AdaptiveSeeker {

	/**
	 * 
	 */
	ArrayList<Integer> uniqueHideLocationsProgression;

	/**
	 * 
	 */
	private ArrayList<Double> percentageChanges;
	
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
		
		this(graphController, "");
		
	}
	
	/**
	 * @param graphController
	 */
	public HighProbabilityAdaptable(GraphController<StringVertex, StringEdge> graphController, String name) {
		
		super(graphController, name);
		
		uniqueHideLocationsProgression = new ArrayList<Integer>();
		
		percentageChanges = new ArrayList<Double>();
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.repeatgame.probability.HighProbability#addHideLocation(HideAndSeek.graph.StringVertex)
	 */
	public void addHideLocation(StringVertex location) {
		
		super.addHideLocation(location);
		
		uniqueHideLocations().add(location);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.AdaptiveGraphTraverser#getAdaptiveWeightings()
	 */
	@Override
	public AdaptiveWeightings getAdaptiveWeightings() {
		
		return new AdaptiveWeightings(0.0, 1.0, 0.0);
	
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.AdaptiveGraphTraverser#environmentalMeasure()
	 */
	public AdaptiveMeasure environmentalMeasure() {
		
		return new AdaptiveMeasure(0.0);
		
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
	@Override
	public AdaptiveMeasure socialMeasure() {
		
		return new AdaptiveMeasure(regularIncrements(), "InverseHighProbabilityAdaptable");
		
	}
	
	/**
	 * @return
	 */
	private double regularIncrements() {
		
		if ( uniqueHideLocationsProgression.size() > 2 ) {
			
			Utils.talk(toString(), "uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 1) " + uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 1));
			Utils.talk(toString(), "uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 2) " + uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 2));
			Utils.talk(toString(), "uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 3) " + uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 3));
			
			Utils.talk(toString(), "( uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 1) -  uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 2) ) " + ( uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 1) -  uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 2) ) );
			Utils.talk(toString(), "estimatedHideLocations " + estimatedNumberOfHideLocations());
			
			if ( ( uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 1) -  uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 2) ) 
			   == (  uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 2) - uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 3) ) &&
			   ( uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 1) -  uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 2) ) 
			   == estimatedNumberOfHideLocations() 
			   //|| uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 1) == graphController.vertexSet().size()
			   )  {
			    
				Utils.talk(toString(), "Social cue adapting!");
				return 1.0;
			   
			}
		
		}
		
		return 0.0;
		
	}
	
	/**
	 * @return
	 */
	private double uniqueLocationsSize() {
		
		Utils.talk(toString(), "uniqueHideLocations().size() / (double)(graphController.vertexSet().size()) " + (uniqueHideLocations().size() / (double)(graphController.vertexSet().size())));
		
		return uniqueHideLocations().size() / (double)(graphController.vertexSet().size()); 
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.AdaptiveSeekerStrategy#performanceOfSelf()
	 */
	@Override
	public AdaptiveMeasure internalMeasure(ArrayList<Double> roundStrategyPerformance) {
		
		return new AdaptiveMeasure(AdaptiveUtils.internalMeasure(roundStrategyPerformance, Metric.TOTAL_EDGE_COST, localGraph));

	}

	public double internalMeasure_Org() {

		percentageChanges.add(graphController.latestTraverserRoundPerformance(responsibleAgent, Metric.COST_CHANGE_PAYOFF));
		
		if (AdaptiveUtils.containsLowPerformance(percentageChanges)) {
			
			Utils.talk(toString(), "Consecutive low performance detected.");
			
			return 0.0;
			
		} else {
			
			return 1.0;
			
		}
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.repeatgame.probability.HighProbability#endOfRound()
	 */
	public void endOfRound() {
		
		uniqueHideLocationsProgression.add(uniqueHideLocations().size());
	
		// From IHP:
		// if (allHideLocations().size() >= ( graphController.vertexSet().size() )) uniqueHideLocations().clear();
				
		// Was: uniqueHideLocations();
		if ( allHideLocations().size() > ( graphController.vertexSet().size() - estimatedNumberOfHideLocations() ) ) { 
			
			allHideLocations().clear();
			
			uniqueHideLocationsProgression.clear();
		
		}
		
		super.endOfRound();
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.repeatgame.probability.InverseHighProbability#endOfGame()
	 */
	@Override
	public void endOfGame() {
		
		uniqueHideLocationsProgression.clear();
		
		super.endOfGame();
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.AdaptiveGraphTraverser#stopStrategy()
	 */
	@Override
	public void stopStrategy() {
		
		percentageChanges.clear();
		
		uniqueHideLocationsProgression.clear();
		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((getName() == null) ? 0 : getName()
						.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		InverseHighProbabilityAdaptable other = (InverseHighProbabilityAdaptable) obj;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		return true;
	}

}
