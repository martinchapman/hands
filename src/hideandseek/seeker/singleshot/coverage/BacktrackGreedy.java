package hideandseek.seeker.singleshot.coverage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;

import Utility.Utils;
import hideandseek.GraphTraverser;
import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;
import hideandseek.seeker.SeekingAgent;

/**
 * A 'greedy' strategy, which chooses the locally closest
 * unvisited vertex (from all those that can currently
 * be seen from the current node, and those that were 
 * viewed, but not visited, previously).
 * 
 * @author Martin
 */
public class BacktrackGreedy extends SeekingAgent {

	/**
	 * Viewed by unvisited nodes.
	 */
	protected HashSet<StringVertex> unvisitedNodes;
	
	/**
	 * Current lower cost path to a previous node.
	 */
	protected List<StringEdge> currentPath;
	
	/**
	 * @param graphController
	 */
	public BacktrackGreedy(GraphController<StringVertex, StringEdge> graphController) {
		
		this(graphController, null);
		
	}
	
	/**
	 * @param graphController
	 */
	public BacktrackGreedy(GraphController<StringVertex, StringEdge> graphController, GraphTraverser responsibleAgent) {
		
		super(graphController, responsibleAgent);
		
		unvisitedNodes = new HashSet<StringVertex>();
		
		currentPath = new ArrayList<StringEdge>();

	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#startNode()
	 */
	@Override
	public StringVertex startNode() {

		return randomNode();
	
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.SeekingAgent#getStatus()
	 */
	public String getStatus() {
		
		return super.getStatus() + "\nQueued Nodes " + unvisitedNodes + " (" + unvisitedNodes.size() + ")" +
								   "\nCurrent Path " + currentPath + " (" + currentPath.size() + ")"; 
		
	}
	
	/**
	 * @return
	 */
	public boolean backtracks() {
		
		return true;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	public StringVertex nextNode(StringVertex currentNode) {
		
		if (currentPath != null && currentPath.size() > 0 && ( !(currentPath.get(0).getSource() == currentNode) || !(currentPath.get(0).getTarget() == currentNode) )) currentPath.clear();
		
		ArrayList<StringVertex> connectedVertices = new ArrayList<StringVertex>();
		
		for (StringEdge edge : getConnectedEdges(currentNode) ) {
			
			connectedVertices.add(edgeToTarget(edge, currentNode));
			
		}
		
		/* ~MDC Hack to overcome the fact that other strategies use the BTG
		 * mechanism may not always call nextNode, hence leaving visited
		 * nodes in the unvisited list.
		 */
		
		// Pretend node is connected so it will be removed below, as already visited.
		/*for ( StringVertex node : unvisitedNodes ) {
			
			if ( exploredNodesTable().containsKey(node) ) {
				
				Utils.talk(toString(), "Additional vertex: " + node);
				
				connectedVertices.add(node);
				
			}
		
		}*/
		
		// ~MDC Not needed with hack
		if ( unvisitedNodes.contains(currentNode) ) {
			
			unvisitedNodes.remove(currentNode);
			
		}
		
		// ~MDC End of hack

		// If not on way back to a closer, and previously unvisited, node:
		if ( currentPath.size() == 0 ) {
		
			//for (StringEdge edge : getConnectedEdges(currentNode) ) {
			
			for ( StringVertex vertex : connectedVertices) {
				
				// Do not relist nodes as unvisited if they have already been visited
				if ( !exploredNodesTable().containsKey(vertex) ) { // edgeToTarget(edge, currentNode)
				
					unvisitedNodes.add(vertex); // edgeToTarget(edge, currentNode)
				
				
				} /*else {
					
					// ~MDC May fail most of the time. Part of hack.
					if ( unvisitedNodes.contains(vertex) ) unvisitedNodes.remove(vertex);
			
				}*/
				
			}
			
			if ( unvisitedNodes.size() == 0 ) {
				
				Utils.talk(toString(), "\nNo unvisited nodes from this node, or to backtrack to.\n" + this.getStatus());
				
				return connectedNode(currentNode);
				
			}
			
			StringVertex closestNode = (StringVertex) unvisitedNodes.toArray()[0];
			
			double shortestDistance = Double.MAX_VALUE;
			
			DijkstraShortestPath<StringVertex, StringEdge> dsp;
			
			// Check which node is closest: may be one adjacent, or one observed, but not visited, previously.
			for ( StringVertex unvisitedNode : unvisitedNodes ) {
				
				dsp = new DijkstraShortestPath<StringVertex, StringEdge>(localGraph, currentNode, unvisitedNode);
		    	
				if ( dsp.getPathLength() <= shortestDistance ) { 
					
					if ( dsp.getPathEdgeList().size() > 1 && !backtracks() ) continue;
					
					closestNode = unvisitedNode;
				
					shortestDistance = dsp.getPathLength();
					
				}
				
			}
			
			dsp = new DijkstraShortestPath<StringVertex, StringEdge>(localGraph, currentNode, closestNode);
			
			
			// If no path can be found, return adjacent node on lowest cost edge (ordered by methods below).
			if ( dsp != null && dsp.getPathEdgeList().size() == 0 ) {
				
				return connectedNode(currentNode);
				
			}
			
			currentPath = dsp.getPathEdgeList();
			
			//if ( dsp.getPathEdgeList().size() > 1 ) Utils.talk(toString(), "Backtracking further than to a neighbour\nCurrent path: " + currentPath);
			
		} 
		
		return edgeToTarget(currentPath.remove(0), currentNode);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.FixedStartDepthFirstSearch#getConnectedEdges(HideAndSeek.graph.StringVertex)
	 */
	protected List<StringEdge> getConnectedEdges(StringVertex currentNode) {
		
		ArrayList<StringEdge> edges = new ArrayList<StringEdge>(super.getConnectedEdges(currentNode));
		
		Collections.sort(edges);
		
		return edges;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#getConnectedEdge(HideAndSeek.graph.StringVertex, java.util.List)
	 */
	@Override
	protected StringEdge getConnectedEdge(StringVertex currentNode, List<StringEdge> connectedEdges) {
		
		for (StringEdge edge : connectedEdges ) {

			if ( uniquelyVisitedNodes().contains(edgeToTarget(edge, currentNode)) ) continue;
			
			return edge;
			
		}
		
		return connectedEdges.get((int)(Math.random() * connectedEdges.size()));
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.SeekingAgent#endOfRound()
	 */
	public void endOfRound() {
		
		super.endOfRound();
		
		// Need to clear in this instance for this mechanism.
		exploredNodesTable().clear();
		
		currentPath.clear();
		
	}
	
}