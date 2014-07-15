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
 * Extends the functionality of parent class to vary the maximum distance 
 * that the seeker is prepared to backtrack. 
 * 
 * @author Martin
 *
 */
public class VariableBacktrackPath extends BacktrackPath {

	/**
	 * @param graphController
	 */
	public VariableBacktrackPath(GraphController <StringVertex, StringEdge> graphController, int maxbacktrackdistance) {
		
		super(graphController);
		
		MAXBACKTRACKDISTANCE = maxbacktrackdistance;
		
	}
	
}
