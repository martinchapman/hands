package HideAndSeek.seeker.repeatgame.probability.automatic;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.repeatgame.probability.VariableNodesInverseHighProbability;

/**
 * If the parent strategy, which is tuned to exploiting hiders that attempt
 * to hide uniquely, is faced with such a hider, it may not always be advantageous
 * to use *all* this information, as it becomes detectable.
 * 
 * @author Martin
 *
 */
public class AutomaticVariableNodesInverseHighProbability extends VariableNodesInverseHighProbability {
	
	/**
	 * @param graphController
	 */
	public AutomaticVariableNodesInverseHighProbability(GraphController<StringVertex, StringEdge> graphController) {
		
		super(graphController, Integer.MAX_VALUE);
		
	}

	/**
	 * @param graphController
	 * @param probabilityOfNonOptimalPlay
	 */
	public AutomaticVariableNodesInverseHighProbability(GraphController<StringVertex, StringEdge> graphController, double probabilityOfNonOptimalPlay) {
		
		super(graphController, Integer.MAX_VALUE);
		
		this.probabilityOfNonOptimalPlay = probabilityOfNonOptimalPlay;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	public StringVertex startNode() {
		
		predictiveNodes = numberOfPredictiveNodes();
		
		return super.startNode();
		
	}
	
	/**
	 * Mechanism by which this strategy does not reveal its
	 * entire behaviour: at a given probability, it will
	 * play non-optimally. 
	 */
	private double probabilityOfNonOptimalPlay;
	
	/**
	 * @return
	 */
	protected int numberOfPredictiveNodes() {
		
		if ( Math.random() < probabilityOfNonOptimalPlay ) {
			
			// Random value between 0 and number of hide locations
			return (int)( Math.random() * ( estimatedNumberOfHideLocations() + 1 ) );
		
		} else {
			
			return estimatedNumberOfHideLocations();
			
		}
		
	}
}
