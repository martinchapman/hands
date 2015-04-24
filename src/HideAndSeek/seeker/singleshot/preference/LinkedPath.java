package HideAndSeek.seeker.singleshot.preference;

import java.util.List;

import HideAndSeek.OpenTraverserStrategy;
import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.SeekerLocalGraph;

/**
 * @author Martin
 *
 */
public class LinkedPath extends SeekerLocalGraph {

	/**
	 * 
	 */
	private int currentNodeGap = 0;
	
	/**
	 * 
	 */
	private int specifiedNodeGap;
	
	/**
	 * 
	 */
	OpenTraverserStrategy explorationMechanism;
	
	/**
	 * @param graphController
	 * @param specifiedNodeGap
	 */
	public LinkedPath(GraphController<StringVertex, StringEdge> graphController) {
		
		this(graphController, 1);
		
	}

	/**
	 * @param graphController
	 */
	public LinkedPath(GraphController<StringVertex, StringEdge> graphController, int specifiedNodeGap) {
		
		super(graphController);
		
		explorationMechanism = new MostConnectedFirstMechanism(graphController);
		
		this.specifiedNodeGap = specifiedNodeGap;
		
	}
	
	/**
	 * @param startNode
	 */
	protected void atStart(StringVertex startNode) {
		
		super.atStart(startNode);
		
		explorationMechanism.atStart(currentNode);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#atNode()
	 */
	public void atNode() {
		
		super.atNode();
		
		explorationMechanism.atNode();
		
	}
	
	public void atNextNode(StringVertex nextNode) {
		
		super.atNextNode(nextNode);
		
		explorationMechanism.atNextNode(nextNode);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public StringVertex nextNode(StringVertex currentNode) {
		
		super.nextNode(currentNode);
		
		// If we are still exploring, 
		if ( super.hideLocations().size() == 0 ) {
			
			return connectedNode(currentNode);
			
		} else {
			
			/* 
			 * If we there are not the required number
			 * of nodes between the current location, 
			 * and the last known hide position, increment
			 * the gap by choosing a random unvisited node
			 */
			if ( currentNodeGap < specifiedNodeGap ) {
				
				currentNodeGap++;
				
				return connectedNode(currentNode);
			
			/* 
			 * If we now have an appropriate gap, look for the next hide location.
			 * If found, move to it. (~MDC 24/4 will not work with new constraints).
			 */
			} else {
			
				for ( StringEdge connectedEdge : getConnectedEdges(currentNode) ) {
					
					if ( graphController.isHideLocation(responsibleAgent, edgeToTarget(connectedEdge, currentNode)) && !hideLocations().contains(edgeToTarget(connectedEdge, currentNode)) ) {
						
						return edgeToTarget(connectedEdge, currentNode);
						
					}
					
				}
			
			}
			
			/* 
			 * If no hide location is found, decrement the gap, so we can increment it again
			 * and try with a new and unvisited node.
			 */
			currentNodeGap = currentNodeGap > 0 ? currentNodeGap - 1 : 0;
			
		}
		
		return connectedNode(currentNode);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#startNode()
	 */
	@Override
	public StringVertex startNode() {

		return randomNode();
	
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#getConnectedEdges(HideAndSeek.graph.StringVertex)
	 */
	public List<StringEdge> getConnectedEdges(StringVertex currentNode) {
		
		return explorationMechanism.getConnectedEdges(currentNode);
	
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#getConnectedEdge(HideAndSeek.graph.StringVertex, java.util.List)
	 */
	public StringEdge getConnectedEdge(StringVertex currentNode, List<StringEdge> connectedEdges) {
		
		return explorationMechanism.getConnectedEdge(currentNode, connectedEdges);
	
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#connectedNode(HideAndSeek.graph.StringVertex)
	 */
	public StringVertex connectedNode(StringVertex currentNode) {
		
		return explorationMechanism.connectedNode(currentNode);
	
	}

}