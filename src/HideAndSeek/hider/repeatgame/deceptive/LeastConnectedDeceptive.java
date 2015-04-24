package HideAndSeek.hider.repeatgame.deceptive;

import java.util.ArrayList;
import java.util.Collections;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.singleshot.preference.LeastConnectedMechanism;
import Utility.Utils;

/**
 * Changes the number of bias hide locations employed in an attempt to deceive
 * the seeker.
 * 
 * Still assumes seeker will choose bias edges going out of a node 100% of the time.
 * 
 * @author Martin
 *
 */
public class LeastConnectedDeceptive extends Deceptive {

	LeastConnectedMechanism leastConnectedMechanism;
	
	/**
	 * @param graph
	 * @param numberOfHideLocations
	 * @param numberOfBiasLocations
	 * @param deceptiveNodes
	 * @param deceptionDuration
	 */
	public LeastConnectedDeceptive( GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations, int deceptiveNodes, int deceptionDuration) {
		
		super(graphController, numberOfHideLocations, deceptiveNodes, deceptionDuration);
		
		leastConnectedMechanism = new LeastConnectedMechanism(graphController, numberOfHideLocations, this);

	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 * @param deceptiveNodes
	 * @param deceptionDuration
	 * @param repeatInterval
	 * @param startRound
	 * @param refreshDeceptiveSet
	 */
	public LeastConnectedDeceptive( GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations, int deceptiveNodes, int deceptionDuration, int repeatInterval, int repeatDuration, boolean refreshDeceptiveSet) {
		
		super(graphController, numberOfHideLocations, deceptiveNodes, deceptionDuration, repeatInterval, repeatDuration, refreshDeceptiveSet);
		
		leastConnectedMechanism = new LeastConnectedMechanism(graphController, numberOfHideLocations, this);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.singleshot.random.RandomSet#hideHere(HideAndSeek.graph.StringVertex)
	 */
	public boolean hideHere(StringVertex vertex) {
		
		leastConnectedMechanism.hideHere(vertex);
		
		return super.hideHere(vertex);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.repeatgame.reactive.Deceptive#createDeceptiveSet(int)
	 */
	public void createDeceptiveSet(int deceptiveNodes) {
	
		// ~MDC This could be neater
		if ( leastConnectedMechanism == null ) { 
			
			super.createDeceptiveSet(deceptiveNodes);
			
			return;
			
		}
		
		ArrayList<StringVertex> minimumConnectivityNodes = leastConnectedMechanism.getMinimumConnectivityNodes();
		
		Utils.talk(toString(), "Minimum connected nodes: " + minimumConnectivityNodes);
		
		if (minimumConnectivityNodes.size() > 0) {
			
			Collections.shuffle(minimumConnectivityNodes);
			
			for (StringVertex vertex : minimumConnectivityNodes) Utils.talk(toString(), vertex + " connections: " + graphController.degreeOf(vertex));
			
			int maxIndex = deceptiveNodes > minimumConnectivityNodes.size() ? minimumConnectivityNodes.size() : deceptiveNodes; 
			
			this.deceptiveSet = new ArrayList<StringVertex>(minimumConnectivityNodes.subList(0, maxIndex));
			
			populateDeceptiveSet(this.deceptiveSet);
		
		} else {
			
			super.createDeceptiveSet(deceptiveNodes);
			
		}
		
	}
	
}
