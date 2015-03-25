package HideAndSeek.seeker.repeatgame.preference;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.singleshot.preference.LeastConnected;
import HideAndSeek.seeker.repeatgame.probability.HighProbability;
import Utility.BehaviourPrediction;

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

	private LeastConnected leastConnectedMechanism;
	
	/**
	 * @param graph
	 */
	public HighProbabilityMinimumConnectivity(
			GraphController <StringVertex, StringEdge> graphController) {
		
		super(graphController);
		
		leastConnectedMechanism = new LeastConnected(graphController, 0);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.Seeker#addHideLocation(HideAndSeek.graph.StringVertex)
	 */
	public void addHideLocation(StringVertex location) {
		
		super.addHideLocation(location);
		
		leastConnectedMechanism.hideHereInterface(location);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.Seeker#endOfRound()
	 */
	@Override
	public void endOfRound() {
		
		super.endOfRound();
		
		likelyNodes = leastConnectedMechanism.getMinimumConnectivityNodes();
		
	}

}
