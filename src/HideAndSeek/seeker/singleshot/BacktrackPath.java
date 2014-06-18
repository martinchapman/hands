package HideAndSeek.seeker.singleshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import org.jgrapht.alg.DijkstraShortestPath;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.SeekerLocalGraph;
import Utility.Utils;

/**
 * This seeker remembers paths that 'could have been' in the graph, such that if
 * it comes to a point in the graph where all outgoing edges from a node have a
 * weight which is greater than a previously encountered yet untraversed outgoing 
 * edge, it backtracks to this edge and takes it next. 
 * 
 * This backtracking is subject to a maximum backtrack distance value.
 * 
 * @author Martin
 *
 */
public class BacktrackPath extends SeekerLocalGraph {

	/**
	 * @param graphController
	 */
	public BacktrackPath(GraphController <StringVertex, StringEdge> graphController) {
		
		super(graphController);
		
		pathInProgress = new ArrayList<StringEdge>();
		
		unvisitedEdges = new TreeSet<StringEdge>();
		
	}
	
	/**
	 * 
	 */
	protected int MAXBACKTRACKDISTANCE = Integer.MAX_VALUE;

	/**
	 * 
	 */
	protected List<StringEdge> pathInProgress;
	
	/**
	 * 
	 */
	protected TreeSet<StringEdge> unvisitedEdges;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected StringVertex nextNode(StringVertex currentNode) {
		
		// Call super to add relevant information to local graph
		super.nextNode(currentNode);
		
		// Get all outgoing edges from this node (ordered by weight)
		List<StringEdge> connectedEdges = getConnectedEdges(currentNode);
		
		// Tentatively add these to the unvisited edges (no duplicates allowed)
		unvisitedEdges.addAll(connectedEdges);
		
		// Remove those edges that we have already traversed
		unvisitedEdges.removeAll(uniquelyVisitedEdges());
		
		// If we are currently on a path back to a cheaper node, do this first:
		if ( pathInProgress.size() > 0 ) { 
		
			Utils.talk(toString(), "Backtracking");
			
			Utils.talk(toString(), "Current node: " + currentNode);
			
			Utils.talk(toString(), "Going to: " + pathInProgress.get(0) + " " + edgeToTarget(pathInProgress.get(0), currentNode));
			
			return edgeToTarget(pathInProgress.remove(0), currentNode);
			
		}
		
		/* If there are any previously visited edges that have less weight than
		   ALL edges going out from our current node and have not been visited, log them */

		ArrayList<StringEdge> cheaperUnvisitedEdges = new ArrayList<StringEdge>();
		
		outerloop:
		for ( StringEdge unvisitedEdge : unvisitedEdges ) {
			
			// Test this edge against all current outgoing edges: is it smaller than all of them?
			for ( StringEdge currentEdge : connectedEdges ) {
			
				// This edge is greater than one outgoing edge, so it is not smaller than all outgoing edges, continue
				if ( graphController.getEdgeWeight(unvisitedEdge) >= graphController.getEdgeWeight(currentEdge) ) continue outerloop;
				
			}
		
			cheaperUnvisitedEdges.add(unvisitedEdge);
			
		}
		
		Utils.talk(toString(), "Cheaper unvisited edges: " + cheaperUnvisitedEdges.size());
		
		// If a cheaper edge(s) is found
		if (cheaperUnvisitedEdges.size() > 0) {
		
			// Sort the edges (if there are multiple, cheapest first)
			Collections.sort(cheaperUnvisitedEdges);
			
			/* Ensure we are always returning on edges we have previously used
			 * (Will always have local knowledge in graph as have come from vertex)
			 */
			if (uniquelyVisitedNodes().contains(cheaperUnvisitedEdges.get(0).getTarget())) {
				
				DijkstraShortestPath<StringVertex, StringEdge> DSP = new DijkstraShortestPath<StringVertex, StringEdge>(localGraph, currentNode, cheaperUnvisitedEdges.get(0).getTarget());
				
				pathInProgress = DSP.getPath().getEdgeList();
						
			} else {
				
				DijkstraShortestPath<StringVertex, StringEdge> DSP = new DijkstraShortestPath<StringVertex, StringEdge>(localGraph, currentNode, cheaperUnvisitedEdges.get(0).getSource());
				
				pathInProgress = DSP.getPath().getEdgeList();
				
			}
			
			// If the seeker is not permitted to backtrack this far, simply continue onwards
			if (pathInProgress.size() > MAXBACKTRACKDISTANCE) { 
				
				// Clear, otherwise next iteration will continue down non-permitted path
				pathInProgress.clear();
				
				return connectedNode(currentNode);
			
			}
			
			Utils.talk(toString(), "Path in progress: " + pathInProgress);
			
			return edgeToTarget(pathInProgress.remove(0), currentNode);
			
		// Otherwise, the cheapest edge is connected to this node
		} else {
			
			return connectedNode(currentNode);
			
		}
			
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.FixedStartDepthFirstSearch#getConnectedEdges(HideAndSeek.graph.StringVertex)
	 * 
	 *
	 */
	protected List<StringEdge> getConnectedEdges(StringVertex currentNode) {
		
		ArrayList<StringEdge> edges = new ArrayList<StringEdge>(graphController.edgesOf(currentNode));
		
		Collections.sort(edges);

		return edges;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#getConnectedEdge(HideAndSeek.graph.StringVertex, java.util.List)
	 * 
	 * Ensure only unvisited edges are returned according to ordering, otherwise return random
	 * 
	 */
	protected StringEdge getConnectedEdge(StringVertex currentNode, List<StringEdge> connectedEdges) {
		
		for (StringEdge edge : connectedEdges ) {

			if ( uniquelyVisitedNodes().contains(edgeToTarget(edge, currentNode)) ) continue;
			
			return edge;
			
		}
		
		return connectedEdges.get((int)(Math.random() * connectedEdges.size()));
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	protected StringVertex startNode() {
		
		StringVertex startNode = randomNode();
		
		return startNode;
	
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.Seeker#endOfRound()
	 */
	@Override
	public void endOfRound() {
		
		// TODO Auto-generated method stub
		super.endOfRound();
		
		pathInProgress.clear();
		
	}
	
}
