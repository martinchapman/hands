package HideAndSeek.seeker.singleshot.preference;

import java.util.LinkedHashSet;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.singleshot.preference.MaxDistanceMechansim;

/**
 * @author Martin
 *
 */
public class MaxDistance extends PreferenceSeeker {

	/**
	 * 
	 */
	private MaxDistanceMechansim maxDistanceMechanism;
	
	/**
	 * @param graphController
	 */
	public MaxDistance(GraphController<StringVertex, StringEdge> graphController, double graphPortion) {
		
		this(graphController, "", graphPortion);
		
	}
	
	/**
	 * @param graphController
	 */
	public MaxDistance(GraphController<StringVertex, StringEdge> graphController, String name, double graphPortion) {
		
		super(graphController, name, graphPortion);
		
		maxDistanceMechanism = new MaxDistanceMechansim(graphController, name, graphController.numberOfHideLocations(), graphPortion, -1);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.singleshot.preference.PreferenceSeeker#computeTargetNodes()
	 */
	@Override
	public LinkedHashSet<StringVertex> computeTargetNodes() {

		LinkedHashSet<StringVertex> targetNodes = maxDistanceMechanism.computeTargetNodes(localGraph);
		
		return maxDistanceMechanism.computeTargetNodes(localGraph);

	}

}
