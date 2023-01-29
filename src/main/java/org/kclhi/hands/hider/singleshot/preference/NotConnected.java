package org.kclhi.hands.hider.singleshot.preference;

import org.kclhi.hands.utility.Utils;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.hider.HidingAgent;

public class NotConnected extends HidingAgent {

	public NotConnected( GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations) {
		
		super(graphController, numberOfHideLocations);

	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#hideHere(HideAndSeek.graph.StringVertex)
	 */
	public boolean hideHere(StringVertex vertex) {
		
		if ( uniquelyVisitedNodes().size() == graphController.vertexSet().size() ) return true;
		
		if ( currentNode == vertex ) {
			
			for ( StringVertex hideLocation : hideLocations() ) {
				
				for ( StringEdge edge : graphController.edgesOf(responsibleAgent, vertex) ) {
					
					if ( edgeToTarget(edge, vertex).equals(hideLocation) ) {
						
						return false;
						
					}
					
				}
				
			}
		
			return true;
			
		} else {
			
			return false;
			
		}
		
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public StringVertex nextNode(StringVertex currentNode) {
		
		return connectedNode(currentNode);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#startNode()
	 */
	@Override
	public StringVertex startNode() {
		
		return randomNode();
		
	}

}
