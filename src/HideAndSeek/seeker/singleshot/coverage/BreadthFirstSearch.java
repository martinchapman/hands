package HideAndSeek.seeker.singleshot.coverage;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.SeekingAgent;
import Utility.Utils;

/**
 * Standard BFS implementation tailored to potentially return to a parent
 * as no assumptions can be made about connectivity between siblings.
 * 
 * Note: There may be more efficient library implementations of these algorithms,
 * but these are tailored to the platform.
 * 
 * @author Martin
 *
 */
public class BreadthFirstSearch extends SeekingAgent {

	/**
	 * @param graph
	 */
	public BreadthFirstSearch(GraphController <StringVertex, StringEdge> graphController) {

		super(graphController);

		toBeVisited = new ArrayList<StringVertex>();

		pathInProgress = new ArrayList<StringEdge>();

	}

	/**
	 * 
	 */
	protected ArrayList<StringVertex> toBeVisited;

	/**
	 * 
	 */
	protected List<StringEdge> pathInProgress;

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	public StringVertex nextNode(StringVertex currentNode) {

		// Add all children of the current node to the end of the list
		for ( StringEdge vertexEdge : getConnectedEdges(currentNode) ) {
			
			StringVertex child = edgeToTarget(vertexEdge, currentNode);
			
			if (!toBeVisited.contains(child)) toBeVisited.add(edgeToTarget(vertexEdge, currentNode));
			
		}
		
		// If we are currently on a path back to a next breadth node, do this first:
		if ( pathInProgress.size() > 1 ) { 

			return edgeToTarget(pathInProgress.remove(0), currentNode);

		}
		
		if ( toBeVisited.size() == 0 ) return connectedNode(currentNode);

		// If we cannot move directly to the next node (i.e. from sibling to sibling) we must find the path to this sibling
		if (!graphController.containsEdge(currentNode, toBeVisited.get(0))) {

		
			if (!localGraph.vertexSet().contains(toBeVisited.get(0))) {
				
				System.err.println("Error: Sibling not in graph: " + toBeVisited.get(0) + getStatus());
				
				return connectedNode(currentNode);
				
			}
			
			// This should be the path through the parent node, unless this is the last sibling.
			DijkstraShortestPath<StringVertex, StringEdge> DSP = new DijkstraShortestPath<StringVertex, StringEdge>(localGraph, currentNode, toBeVisited.get(0));

			pathInProgress = DSP.getPath().getEdgeList();

			if ( pathInProgress.size() > 0 ) {
				
				return edgeToTarget(pathInProgress.remove(0), currentNode);
				
			} else {
				
				/* 
				 * Because there should always be a path through the parent to another child,
				 * this should never be invoked.
				 */
				
				System.err.println("Error: No path to sibling.\n Target: " + toBeVisited.get(0) + getStatus());
				
				return connectedNode(currentNode);
				
			}

		}

		Utils.talk(toString(), "Next node in BFS: " + toBeVisited.get(0));
		
		// Otherwise, freely move to the next thing to be visited
		return toBeVisited.remove(0);

	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#getStatus()
	 */
	public String getStatus() {
		
		return super.getStatus() + "\nNodes to visit: " + toBeVisited;
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	public StringVertex startNode() {
		
		return randomNode();
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.Seeker#endOfRound()
	 */
	@Override
	public void endOfRound() {
	
		super.endOfRound();
		
		toBeVisited.clear();
		
		pathInProgress.clear();
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.Seeker#printRoundStats()
	 */
	public String printRoundStats() {
		
		Utils.talk(toString(), "Explored nodes: " + exploredNodesTable());
		
		return super.printRoundStats();
		
	}

}