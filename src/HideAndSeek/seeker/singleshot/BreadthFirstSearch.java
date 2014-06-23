package HideAndSeek.seeker.singleshot;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.SeekerLocalGraph;
import Utility.Utils;

/**
 * Standard BFS implementation tailored to potentially return to a parent
 * as no assumptions can be made about connectivity between siblings.
 * 
 * @author Martin
 *
 */
public class BreadthFirstSearch extends SeekerLocalGraph {

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
	protected StringVertex nextNode(StringVertex currentNode) {

		super.nextNode(currentNode);
		
		//toBeVisited.removeAll(uniquelyVisitedNodes());

		// If we are currently on a path back to a next breadth node, do this first:
		if ( pathInProgress.size() > 1 ) { 

			return edgeToTarget(pathInProgress.remove(0), currentNode);

		}

		Utils.talk(toString(), "" + getConnectedEdges(toBeVisited.get(0)));
		
		// Add all the children of the item on the top of the search list to the search list also
		for ( StringEdge vertexEdge : getConnectedEdges(toBeVisited.get(0)) ) {

			StringVertex child = edgeToTarget(vertexEdge, toBeVisited.get(0));

			if (!toBeVisited.contains(child)) toBeVisited.add(child);

		}

		// If we cannot move directly to the next node (i.e. from sibling to sibling) we must find the path to our next node
		if (!graphController.containsEdge(currentNode, toBeVisited.get(0))) {

			/* This should rarely happen as there will be a path to the child via the parent,
			 * which will be added to the local graph
			 */
			if (!localGraph.vertexSet().contains(toBeVisited.get(0))) return connectedNode(currentNode);
			
			DijkstraShortestPath<StringVertex, StringEdge> DSP = new DijkstraShortestPath<StringVertex, StringEdge>(localGraph, currentNode, toBeVisited.get(0));

			pathInProgress = DSP.getPath().getEdgeList();

			return edgeToTarget(pathInProgress.remove(0), currentNode);

		}

		// Otherwise, freely move to the next thing to be visited
		return toBeVisited.remove(0);

	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	protected StringVertex startNode() {
		
		StringVertex startNode = randomNode();
		
		// Add all children of start node
		for ( StringEdge vertexEdge : graphController.edgesOf(startNode) ) {
			
			toBeVisited.add(edgeToTarget(vertexEdge, startNode));
			
		}
		
		return startNode;
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.Seeker#endOfRound()
	 */
	@Override
	public void endOfRound() {
		// TODO Auto-generated method stub
		super.endOfRound();
		
		toBeVisited.clear();
		
		pathInProgress.clear();
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.Seeker#printRoundStats()
	 */
	public String printRoundStats() {
		
		Utils.talk(toString(), "Explored nodes: " + exploredNodes);
		
		return super.printRoundStats();
		
	}

}


