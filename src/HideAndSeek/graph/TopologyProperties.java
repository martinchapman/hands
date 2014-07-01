package HideAndSeek.graph;

import java.util.ArrayList;
import java.util.Collections;

import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * @author Martin
 *
 * @param <V>
 * @param <E>
 */
public class TopologyProperties<V, E extends DefaultWeightedEdge> {

	/**
	 * 
	 */
	private HiddenObjectGraph<V, E> graph;
	
	/**
	 * 
	 */
	private String type;
	
	/**
	 * @return
	 */
	public String getType() {
		
		return type;
	
	}

	/**
	 * 
	 */
	private ArrayList<NodeConnectionCount> nodeConnectivity;
	
	/**
	 * @param graph
	 */
	public TopologyProperties(String type, HiddenObjectGraph<V, E> graph) {
		
		this.graph = graph;
		
		nodeConnectivity = new ArrayList<NodeConnectionCount>();
		
		for ( V vertex : graph.vertexSet() ) {
			
			nodeConnectivity.add(new NodeConnectionCount(vertex, graph.edgesOf(vertex).size()));
			
		}
		
		Collections.sort(nodeConnectivity);
		
	}
	
	/**
	 * @return
	 */
	public int degreeOfMostConnectedNode() {
		
		return nodeConnectivity.get(0).getNodeConnections();
		
	}
	
	/**
	 * @return
	 */
	public int degreeOfLeastConnectedNode() {
		
		return nodeConnectivity.get(nodeConnectivity.size()).getNodeConnections();
		
	}
	
	/**
	 * @author Martin
	 *
	 */
	private class NodeConnectionCount implements Comparable<NodeConnectionCount> {
		
		/**
		 * 
		 */
		private V vertex;
		
		/**
		 * @return
		 */
		public V getVertex() {
			
			return vertex;
		
		}

		/**
		 * 
		 */
		private int nodeConnections;
		
		/**
		 * @return
		 */
		public int getNodeConnections() {
		
			return nodeConnections;
		
		}

		/**
		 * @param vertex
		 * @param nodeConnections
		 */
		public NodeConnectionCount(V vertex, int nodeConnections) {
			
			this.vertex = vertex;
			
			this.nodeConnections = nodeConnections;
			
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			
			return vertex + " " + nodeConnections;
			
		}

		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(NodeConnectionCount o) {
			
			if ( getNodeConnections() < o.getNodeConnections() ) {
				
				return -1;
				
			} else if ( getNodeConnections() > o.getNodeConnections() ) {
				
				return 1;
				
			} else {
			
				return 0;
			
			}
		
		}
		
	}
	
	
}
