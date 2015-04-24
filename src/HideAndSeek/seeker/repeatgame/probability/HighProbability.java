package HideAndSeek.seeker.repeatgame.probability;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.SeekerLocalGraph;
import Utility.BehaviourPrediction;
import Utility.Utils;

/**
 * @author Martin
 *
 */
public class HighProbability extends SeekerLocalGraph {

	/**
	 * 
	 */
	protected boolean printHighestProbabilityNodes = false;
	
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
			GraphController <StringVertex, StringEdge> graphController, String name) {
		super(graphController, name);
		
		likelyNodes = new ArrayList<StringVertex>();
		
		behaviourPrediction = new BehaviourPrediction();
		
		currentPath = new ArrayList<StringEdge>();
		
		lastHighestProbabilityNodes = new ArrayList<StringVertex>();
		
	}
	
	public HighProbability(
			GraphController <StringVertex, StringEdge> graphController) {
		
		this(graphController, "");
		
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
	public StringVertex nextNode(StringVertex currentNode) {
		
		super.nextNode(currentNode);
		
		// If we happen to pass by, or land upon, our likely node, it is no longer a target
		if ( likelyNodes.contains(currentNode) ) likelyNodes.remove(likelyNodes.indexOf(currentNode));
		
		// If we're already on the DSP to a node, continue on it
		if (currentPath.size() > 0) return edgeToTarget(currentPath.remove(0), currentNode);
		
		// Use likely node information if available, and if graph has sufficient information to use:
		if ( likelyNodes.size() > 0 && localGraph.containsVertex(likelyNodes.get(0)) ) {
			
			Utils.talk(toString(), "Heading for: " + likelyNodes.get(0));
			
			DijkstraShortestPath<StringVertex, StringEdge> dsp = new DijkstraShortestPath<StringVertex, StringEdge>(localGraph, currentNode, likelyNodes.get(0));
	    	
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
	public StringVertex startNode() {
		
		return randomNode();
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.Seeker#addHideLocation(HideAndSeek.graph.StringVertex)
	 */
	public void addHideLocation(StringVertex location) {
		
		super.addHideLocation(location);
		
		behaviourPrediction.recordHideLocation(location);
		
	}

	/**
	 * 
	 */
	private ArrayList<StringVertex> lastHighestProbabilityNodes;
	
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
		
		if ( printHighestProbabilityNodes ) {
		
			ArrayList<StringVertex> highestProbabilityNodes = new ArrayList<StringVertex>();
			
			for (StringVertex likelyNode : likelyNodes) {
				
				if (highestProbabilityNodes.size() == 0 || 
						behaviourPrediction.getProbability(highestProbabilityNodes.get(highestProbabilityNodes.size() - 1)) 
						== behaviourPrediction.getProbability(likelyNode) ) {
				
					highestProbabilityNodes.add(likelyNode);
				
				} else {
					
					break;
					
				}
				
			}
			
			Utils.talk(toString(), highestProbabilityNodes.size() + " highest probability nodes: " + highestProbabilityNodes);
		
		}
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.Seeker#endOfGame()
	 */
	@Override
	public void endOfGame() {
		
		super.endOfGame();
		
		behaviourPrediction.showGraphs();
		
		// If game had ended, and an instance of this object may be used in further games, clear all learning.
		behaviourPrediction = new BehaviourPrediction();
		
		
	}

}
