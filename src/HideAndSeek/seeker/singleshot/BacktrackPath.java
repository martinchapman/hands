package HideAndSeek.seeker.singleshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.SeekerLocalGraph;

/**
 * @author Martin
 *
 */
public class BacktrackPath extends SeekerLocalGraph {

	/**
	 * @param graphController
	 */
	public BacktrackPath(GraphController <StringVertex, StringEdge> graphController) {
		
		super(graphController);
		
		uniquelyVisitedEdges = new HashSet<StringEdge>();
		
	}

	/**
	 * 
	 */
	protected HashSet<StringEdge> uniquelyVisitedEdges;
	
	/**
	 * 
	 */
	protected List<StringEdge> pathInProgress;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected StringVertex nextNode(StringVertex currentNode) {
		
		super.nextNode(currentNode);
		
		// If we are currently on a path back to a cheaper node, do this first:
		if ( pathInProgress.size() > 0) return edgeToTarget(pathInProgress.remove(0), currentNode);
		
		// Otherwise, get all outgoing edges from this node
		List<StringEdge> connectedEdges = getConnectedEdges(currentNode);
		
		/* If there are any previously visited edges (logged in our local graph) that have
		   less weight than ALL edges going out from our current node and have not been 
		   visited, log them */
		
		ArrayList<StringEdge> cheaperUnvisitedEdges = new ArrayList<StringEdge>();
		
		Boolean cheaper = false;
		
		for ( StringEdge graphEdge : localGraph.edgeSet() ) {
			
			// Test this edge against all current outgoing edges: is it smaller than all of them?
			for ( StringEdge currentEdge : connectedEdges ) {
			
				// It is greater than one of them, so it is not smaller than all of them, continue
				if ( graphController.getEdgeWeight(currentEdge) > localGraph.getEdgeWeight(graphEdge) ) continue;
				
			}
		
			// Reached here, so must be smaller than all. Only add if not already visited
			if ( uniquelyVisitedEdges.contains(graphEdge) ) cheaperUnvisitedEdges.add(graphEdge);
			
		}
		
		// If a cheaper edge(s) is found
		if (cheaperUnvisitedEdges.size() > 0) {
		
			// Sort the edges (if there are multiple, cheapest first)
			Collections.sort(cheaperUnvisitedEdges);
			
			// Create a path to the target of the cheapest edge
			DijkstraShortestPath<StringVertex, StringEdge> DSP = new DijkstraShortestPath<StringVertex, StringEdge>(localGraph, currentNode, cheaperUnvisitedEdges.get(0).getTarget());
		
			pathInProgress = DSP.getPath().getEdgeList();
			
			return edgeToTarget(pathInProgress.remove(0), currentNode);
			
		// Otherwise, the cheapest edge is connected to this node
		} else {
			
			Collections.sort(connectedEdges);
			
			return edgeToTarget(connectedEdges.get(0), currentNode);
			
		}
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	protected StringVertex startNode() {
		
		return randomNode();
	}

}
