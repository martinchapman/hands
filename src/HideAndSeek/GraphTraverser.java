package HideAndSeek;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.jgrapht.graph.DefaultEdge;

import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;

/**
 * @author Martin
 *
 */
public abstract class GraphTraverser {

	/**
	 * 
	 */
	protected HiddenObjectGraph<StringVertex, StringEdge> graph;
	
	/**
	 * @param graph
	 */
	public GraphTraverser(HiddenObjectGraph<StringVertex, StringEdge> graph) {
		
		this.graph = graph;
		
		graph.registerTraversingAgent(this);
		
		if ( uniquelyVisitNodes == true ) {
		
			uniquelyVisitedNodes = new HashSet<StringVertex>();
		
		}
		
	}
	
	/**
	 * @return
	 */
	protected StringVertex randomNode() {
		
		StringVertex[] vertices = new StringVertex[graph.vertexSet().size()];
		
		graph.vertexSet().toArray(vertices);
		
		return vertices[(int)(Math.random() * vertices.length)];
		
	}
	
	/**
	 * 
	 */
	protected boolean uniquelyVisitNodes = true;
	
	/**
	 * 
	 */
	protected HashSet<StringVertex> uniquelyVisitedNodes; 
	
	/**
	 * Working with connectedNode in order to determine how nodes
	 * from a set are selected
	 * 
	 * @param connectedEdges
	 * @return
	 */
	protected StringEdge getConnectedEdge(StringVertex currentNode, List<StringEdge> connectedEdges) {
	
		return connectedEdges.get((int)(Math.random() * connectedEdges.size()));
		
	}
	
	/**
	 * Return the edges linked to the specified node
	 * 
	 * @param currentNode
	 * @return
	 */
	protected List<StringEdge> getConnectedEdges(StringVertex currentNode) {
		
		return new ArrayList<StringEdge>(graph.edgesOf(currentNode));
		
	}
	
	/**
	 * Select a connected node according to the functionality of getConnectedEdge
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
		
			connectedEdge = getConnectedEdge(currentNode, connectedEdges);
			
			target = edgeToTarget(connectedEdge, currentNode);
			
			selectedInThisSession.add(connectedEdge);
			
					// Loop while not allowed to repeat nodes BUT
		} while (   uniquelyVisitNodes == true && uniquelyVisitedNodes.contains( target ) &&
				    // only if we haven't already tried all outgoing edges available (more a programmatic choice than a strategic one)
				    selectedInThisSession.size() != connectedEdges.size()  );
		
		return target;
		
	}
	
	protected StringVertex edgeToTarget(StringEdge connectedEdge, StringVertex currentNode) {
		
		if (connectedEdge.getSource().equals(currentNode)) {
			
			return connectedEdge.getTarget();
			
		} else if (connectedEdge.getTarget().equals(currentNode)) {
			
			return connectedEdge.getSource();
			
		}
		
		return null;
		
	}
	
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
