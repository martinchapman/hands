package org.kclhi.hands.seeker.repeatgame.probability.adaptable;

import java.util.ArrayList;

import org.kclhi.hands.utility.Metric;
import org.kclhi.hands.utility.Utils;
import org.kclhi.hands.utility.adaptive.AdaptiveMeasure;
import org.kclhi.hands.utility.adaptive.AdaptiveUtils;
import org.kclhi.hands.utility.adaptive.AdaptiveWeightings;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.seeker.AdaptiveSeeker;
import org.kclhi.hands.seeker.repeatgame.probability.VariableNodesInverseHighProbability;

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
	 * @see org.kclhi.hands.seeker.repeatgame.probability.HighProbability#addHideLocation(HideAndSeek.graph.StringVertex)
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
	public AdaptiveMeasure environmentalMeasure() {
	
		return new AdaptiveMeasure(0.0);
		
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
	public AdaptiveMeasure socialMeasure() {

		return new AdaptiveMeasure(regularIncrements(), "HighProbabilityAdaptable");
		
	}
	
	/**
	 * @return
	 */
	private double regularIncrements() {
		
		if ( uniqueHideLocationsProgression.size() > 2 ) {

			Utils.talk(toString(), "uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 1) " + uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 1));
			Utils.talk(toString(), "uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 2) " + uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 2));
			Utils.talk(toString(), "uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 3) " + uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 3));
			
			if ( ( uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 1) -  uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 2) ) 
			   != (  uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 2) - uniqueHideLocationsProgression.get(uniqueHideLocationsProgression.size() - 3) ) )  
			   return 1.0;
		
		}
		
		return 0.0;
		
	}
	
	/**
	 * NB: Not a reliable metric, as when confidence level is high enough, 
	 * number of remaining unique nodes is too low.
	 * @return
	 */
	private double uniqueLocationsSize() {
		
		Utils.talk(toString(), "1 - (uniqueHideLocations().size() / (double)(graphController.vertexSet().size())) " + (1 - (uniqueHideLocations().size() / (double)(graphController.vertexSet().size()))));
		
		return 1 - (uniqueHideLocations().size() / (double)(graphController.vertexSet().size())); 
		
	}
	
	/* (non-Javadoc)
	 * @see org.kclhi.hands.seeker.repeatgame.probability.InverseHighProbability#endOfRound()
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

	/**
	 * @param roundStrategyPerformance
	 * @return
	 */
	public AdaptiveMeasure internalMeasure(ArrayList<Double> roundStrategyPerformance) {
		
		return new AdaptiveMeasure(AdaptiveUtils.internalMeasure(roundStrategyPerformance, Metric.TOTAL_EDGE_COST, localGraph));
		
	}

	/**
	 * @deprecated
	 * @return
	 */
	public double internalMeasure_Org() {

		percentageChanges.add(graphController.latestTraverserRoundPerformance(responsibleAgent, Metric.COST_CHANGE_PAYOFF));
		
		if (AdaptiveUtils.containsLowPerformance(percentageChanges)) {
			
			Utils.talk(toString(), "Consecutive low performance detected.");
			
			return 0.0;
			
		} else {
			
			return 1.0;
			
		}
		
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
