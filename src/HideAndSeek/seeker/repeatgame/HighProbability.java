package HideAndSeek.seeker.repeatgame;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.SeekerLocalGraph;
import Utility.BehaviourPrediction;
import Utility.Utils;

/* Rather than just learning likely nodes could also learn the PROPERTIES of those nodes desired by a Hider?
   i.e. doesn't just learn hider always hiding in V20, learns the properties of V20 that attract the Hider to it */

/**
 * @author Martin
 *
 */
public class HighProbability extends SeekerLocalGraph {

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
	public HighProbability(
			GraphController <StringVertex, StringEdge> graphController) {
		super(graphController);
		
		likelyNodes = new ArrayList<StringVertex>();
		
		behaviourPrediction = new BehaviourPrediction();
		
		currentPath = new ArrayList<StringEdge>();
		
	}

	/**
	 * 
	 */
	protected ArrayList<StringVertex> likelyNodes;
	
	/**
	 * 
	 */
	protected List<StringEdge> currentPath;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected StringVertex nextNode(StringVertex currentNode) {
		
		super.nextNode(currentNode);
		
		// If we happen to pass by, or land upon, our likely node, it is no longer a target
		if ( likelyNodes.contains(currentNode) ) likelyNodes.remove(likelyNodes.indexOf(currentNode));
		
		// If we're already on the DSP to a node, continue on it
		if (currentPath.size() > 0) return edgeToTarget(currentPath.remove(0), currentNode);
		
		// Use likely node information if available, and if graph has sufficient information to use:
		if ( likelyNodes.size() > 0 ) {
			
			StringVertex targetNode;
			
			DijkstraShortestPath<StringVertex, StringEdge> dsp = new DijkstraShortestPath<StringVertex, StringEdge>(localGraph, currentNode, likelyNodes.get(0));
	    	
			Utils.talk(this.toString(), "Heading for " + likelyNodes.get(0));
			
			// If no path available, return random connected node
			if (dsp.getPathEdgeList() == null || dsp.getPathEdgeList().size() == 0) return connectedNode(currentNode);
			
			currentPath = new ArrayList<StringEdge>(dsp.getPathEdgeList());
			
			return edgeToTarget(currentPath.remove(0), currentNode);
		
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
		
		return randomNode();
		
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
		
		Utils.talk(this.toString(), "Likely nodes:");
		Utils.talk(this.toString(), behaviourPrediction.toString());
		
	}

	@Override
	protected void endOfGame() {
		
		super.endOfGame();
		
		behaviourPrediction.showGraphs();
		
		// If game had ended, and an instance of this object may be used in further games, clear all learning.
		behaviourPrediction = new BehaviourPrediction();
		
		
	}

}
