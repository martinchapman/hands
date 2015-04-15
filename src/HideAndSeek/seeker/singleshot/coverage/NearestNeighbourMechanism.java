package HideAndSeek.seeker.singleshot.coverage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;

import HideAndSeek.OpenTraverserStrategy;
import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;


/**
 * @author Martin
 *
 */
public class NearestNeighbourMechanism extends NearestNeighbour implements OpenTraverserStrategy {

	public NearestNeighbourMechanism(GraphController<StringVertex, StringEdge> graphController) {
		
		super(graphController);

	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.singleshot.coverage.NearestNeighbour#nextNode(HideAndSeek.graph.StringVertex)
	 */
	public StringVertex nextNode(StringVertex currentNode) {
		
		return super.nextNode(currentNode);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#connectedNode(HideAndSeek.graph.StringVertex)
	 */
	public StringVertex connectedNode(StringVertex currentNode) {
		
		return super.connectedNode(currentNode);
	
	}
	
}