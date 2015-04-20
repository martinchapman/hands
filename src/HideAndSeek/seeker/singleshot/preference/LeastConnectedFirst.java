package HideAndSeek.seeker.singleshot.preference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.SeekerLocalGraph;
import Utility.Utils;

/**
 * This strategy can be viewed as akin to a *BAYESIAN* approach, 
 * as it does not assume that each node has 1 / N chance of being
 * hidden in.
 * 
 * Instead it uses the PRIOR knowledge that this is a 
 * competitive game, in which hiders want to obscure themselves,
 * and thus they will express a higher preference towards more
 * obscure nodes.
 * 
 * 
 * @author Martin
 *
 */
public class LeastConnectedFirst extends SeekerLocalGraph {

	/**
	 * @param graph
	 */
	public LeastConnectedFirst(GraphController <StringVertex, StringEdge> graphController) {

		super(graphController);

	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#getConnectedEdge(HideAndSeek.graph.StringVertex, java.util.List)
	 */
	protected StringEdge getConnectedEdge(StringVertex currentNode, List<StringEdge> connectedEdges) {
		
		for (StringEdge edge : connectedEdges ) {

			if ( uniquelyVisitedNodes().contains(edgeToTarget(edge, currentNode)) ) continue;
			
			return edge;
			
		}
		
		return connectedEdges.get((int)(Math.random() * connectedEdges.size()));
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#getConnectedEdges(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected List<StringEdge> getConnectedEdges(StringVertex currentNode) {
		
		List<StringEdge> connections = new ArrayList<StringEdge>(super.getConnectedEdges(currentNode));
		
		ArrayList<ConnectedNodeConnectivity> nodeConnectivity = new ArrayList<ConnectedNodeConnectivity>();
		
		for ( StringEdge edge : connections ) {
			
			if ( localGraph.vertexSet().contains(edgeToTarget(edge, currentNode)) ) {
				
				nodeConnectivity.add(new ConnectedNodeConnectivity(edge, localGraph.edgesOf(edgeToTarget(edge, currentNode)).size()));
				
			} else {
				
				nodeConnectivity.add(new ConnectedNodeConnectivity(edge, 0));
				
			}
			
		}
		
		Collections.sort(nodeConnectivity);
		
		connections.clear();
		
		for ( ConnectedNodeConnectivity node : nodeConnectivity ) {
			
			connections.add(node.getEdge());
			
		}
		
		Utils.talk(toString(), "" + nodeConnectivity);
		
		return connections;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	public StringVertex startNode() {

		return randomNode();
		
	}

	@Override
	public StringVertex nextNode(StringVertex currentNode) {
		
		super.nextNode(currentNode);
		
		return connectedNode(currentNode);
		
	}
	
	/**
	 * Pairs edges with a figure denoting the number of nodes connected to the vertex
	 * at the end of that edge
	 * 
	 * @author Martin
	 */
	private class ConnectedNodeConnectivity implements Comparable<ConnectedNodeConnectivity> {
		
		private StringEdge edge;
		
		public StringEdge getEdge() {
			
			return edge;
		
		}

		private int connectionsAtNodeOnEndOfConnection;
		
		public int getConnectionsAtNodeOnEndOfConnection() {
		
			return connectionsAtNodeOnEndOfConnection;
		
		}

		public ConnectedNodeConnectivity(StringEdge edge, int connectionsAtNodeOnEndOfConnection) {
			
			this.edge = edge;
			
			this.connectionsAtNodeOnEndOfConnection = connectionsAtNodeOnEndOfConnection;
			
		}
		
		public String toString() {
			
			return edge + " " + connectionsAtNodeOnEndOfConnection;
			
		}

		@Override
		public int compareTo(ConnectedNodeConnectivity o) {
			
			if ( getConnectionsAtNodeOnEndOfConnection() < o.getConnectionsAtNodeOnEndOfConnection() ) {
				
				return -1;
				
			} else if ( getConnectionsAtNodeOnEndOfConnection() > o.getConnectionsAtNodeOnEndOfConnection() ) {
				
				return 1;
				
			} else {
			
				return 0;
			
			}
		
		}
		
	}

}
