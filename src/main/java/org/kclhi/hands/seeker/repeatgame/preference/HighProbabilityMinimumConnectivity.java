package org.kclhi.hands.seeker.repeatgame.preference;

import org.kclhi.hands.utility.BehaviourPrediction;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.hider.singleshot.preference.LeastConnectedMechanism;
import org.kclhi.hands.seeker.repeatgame.probability.HighProbability;

/**
 * Harnesses the MinimumConnectivity strategy to learn those nodes in the graph 
 * that are least connected, and treats these as high probability nodes, as 
 * opposed to those that are actually visited, in case the Hider is employing 
 * MinimumConnecitivity. More predictive than frequency based.
 * 
 * May help to eliminate noise introduced by the frequency algorithm. 
 * 
 * (General idea: Rather than just learning likely nodes could also learn the 
 * PROPERTIES of those nodes desired by a Hider? i.e. doesn't just learn that 
 * a hider always hides in V20, learns the properties of V20 that attract the 
 * Hider to it.)
 * 
 * @author Martin
 */
public class HighProbabilityMinimumConnectivity extends HighProbability {

	private LeastConnectedMechanism leastConnectedMechanism;
	
	/**
	 * @param graph
	 */
	public HighProbabilityMinimumConnectivity(GraphController <StringVertex, StringEdge> graphController) {
		
		super(graphController);
		
		leastConnectedMechanism = new LeastConnectedMechanism(graphController, 0, this);
		
	}
	
	/* (non-Javadoc)
	 * @see org.kclhi.hands.seeker.Seeker#addHideLocation(HideAndSeek.graph.StringVertex)
	 */
	public void addHideLocation(StringVertex location) {
		
		super.addHideLocation(location);
		
		leastConnectedMechanism.hideHere(location);
		
	}

	/* (non-Javadoc)
	 * @see org.kclhi.hands.seeker.Seeker#endOfRound()
	 */
	@Override
	public void endOfRound() {
		
		super.endOfRound();
		
		likelyNodes = leastConnectedMechanism.getMinimumConnectivityNodes();
		
	}

}
