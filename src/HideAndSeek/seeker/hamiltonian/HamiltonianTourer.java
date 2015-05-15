package HideAndSeek.seeker.hamiltonian;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.alg.EulerianCircuit;
import org.jgrapht.alg.HamiltonianCycle;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.SeekingAgent;

/**
 * If an Hamiltonian circuit exists in the graph, follow it. 
 * 
 * @author Martin
 *
 */
public class HamiltonianTourer extends SeekingAgent {

	/**
	 * 
	 */
	protected List<StringVertex> currentTour;
	
	/**
	 * @param graphController
	 */
	public HamiltonianTourer(GraphController<StringVertex, StringEdge> graphController) {
		
		super(graphController);
		
		currentTour = new ArrayList<StringVertex>();
	
	}
	
	/**
	 * @param currentNode
	 * @return
	 */
	public StringVertex nextNode(StringVertex currentNode) {
		
		/* 
		 * If a tour is available of the graph, take the next node on it,
		 * otherwise, default to a randomly selected node.
		 */
		if ( currentTour.size() > 0 ) {
			
			return currentTour.remove(0);
			
		} else {
			
			return connectedNode(currentNode);
			
		}
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#startNode()
	 */
	@Override
	public StringVertex startNode() {

		if ( currentTour.size() > 0 ) {
			
			return currentTour.remove(0);
			
		} else {
			
			return randomNode();
			
		}
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.SeekingAgent#endOfRound()
	 */
	public void endOfRound() {
		
		super.endOfRound();
		
		if ( HamiltonianCycle.getApproximateOptimalForCompleteGraph(localGraph).size() == graphController.vertexSet().size() ) {
			
			currentTour = HamiltonianCycle.getApproximateOptimalForCompleteGraph(localGraph);
			
		}
		
	}

}
