package HideAndSeek.hider.repeatgame.bias;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.singleshot.cost.VariableGreedy;

/**
 * @author Martin
 */
public class FixedStartVariableBias extends VariableBias {

	/**
	 * @param graphController
	 * @param name
	 * @param numberOfHideLocations
	 * @param bias
	 */
	public FixedStartVariableBias(
			GraphController <StringVertex, StringEdge> graphController, String name,
			int numberOfHideLocations, double bias) {
		super(graphController, name, numberOfHideLocations, bias);
		
	}

	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 * @param bias
	 */
	public FixedStartVariableBias(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations, double bias) {
		this(graphController, "", numberOfHideLocations, bias);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	public StringVertex startNode() {

		return firstNode();
		
	}

}
