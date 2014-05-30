package HideAndSeek.hider.singleshot;

import java.util.ArrayList;

import HideAndSeek.GraphTraverser;
import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import Utility.Utils;

/**
 * 
 * 
 * @author Martin
 *
 */
public class VariableDistanceHider extends FixedDistanceHider implements Runnable {
	
	/**
	 * @param graph
	 */
	public VariableDistanceHider(HiddenObjectGraph<StringVertex, StringEdge> graph, int numberOfHideLocations, int minHideDistance) {
	
		super(graph, numberOfHideLocations);
		
		this.minHideDistance = minHideDistance;

	}
	
}
