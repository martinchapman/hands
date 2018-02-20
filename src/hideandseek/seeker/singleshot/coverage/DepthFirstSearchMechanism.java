package hideandseek.seeker.singleshot.coverage;

import java.util.List;

import hideandseek.GraphTraverser;
import hideandseek.OpenTraverserStrategy;
import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;

/**
 * @author Martin
 *
 */
public class DepthFirstSearchMechanism extends DepthFirstSearch implements OpenTraverserStrategy {

	/**
	 * @param graphController
	 * @param responsibleAgent
	 */
	public DepthFirstSearchMechanism( GraphController<StringVertex, StringEdge> graphController, GraphTraverser responsibleAgent ) {
		
		super(graphController);

	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.singleshot.coverage.NearestNeighbour#nextNode(HideAndSeek.graph.StringVertex)
	 */
	public StringVertex nextNode(StringVertex currentNode) {
		
		this.currentNode = currentNode;
		
		return super.nextNode(currentNode);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#connectedNode(HideAndSeek.graph.StringVertex)
	 */
	public StringVertex connectedNode(StringVertex currentNode) {
		
		return super.connectedNode(currentNode);
	
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.singleshot.LeastConnectedFirst#getConnectedEdges(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public List<StringEdge> getConnectedEdges(StringVertex currentNode) {
		
		return super.getConnectedEdges(currentNode);
	
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.singleshot.preference.LeastConnectedFirst#getConnectedEdge(HideAndSeek.graph.StringVertex, java.util.List)
	 */
	@Override
	public StringEdge getConnectedEdge(StringVertex currentNode, List<StringEdge> connectedEdges) {
		
		return super.getConnectedEdge(currentNode, connectedEdges);
	
	}
	
	public void atStart(StringVertex startNode) {
		
		super.atStart(startNode);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.SeekingAgent#atNode()
	 */
	public void atNode() {
		
		super.atNode();
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#nextNodeAccepted(HideAndSeek.graph.StringVertex)
	 */
	public void atNextNode(StringVertex nextNode) {
		
		super.atNextNode(nextNode);
		
	}

}
