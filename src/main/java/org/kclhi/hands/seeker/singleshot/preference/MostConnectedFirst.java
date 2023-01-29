package org.kclhi.hands.seeker.singleshot.preference;

import java.util.Collections;
import java.util.List;

import org.kclhi.hands.utility.Utils;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
 * Switches functionality of parent to head to most connected node
 * first.
 * 
 * @deprecated Not viable with graph exploration
 * @author Martin
 *
 */
public class MostConnectedFirst extends LeastConnectedFirst {

	/**
	 * @param graphController
	 */
	public MostConnectedFirst( GraphController<StringVertex, StringEdge> graphController ) {
		
		super(graphController);
	
	}

	/* (non-Javadoc)
	 * @see org.kclhi.hands.seeker.singleshot.LeastConnectedFirst#getConnectedEdges(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected List<StringEdge> getConnectedEdges(StringVertex currentNode) {
		
		List<StringEdge> connectedEdges = super.getConnectedEdges(currentNode);
		
		Collections.reverse(connectedEdges);
		
		return connectedEdges;
	
	}
	
}
