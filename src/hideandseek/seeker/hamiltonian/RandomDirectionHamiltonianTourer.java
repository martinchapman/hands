package hideandseek.seeker.hamiltonian;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.alg.EulerianCircuit;

import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;
import hideandseek.seeker.SeekingAgent;

/**
 * If a Hamiltonian circuit exists in the graph, follow it
 * one way, or the other, with equal percentage. 
 * 
 * This is seen as the optimal strategy in the literature
 * by Gal and Alpern.
 * 
 * @author Martin
 *
 */
public class RandomDirectionHamiltonianTourer extends HamiltonianTourer {

	/**
	 * @param graphController
	 */
	public RandomDirectionHamiltonianTourer(GraphController<StringVertex, StringEdge> graphController) {
		
		super(graphController);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.SeekingAgent#endOfRound()
	 */
	public void endOfRound() {
		
		super.endOfRound();
		
		if ( currentTour.size() > 0 ) {
			
			if ( Math.random() < 0.5 ) {
				
				Collections.reverse(currentTour);
				
			}
			
		}
		
	}

}
