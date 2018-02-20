package hideandseek.hider.repeatgame.bias;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Utility.Utils;
import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;
import hideandseek.hider.singleshot.cost.VariableGreedy;

/**
 * Same as parent except does not move to a new, randomised start
 * location between rounds (so, theoretically, saves on movement cost)
 * 
 * @author Martin
 */
public class VariableBiasStaticBetween extends VariableGreedy {

	/**
	 * @param graphController
	 * @param name
	 * @param numberOfHideLocations
	 * @param bias
	 */
	public VariableBiasStaticBetween(
			GraphController <StringVertex, StringEdge> graphController, String name,
			int numberOfHideLocations, double bias) {
		super(graphController, numberOfHideLocations, bias);
		
	}

	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 * @param bias
	 */
	public VariableBiasStaticBetween(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations, double bias) {
		this(graphController, "", numberOfHideLocations, bias);
		
	}
	

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	public StringVertex startNode() {

		return currentNode();
		
	}

}
