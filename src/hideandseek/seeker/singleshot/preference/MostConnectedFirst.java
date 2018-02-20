package hideandseek.seeker.singleshot.preference;

import java.util.Collections;
import java.util.List;

import Utility.Utils;
import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;

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
	 * @see HideAndSeek.seeker.singleshot.LeastConnectedFirst#getConnectedEdges(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected List<StringEdge> getConnectedEdges(StringVertex currentNode) {
		
		List<StringEdge> connectedEdges = super.getConnectedEdges(currentNode);
		
		Collections.reverse(connectedEdges);
		
		return connectedEdges;
	
	}
	
}
