package HideAndSeek;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.SeekerLocalGraph;

/**
 * The methods that a traverser strategy must expose
 * in order to have its mechanisms available to other
 * strategies
 * 
 * @author Martin
 */
public interface OpenTraverserStrategy  {

	public StringVertex nextNode(StringVertex currentNode);

	public StringVertex connectedNode(StringVertex currentNode);
	
}