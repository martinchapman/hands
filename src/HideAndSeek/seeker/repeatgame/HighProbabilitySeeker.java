package HideAndSeek.seeker.repeatgame;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.Seeker;
import Utility.BehaviourPrediction;

/**
 * @author Martin
 *
 */
public class HighProbabilitySeeker extends Seeker {

	/**
	 * 
	 */
	protected BehaviourPrediction behaviourPrediction;
	
	/**
	 * 
	 */
	protected int predictiveNodes = Integer.MAX_VALUE;
	
	/**
	 * @return
	 */
	public int getPredictiveNodes() {
		
		return predictiveNodes;
	
	}

	/**
	 * @param graph
	 */
	public HighProbabilitySeeker(
			GraphController <StringVertex, StringEdge> graphController) {
		super(graphController);
		
		likelyNodes = new ArrayList<StringVertex>();
		
		behaviourPrediction = new BehaviourPrediction();
		
	}

	/**
	 * 
	 */
	protected ArrayList<StringVertex> likelyNodes;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected StringVertex nextNode(StringVertex currentNode) {
		
		if ( likelyNodes.contains(currentNode) ) likelyNodes.remove(likelyNodes.indexOf(currentNode));
		
		// Use likely node information if available
		if (likelyNodes.size() > 0) {
			
			StringVertex targetNode;
			
			DijkstraShortestPath<StringVertex, StringEdge> dsp = new DijkstraShortestPath<StringVertex, StringEdge>(graphController, currentNode, likelyNodes.get(0));
	    	
			List<StringEdge> DSP = new ArrayList<StringEdge>(dsp.getPathEdgeList());
			
			return edgeToTarget(DSP.get(0), currentNode);
		
		// If not, search randomly
		} else {
			
			return connectedNode(currentNode);
			
		}
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	protected StringVertex startNode() {
		
		StringVertex[] vertices = new StringVertex[graph.vertexSet().size()];
		
		return graph.vertexSet().toArray(vertices)[0];
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.Seeker#addHideLocation(HideAndSeek.graph.StringVertex)
	 */
	protected void addHideLocation(StringVertex location) {
		
		super.addHideLocation(location);
		
		behaviourPrediction.recordHideLocation(location);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.Seeker#endOfRound()
	 */
	@Override
	public void endOfRound() {
		
		super.endOfRound();
		
		// Recalculate probabilities
		behaviourPrediction.endRound();
		
		/* Recreate list of likely vertices (currently assuming unknown value of K on part of seeker (until all objects are found), 
		   so just get ALL likely locations) */
		likelyNodes = behaviourPrediction.rankLikelyHideLocations(predictiveNodes);
		
	}

}
