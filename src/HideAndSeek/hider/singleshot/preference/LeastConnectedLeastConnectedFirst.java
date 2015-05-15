package HideAndSeek.hider.singleshot.preference;

import java.util.ArrayList;
import java.util.HashSet;

import HideAndSeek.OpenTraverserStrategy;
import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.singleshot.coverage.BacktrackGreedyMechanism;
import HideAndSeek.seeker.singleshot.preference.LeastConnectedFirstMechanism;

/**
 * @deprecated Not viable with graph exploration.
 * @author Martin
 *
 */
public class LeastConnectedLeastConnectedFirst extends LeastConnected {
	
	/**
	 * 
	 */
	private ArrayList<StringVertex> leastConnectedNodes;
	
	/**
	 * @return
	 */
	public ArrayList<StringVertex> getMinimumConnectivityNodes() {
		
		return leastConnectedNodes;
	
	}

	/**
	 * The imagined number of max connections that a node
	 * can have in order to be considered to have least connectivity.
	 * (1 = leaf).
	 */
	protected static final int MAX_CONNECTIONS = 1;
	
	/**
	 * @param graph
	 * @param numberOfHideLocations
	 */
	public LeastConnectedLeastConnectedFirst(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations) {
		
		this(graphController, numberOfHideLocations, 1.0);
	
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public LeastConnectedLeastConnectedFirst( GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations, double graphPortion ) {
		
		super(graphController, numberOfHideLocations, graphPortion);
		
	}
	
	public OpenTraverserStrategy getExplorationMechanism() {
		
		return new LeastConnectedFirstMechanism(graphController);
		
	}
	
}
