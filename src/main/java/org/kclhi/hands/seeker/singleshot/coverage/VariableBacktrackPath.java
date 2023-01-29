package org.kclhi.hands.seeker.singleshot.coverage;

import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
 * Extends the functionality of parent class to vary the maximum distance 
 * that the seeker is prepared to backtrack. 
 * 
 * @author Martin
 * @deprecated
 *
 */
public class VariableBacktrackPath extends BacktrackPath {

	/**
	 * @param graphController
	 */
	public VariableBacktrackPath(GraphController <StringVertex, StringEdge> graphController, String name, int maxbacktrackdistance) {
		
		super(graphController, name);
		
		MAXBACKTRACKDISTANCE = maxbacktrackdistance;
		
	}
	
	/**
	 * @param graphController
	 */
	public VariableBacktrackPath(GraphController <StringVertex, StringEdge> graphController, int maxbacktrackdistance) {
		
		this(graphController, "", maxbacktrackdistance);
		
	}
	
}
