package HideAndSeek.seeker.singleshot.nonhomo;

import java.util.Collections;
import java.util.List;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import Utility.Utils;

/**
 * Switches functionality of parent to head to most connected node
 * first.
 * 
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
	 * @see HideAndSeek.seeker.singleshot.LeastConnectedFirst#getConnectedEdges(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected List<StringEdge> getConnectedEdges(StringVertex currentNode) {
		
		List<StringEdge> connectedEdges = super.getConnectedEdges(currentNode);
		
		Collections.reverse(connectedEdges);
		
		return connectedEdges;
	
	}
	
}
