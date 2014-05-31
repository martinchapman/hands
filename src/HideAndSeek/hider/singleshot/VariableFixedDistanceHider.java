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
public class VariableFixedDistanceHider extends RandomFixedDistanceHider implements Runnable {
	
	/**
	 * @param graph
	 */
	public VariableFixedDistanceHider(HiddenObjectGraph<StringVertex, StringEdge> graph, int numberOfHideLocations, int minHideDistance) {
	
		super(graph, numberOfHideLocations);
		
		this.minHideDistance = minHideDistance;

	}
	
}
