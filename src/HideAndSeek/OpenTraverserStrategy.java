package HideAndSeek;

import java.util.List;

import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;

/**
 * The methods that a traverser strategy must expose
 * in order to have its mechanisms available to other
 * strategies
 * 
 * @author Martin
 */
public interface OpenTraverserStrategy extends GraphTraverser  {

	/**
	 * @param currentNode
	 * @return
	 */
	public StringVertex nextNode(StringVertex currentNode);

	/**
	 * @param currentNode
	 * @return
	 */
	public StringVertex connectedNode(StringVertex currentNode);
	
	/**
	 * @param currentNode
	 * @param connectedEdges
	 * @return
	 */
	public StringEdge getConnectedEdge(StringVertex currentNode, List<StringEdge> connectedEdges);
	
	/**
	 * @param currentNode
	 * @return
	 */
	public List<StringEdge> getConnectedEdges(StringVertex currentNode);
	
	/**
	 * 
	 */
	public void atNode();
	
	/**
	 * 
	 */
	public void atNextNode(StringVertex nextNode);
	
	/**
	 * @param startNode
	 */
	public void atStart(StringVertex startNode);
	
	
}