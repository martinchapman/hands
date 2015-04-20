package HideAndSeek.seeker;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.jgrapht.alg.DijkstraShortestPath;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;

/**
 * This seeker constructs its own version of the search graph,
 * in an attempt to gain a more holistic view of the game environment.
 * 
 * Designed to be extended.
 * 
 * @author Martin
 *
 */
public abstract class SeekerLocalGraph extends SeekingAgent {

	/**
	 * 
	 */
	protected HiddenObjectGraph<StringVertex, StringEdge> localGraph;
	
	/**
	 * @param graph
	 */
	public SeekerLocalGraph(GraphController <StringVertex, StringEdge> graphController) {
		
		super(graphController);
		
		localGraph = new HiddenObjectGraph<StringVertex, StringEdge>(StringEdge.class);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 * 
	 * ~MDC: Needs tidying.
	 */
	@Override
	public StringVertex nextNode(StringVertex currentNode) {
		
		addUniquelyVisitedNode(currentNode);
		
		// Update the local graph from the current node as the Seeker moves
		for ( StringEdge edge : getConnectedEdges(currentNode) ) {
			
			localGraph.addVertexIfNonExistent(edge.getSource());
			
			localGraph.addVertexIfNonExistent(edge.getTarget());
			
			localGraph.addEdgeIfNonExistent(edge.getSource(), edge.getTarget());
			
			localGraph.setEdgeWeight(localGraph.getEdge(edge.getSource(), edge.getTarget()), graphController.getEdgeWeight(edge));
			
		}
		
		return currentNode;
		
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
