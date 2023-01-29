package org.kclhi.hands.seeker.hamiltonain.adaptable;

import java.util.ArrayList;

import org.jgrapht.alg.HamiltonianCycle;

import org.kclhi.hands.utility.adaptive.AdaptiveMeasure;
import org.kclhi.hands.utility.adaptive.AdaptiveWeightings;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.seeker.AdaptiveSeeker;
import org.kclhi.hands.seeker.hamiltonian.HamiltonianTourer;

/**
 * Allows for a strategy to communicate to a parent adaptive agent whether
 * the graph contains an Hamiltonian circuit, and thus whether this strategy
 * can work. 
 * 
 * @author Martin
 * @deprecated
 *
 */
public class HamiltonianTourerAdaptable extends HamiltonianTourer implements AdaptiveSeeker {

	/**
	 * @param graphController
	 */
	public HamiltonianTourerAdaptable(GraphController<StringVertex, StringEdge> graphController) {
		
		super(graphController);
	
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.AdaptiveGraphTraverser#getAdaptiveWeightings()
	 */
	@Override
	public AdaptiveWeightings getAdaptiveWeightings() {

		return null;
	
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.AdaptiveGraphTraverser#relevanceOfStrategy()
	 */
	@Override
	public AdaptiveMeasure environmentalMeasure() {
		
		if ( HamiltonianCycle.getApproximateOptimalForCompleteGraph(localGraph).size() == graphController.vertexSet().size() ) {
			
			return new AdaptiveMeasure(1.0);
			
		} else {
			
			return new AdaptiveMeasure(0.0);
			
		}
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.AdaptiveGraphTraverser#performanceOfOpponent()
	 */
	@Override
	public AdaptiveMeasure socialMeasure() {
		
		return new AdaptiveMeasure(0.0);
	
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.AdaptiveGraphTraverser#internalMeasure(java.util.ArrayList)
	 */
	@Override
	public AdaptiveMeasure internalMeasure(ArrayList<Double> roundStrategyPerformance) {

		return new AdaptiveMeasure(0.0);

	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.AdaptiveGraphTraverser#stopStrategy()
	 */
	@Override
	public void stopStrategy() {}

}
