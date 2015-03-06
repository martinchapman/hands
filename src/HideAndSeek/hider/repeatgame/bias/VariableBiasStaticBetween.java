package HideAndSeek.hider.repeatgame.bias;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.singleshot.cost.VariableLowEdgeCost;
import Utility.Utils;

/**
 * Same as parent except does not move to a new, randomised start
 * location between rounds (so, theoretically, saves on movement cost)
 * 
 * @author Martin
 */
public class VariableBiasStaticBetween extends VariableLowEdgeCost {

	public VariableBiasStaticBetween(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations, double bias) {
		super(graphController, numberOfHideLocations, bias);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	protected StringVertex startNode() {

		return getCurrentNode();
		
	}

}
