package HideAndSeek.seeker.repeatgame.probability.adaptable;

import java.util.ArrayList;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.AdaptiveSeeker;
import HideAndSeek.seeker.repeatgame.probability.VariableNodesInverseHighProbability;
import Utility.Metric;
import Utility.Utils;
import Utility.adaptive.AdaptiveUtils;
import Utility.adaptive.AdaptiveWeightings;

/**
 * Augmented to provide feedback on its own performance, such that
 * it may be changed by a parent strategy.
 * 
 * @author Martin
 *
 */
public class InverseHighProbabilityAdaptable extends VariableNodesInverseHighProbability implements AdaptiveSeeker {
 
	/**
	 * 
	 */
	private ArrayList<Double> percentageChanges;
	
	/**
	 * 
	 */
	ArrayList<Integer> uniqueHideLocationsProgression;
	
	/**
	 * @param graphController
	 */
	public InverseHighProbabilityAdaptable(GraphController<StringVertex, StringEdge> graphController, String name, int predictiveNodes) {
		
		super(graphController, name, predictiveNodes);
		
		percentageChanges = new ArrayList<Double>();
		
		uniqueHideLocationsProgression = new ArrayList<Integer>();
		
	}

	
	/**
	 * @param graphController
	 */
	public InverseHighProbabilityAdaptable(GraphController<StringVertex, StringEdge> graphController, int predictiveNodes) {
		
		this(graphController, "", predictiveNodes);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.repeatgame.probability.HighProbability#addHideLocation(HideAndSeek.graph.StringVertex)
	 */
	public void addHideLocation(StringVertex location) {
		
		super.addHideLocation(location);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.AdaptiveGraphTraverser#getAdaptiveWeightings()
	 */
	public AdaptiveWeightings getAdaptiveWeightings() {
		
		return new AdaptiveWeightings(0.0, 1.0, 0.0);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.AdaptiveGraphTraverser#environmentalMeasure()
	 */
	@Override
	public double environmentalMeasure() {
	
		return 0.0;
		
	}


	/**
	 * The smaller the number of hide locations that remain, 
	 * the more likely that it is that a Hider is attempting
	 * to visit unique nodes. Therefore, we model the suitability
	 * of the strategy on this value.
	 * 
	 * 
	 * @return
	 */
	@Override
	public double socialMeasure() {

		if ( uniqueHideLocationsProgression.size() > 2 ) {

			Utils.talk(toString(), "uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 1) " + uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 1));
			Utils.talk(toString(), "uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 2) " + uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 2));
			Utils.talk(toString(), "uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 3) " + uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 3));
			
			if ( ( uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 1) -  uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 2) ) 
			   != (  uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 2) - uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 3) ) )  
			   return 1.0;
		
		}
		
		return 0.0;
		
		//return 1 - (uniqueHideLocations.size() / (double)(graphController.vertexSet().size())); 
		
	}

	/**
	 * @param roundStrategyPerformance
	 * @return
	 */
	public double internalMeasure(ArrayList<Double> roundStrategyPerformance) {
		
		return AdaptiveUtils.internalMeasure(roundStrategyPerformance, Metric.TOTAL_EDGE_COST, localGraph);
		
	}

	/**
	 * @deprecated
	 * @return
	 */
	public double internalMeasure_Org() {

		percentageChanges.add(graphController.latestTraverserRoundPerformance(responsibleAgent, Metric.COST_CHANGE_SCORE));
		
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
	
		if ( uniqueHideLocations().size() > ( graphController.vertexSet().size() - estimatedNumberOfHideLocations() ) ) { 
			
			uniqueHideLocations().clear();
			
			uniqueHideLocationsProgression.clear();
			
		}
		
		super.endOfRound();
		
		Utils.talk(toString(), "uniqueHideLocations.size() " + uniqueHideLocations().size());
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.repeatgame.probability.InverseHighProbability#endOfGame()
	 */
	@Override
	public void endOfGame() {
		
		super.endOfGame();
		
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


	/* (non-Javadoc)
	 * @see HideAndSeek.AdaptiveGraphTraverser#stopStrategy()
	 */
	@Override
	public void stopStrategy() {
		
		percentageChanges.clear();
		
		uniqueHideLocationsProgression.clear();
		
	}
	
}
