package hideandseek.hider.singleshot.random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;

import Utility.Utils;
import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;
import hideandseek.hider.HidingAgent;

/**
 * Same as parent, except does not move between rounds.
 * 
 * @author Martin
 *
 */
public class GreedyRandomSetStaticBetween extends GreedyRandomSet {

	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public GreedyRandomSetStaticBetween(GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations) {
		
		super(graphController, numberOfHideLocations);
		
	}
  
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.singleshot.random.RandomSet#startNode()
	 */
	public StringVertex startNode() {

		return currentNode();
		
	}

}