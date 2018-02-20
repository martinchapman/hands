package hideandseek.hider.repeatgame.deceptive;

import Utility.BinomialProbability;
import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;

public class BinomialDeceptive extends Deceptive {

	public BinomialDeceptive( GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations, int deceptiveNodes ) {
		
		super(graphController, numberOfHideLocations, deceptiveNodes, 0);
		
		// Trials = Those rounds remaining after the deception duration
		/* Number of successes = deception duration. Because, X successes is equal to achieving one single node X times. And if a single node
		   is achieved the same number of times as those nodes in the deceptive set were played, then it will equal the 'corruption' inflicted
		   upon the seeker's frequency algorithm, and weaken it. */
		/* Probability of achieving success is one round = numberOfHideLocations (number of attempts you have to get the node we want)
		 * / (over) the number of nodes - those used deceptively, because a deceptive hider will not include any nodes used deceptively in the
		 * pool of those it uses randomly.
		 */
		BinomialProbability bp = new BinomialProbability(99, 1, graphController.vertexSet().size() - deceptiveNodes, numberOfHideLocations);
		
		for (int i = 1; i <= 100; i++) {
			
			int deceptionDuration = i;
			
			int remainingRounds = 100 - deceptionDuration;
			
			bp.setMinus_r(remainingRounds);
			
			bp.setR(deceptionDuration);
			
			double bProbability = bp.calculateRorGreaterSuccesses();
			
			// Less that 0.01
			if ( bProbability < 0.01) this.deceptionDuration = deceptionDuration;
			
		}
		
		
	}
	
}
