package org.kclhi.hands;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.jgrapht.alg.DijkstraShortestPath;

import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.HiddenObjectGraph;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
 * This traverser constructs its own version of the search graph,
 * in an attempt to gain a more holistic view of the game environment.
 * 
 * Designed to be extended.
 * 
 * @author Martin
 *
 */
public abstract class TraverserLocalGraph extends GraphTraversingAgent {

	/**
	 * 
	 */
	protected HiddenObjectGraph<StringVertex, StringEdge> localGraph;
	
	/**
	 * @return
	 */
	public HiddenObjectGraph<StringVertex, StringEdge> getLocalGraph() {
		
		return localGraph;
		
	}

	/**
	 * @param localGraph
	 */
	public void setLocalGraph(HiddenObjectGraph<StringVertex, StringEdge> localGraph) {
		
		this.localGraph = localGraph;
	
	}

	/**
	 * 
	 */
	protected static final boolean KNOWS_VERTICES = true;
	
	/**
	 * @param graphController
	 */
	public TraverserLocalGraph(GraphController <StringVertex, StringEdge> graphController, GraphTraverser responsibleAgent) {
		
		this(graphController, "", responsibleAgent);
		
	}
	
	/**
	 * @param graphController
	 */
	public TraverserLocalGraph(GraphController <StringVertex, StringEdge> graphController, String name) {
		
		this(graphController, name, null);
		
	}
	
	/**
	 * @param graphController
	 */
	public TraverserLocalGraph(GraphController <StringVertex, StringEdge> graphController) {
		
		this(graphController, "", null);
		
	}
	
	/**
	 * @param graph
	 */
	public TraverserLocalGraph(GraphController <StringVertex, StringEdge> graphController, String name, GraphTraverser responsibleAgent) {
		
		super(graphController, name, responsibleAgent);
		
		localGraph = new HiddenObjectGraph<StringVertex, StringEdge>(StringEdge.class);
		
		for ( StringVertex vertex : graphController.vertexSet() ) {
			
			if ( KNOWS_VERTICES ) localGraph.addVertex(vertex);
			
		}
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 * 
	 * ~MDC: Needs tidying.
	 */
	@Override
	public void atNode() {
		
		super.atNode();
		
		addUniquelyVisitedNode(currentNode);
		
		// Update the local graph from the current node as the Seeker moves
		for ( StringEdge edge : getConnectedEdges(currentNode) ) {
			
			localGraph.addVertexIfNonExistent(edge.getSource());
			
			localGraph.addVertexIfNonExistent(edge.getTarget());
			
			localGraph.addEdgeIfNonExistent(edge.getSource(), edge.getTarget());
			
			localGraph.setEdgeWeight(localGraph.getEdge(edge.getSource(), edge.getTarget()), graphController.getEdgeWeight(edge));
			
		}
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#getStatus()
	 */
	protected String getStatus() {
		
		return super.getStatus() + "\nKnown edges: " + localGraph.edgeSet().size();
		
	}

	/**
	 * @param currentNode
	 * @param numberOfNodes Number of nodes to return ordered.
	 * @param potentialNodes
	 * @return
	 */
	protected ArrayList<StringVertex> orderNodesByProximity(StringVertex currentNode, int numberOfNodes, ArrayList<StringVertex> potentialNodes) {
		
		TreeMap<Integer, ArrayList<StringVertex>> distanceAndNodes = new TreeMap<Integer, ArrayList<StringVertex>>();
		
		ArrayList<StringVertex> unknownNodes = new ArrayList<StringVertex>();
		
		if ( numberOfNodes > potentialNodes.size() ) numberOfNodes = potentialNodes.size();
		
		List<StringVertex> subsetPotentialNodes = potentialNodes.subList(0, numberOfNodes);
		
		for ( StringVertex targetNode : subsetPotentialNodes ) {
			
			if ( !localGraph.containsVertex(currentNode) || !localGraph.containsVertex(targetNode) ) { 
				
				unknownNodes.add(targetNode);
				
				continue; 
				
			}
			
			DijkstraShortestPath<StringVertex, StringEdge> dsp = new DijkstraShortestPath<StringVertex, StringEdge>( localGraph, currentNode, targetNode );
	    	
			// If no path available, return random connected node
			if ( dsp.getPathEdgeList() == null || dsp.getPathEdgeList().size() == 0 ) { 
				
				unknownNodes.add(targetNode);
				
				continue;
			
			} else {
				
				if (distanceAndNodes.containsKey(dsp.getPathEdgeList().size())) {
					
					distanceAndNodes.get(dsp.getPathEdgeList().size()).add(targetNode); 
				
				} else {
				
					ArrayList<StringVertex> nodes = new ArrayList<StringVertex>();
					
					nodes.add(targetNode);
					
					distanceAndNodes.put(dsp.getPathEdgeList().size(), nodes);
					
				}
				
			}
		
		}
		
		ArrayList<StringVertex> orderedByProximity = new ArrayList<StringVertex>();
		
		for ( ArrayList<StringVertex> vertices : distanceAndNodes.values()) {
			
			orderedByProximity.addAll(vertices);
			
		}
		
		orderedByProximity.addAll(unknownNodes);
		
		return orderedByProximity;
		
	}
	
}
