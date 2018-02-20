package hideandseek.hider.singleshot.preference;

import java.util.LinkedHashSet;

import hideandseek.GraphTraverser;
import hideandseek.graph.GraphController;
import hideandseek.graph.HiddenObjectGraph;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;

/**
 * @author Martin
 *
 */
public class MaxDistanceMechansim extends MaxDistance implements OpenPreferenceHiderStrategy {

	public MaxDistanceMechansim(
			GraphController<StringVertex, StringEdge> graphController,
			int numberOfHideLocations, double graphPortion, int minDistance) {
		super(graphController, numberOfHideLocations, graphPortion, minDistance);
		// TODO Auto-generated constructor stub
	}

	public MaxDistanceMechansim(
			GraphController<StringVertex, StringEdge> graphController,
			int numberOfHideLocations, GraphTraverser responsibleAgent) {
		super(graphController, numberOfHideLocations, responsibleAgent);
		// TODO Auto-generated constructor stub
	}

	public MaxDistanceMechansim(
			GraphController<StringVertex, StringEdge> graphController,
			int numberOfHideLocations) {
		super(graphController, numberOfHideLocations);
		// TODO Auto-generated constructor stub
	}

	public MaxDistanceMechansim(
			GraphController<StringVertex, StringEdge> graphController,
			String name, int numberOfHideLocations, double graphPortion,
			int minDistance, GraphTraverser responsibleAgent) {
		super(graphController, name, numberOfHideLocations, graphPortion, minDistance,
				responsibleAgent);
		// TODO Auto-generated constructor stub
	}

	public MaxDistanceMechansim(
			GraphController<StringVertex, StringEdge> graphController,
			String name, int numberOfHideLocations, double graphPortion,
			int minDistance) {
		super(graphController, name, numberOfHideLocations, graphPortion, minDistance);
		// TODO Auto-generated constructor stub
	}

	public MaxDistanceMechansim(
			GraphController<StringVertex, StringEdge> graphController,
			String name, int numberOfHideLocations,
			GraphTraverser responsibleAgent) {
		super(graphController, name, numberOfHideLocations, responsibleAgent);
		// TODO Auto-generated constructor stub
	}

	public MaxDistanceMechansim(
			GraphController<StringVertex, StringEdge> graphController,
			String name, int numberOfHideLocations) {
		super(graphController, name, numberOfHideLocations);
		// TODO Auto-generated constructor stub
	}

	/**/
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.singleshot.preference.PreferenceHider#hideHere(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public boolean hideHere(StringVertex vertex) {

		return super.hideHere(vertex);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.singleshot.preference.OpenPreferenceHiderStrategy#computeTargetNodes(HideAndSeek.graph.HiddenObjectGraph)
	 */
	@Override
	public LinkedHashSet<StringVertex> computeTargetNodes(HiddenObjectGraph<StringVertex, StringEdge> localGraph) {
		
		setLocalGraph(localGraph);
		
		return super.computeTargetNodes();
		
	}

}
