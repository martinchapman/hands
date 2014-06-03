package HideAndSeek.hider.adaptive;

import java.util.ArrayList;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.repeatgame.VariableBiasLocationsHider;

/**
 * Changes the number of bias hide locations employed in an attempt to deceive
 * the seeker.
 * 
 * Still assumes will choose bias edges going out of a node 100% of the time.
 * 
 * @author Martin
 *
 */
public class DeceptiveHider extends VariableBiasLocationsHider {

	/**
	 * @param graph
	 * @param numberOfHideLocations
	 * @param numberOfBiasLocations
	 * @param deceptiveNodes
	 * @param deceptionDuration
	 */
	public DeceptiveHider(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations, int numberOfBiasLocations, int deceptiveNodes, int deceptionDuration) {
		
		super(graphController, numberOfHideLocations, numberOfBiasLocations);
		
		setDeceptiveNodes(deceptiveNodes);
		
		this.deceptionDuration = deceptionDuration;
		
		createDeceptiveSet();

	}
	
	/**
	 * Number of nodes to hide deceptively
	 */
	protected int deceptiveNodes;
	
	/**
	 * Deceptive nodes used in this round
	 */
	protected int deceptiveNodesUsed = 0;
	
	/**
	 * Number of rounds for which to hide objects deceptively
	 */
	protected int deceptionDuration;
	
	/**
	 * @param deceptiveNodes
	 */
	protected void setDeceptiveNodes(int deceptiveNodes) {
		
		if (deceptiveNodes > numberOfHideLocations) {
			
			this.deceptiveNodes = numberOfHideLocations;
			
		} else {
			
			this.deceptiveNodes = deceptiveNodes;
			
		}
		
	}
	
	/**
	 * 
	 */
	protected ArrayList<StringVertex> deceptiveSet;
	
	/**
	 * 
	 */
	private void createDeceptiveSet() {
	
		deceptiveSet = new ArrayList<StringVertex>();
		
		for (int i = 0; i < deceptiveNodes; i++) {
			
			deceptiveSet.add(randomNode());
			
		}
		
	}
	
	/**
	 * @param location
	 * @return
	 */
	public boolean hideHere(StringVertex location) {
		
		if ( roundsPassed == deceptionDuration || deceptiveNodesUsed == deceptiveNodes) {
			
			return super.hideHere(location);
			
		} else {
			
			if ( deceptiveSet.contains(location) ) {
				
				deceptiveNodesUsed++;
				
				return true;
				
			} else {
				
				return false;
				
			}
			
		}
		
	}
	
	/**
	 * 
	 */
	@Override
	public void endOfRound() {
		
		super.endOfRound();
		
		deceptiveNodesUsed = 0;
		
	}
	
}
