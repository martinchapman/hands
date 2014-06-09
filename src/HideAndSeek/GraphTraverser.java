package HideAndSeek;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import Utility.Utils;

/**
 * @author Martin
 *
 */
public abstract class GraphTraverser {

	/**
	 * 
	 */
	protected GraphController<StringVertex, StringEdge> graphController;
	
	/**
	 * @param graph
	 */
	public GraphTraverser(GraphController<StringVertex, StringEdge> graphController) {
		
		this.graphController = graphController;
		
		graphController.registerTraversingAgent(this);
		
		if ( uniquelyVisitNodes == true ) {
		
			uniquelyVisitedNodes = new HashSet<StringVertex>();
		
			uniquelyVisitedEdges = new HashSet<StringEdge>();
			
		}
		
	}
	
	/**
	 * @return
	 */
	protected StringVertex randomNode() {
		
		StringVertex[] vertices = new StringVertex[graphController.vertexSet().size()];
		
		graphController.vertexSet().toArray(vertices);
		
		return vertices[(int)(Math.random() * vertices.length)];
		
	}
	
	/**
	 * 
	 */
	protected boolean uniquelyVisitNodes = true;
	
	/**
	 * 
	 */
	private HashSet<StringVertex> uniquelyVisitedNodes; 
	
	/**
	 * @param node
	 */
	protected void addUniquelyVisitedNode(StringVertex node) {
		
		uniquelyVisitedNodes.add(node);
		
	}
	
	/**
	 * @return
	 */
	final public HashSet<StringVertex> uniquelyVisitedNodes() {
		
		return uniquelyVisitedNodes;
		
	}
	
	/**
	 * 
	 */
	private HashSet<StringEdge> uniquelyVisitedEdges;
	
	/**
	 * @param node
	 */
	protected void addUniquelyVisitedEdge(StringEdge edge) {
		
		uniquelyVisitedEdges.add(edge);
		
	}
	
	/**
	 * @return
	 */
	final public HashSet<StringEdge> uniquelyVisitedEdges() {
		
		return uniquelyVisitedEdges;
		
	}
	
	/**
	 * @param edge
	 * @return
	 */
	protected boolean visitedEdge(StringEdge edge) {
		
		return uniquelyVisitedEdges.contains(edge);
		
	}
	
	/**
	 * Working with connectedNode in order to determine how nodes
	 * from a set are selected. Default is random.
	 * 
	 * @param connectedEdges
	 * @return
	 */
	protected StringEdge getConnectedEdge(StringVertex currentNode, List<StringEdge> connectedEdges) {
	
		return connectedEdges.get((int)(Math.random() * connectedEdges.size()));
		
	}
	
	/**
	 * Return the edges linked to the specified node. Default is
	 * all connected nodes.
	 * 
	 * @param currentNode
	 * @return
	 */
	protected List<StringEdge> getConnectedEdges(StringVertex currentNode) {
		
		return new ArrayList<StringEdge>(graphController.edgesOf(currentNode));
		
	}
	
	/**
	 * Select a connected node according to how edges are sorted, and how
	 * edges are selected from this set.
	 * 
	 * A wrapper method, of sorts, to enforce additional constraints such a
	 * no backtracking.
	 * 
	 * @param currentNode
	 * @return
	 */
	protected StringVertex connectedNode(StringVertex currentNode) {
		
		List<StringEdge> connectedEdges = getConnectedEdges(currentNode);
		
		StringEdge connectedEdge = null;
		
		StringVertex target = null;
		
		HashSet<StringEdge> selectedInThisSession = new HashSet<StringEdge>();
		
		do {
			
			// If we have tried all outgoing edges available (more a programmatic choice than a strategic one), return random.
			if (selectedInThisSession.size() == connectedEdges.size()) {
				
				target = edgeToTarget(connectedEdges.get((int)(Math.random() * connectedEdges.size())), currentNode);
				
				break;
				
			}
			
			connectedEdge = getConnectedEdge(currentNode, connectedEdges);
			
			target = edgeToTarget(connectedEdge, currentNode);
			
			selectedInThisSession.add(connectedEdge);
			
					// Otherwise, loop while not allowed to repeat nodes
		} while (   uniquelyVisitNodes == true && uniquelyVisitedNodes.contains( target ) );
		
		addUniquelyVisitedNode(target);
		
		addUniquelyVisitedEdge(connectedEdge);
		
		return target;
		
	}
	
	/**
	 * @param connectedEdge
	 * @param currentNode
	 * @return
	 */
	protected StringVertex edgeToTarget(StringEdge connectedEdge, StringVertex currentNode) {
		
		if (connectedEdge.getSource().equals(currentNode)) {
			
			return connectedEdge.getTarget();
			
		} else if (connectedEdge.getTarget().equals(currentNode)) {
			
			return connectedEdge.getSource();
			
		}
		
		return null;
		
	}
	
	/**
	 * 
	 */
	protected void endOfRound() { 
		
		if (uniquelyVisitedNodes != null) {
			
			uniquelyVisitedNodes.clear();
			
		}
		
	}
	
	/**
	 * How to derive the next node when traversing the graph
	 * @param currentNode
	 * @return
	 */
	protected abstract StringVertex nextNode(StringVertex currentNode);
	
	/**
	 * At which node to start from when hiding
	 * @return
	 */
	protected abstract StringVertex startNode();

}
