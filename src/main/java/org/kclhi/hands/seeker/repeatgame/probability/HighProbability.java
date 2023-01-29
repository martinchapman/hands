package org.kclhi.hands.seeker.repeatgame.probability;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;

import org.kclhi.hands.utility.BehaviourPrediction;
import org.kclhi.hands.utility.Utils;
import org.kclhi.hands.GraphTraverser;
import org.kclhi.hands.OpenTraverserStrategy;
import org.kclhi.hands.VariableTraversalStrategy;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.seeker.SeekingAgent;
import org.kclhi.hands.seeker.singleshot.coverage.BacktrackGreedyMechanism;

/**
 * @author Martin
 *
 */
public class HighProbability extends SeekingAgent implements VariableTraversalStrategy {

	/**
	 * 
	 */
	private OpenTraverserStrategy explorationMechanism;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.VariableTraversalStrategy#getExplorationMechanism(HideAndSeek.GraphTraverser)
	 */
	public OpenTraverserStrategy getExplorationMechanism(GraphTraverser responsibleAgent) {
		
		return new BacktrackGreedyMechanism(graphController, responsibleAgent);
		
	}

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
		
		this.explorationMechanism = getExplorationMechanism(responsibleAgent);
		
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
	 * @see HideAndSeek.GraphTraversingAgent#connectedNode(HideAndSeek.graph.StringVertex)
	 */
	public StringVertex connectedNode(StringVertex currentNode) {
		
		return explorationMechanism.nextNode(currentNode);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	public StringVertex startNode() {
		
		return randomNode();
		
	}
	
	/* (non-Javadoc)
	 * @see org.kclhi.hands.seeker.Seeker#addHideLocation(HideAndSeek.graph.StringVertex)
	 */
	public void addHideLocation(StringVertex location) {
		
		super.addHideLocation(location);
		
		behaviourPrediction.recordHideLocation(location);
		
	}

	/**
	 * 
	 */
	private ArrayList<StringVertex> lastHighestProbabilityNodes;
	
	/**
	 * 
	 */
	private int numberInTopK = 0;

	/**
	 * 
	 */
	private boolean LOG_TOP_K = false;
	
	/* (non-Javadoc)
	 * @see org.kclhi.hands.seeker.Seeker#endOfRound()
	 */
	@Override
	public void endOfRound() {
		
		numberInTopK = 0;
		
		if ( lastHighestProbabilityNodes.size() > 0 ) lastHighestProbabilityNodes = new ArrayList<StringVertex>(lastHighestProbabilityNodes.subList(0, hideLocations().size() - 1));
		
		for ( StringVertex vertex : hideLocations() ) {
			
			if ( lastHighestProbabilityNodes.contains(vertex) ) numberInTopK++;
			
		}

		super.endOfRound();
		
		// Recalculate probabilities
		behaviourPrediction.endRound();
		
		currentPath.clear();
		
		/* Recreate list of likely vertices (currently assuming unknown value of K on part of seeker (until all objects are found), 
		   so just get ALL likely locations) */
		likelyNodes = behaviourPrediction.rankLikelyHideLocations(predictiveNodes);
		
		Utils.talk(toString(), "Likely: " + likelyNodes.size() + " " + likelyNodes);
		
		lastHighestProbabilityNodes = new ArrayList<StringVertex>(likelyNodes);
		
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

		explorationMechanism.endOfRound();
		
	}

	/* (non-Javadoc)
	 * @see org.kclhi.hands.seeker.SeekingAgent#printRoundStats()
	 */
	@Override
	public String printRoundStats() {

		if ( LOG_TOP_K ) {
			
			return super.printRoundStats() + ",numberInTopK," + numberInTopK;
			
		} else {
			
			return super.printRoundStats();
			
		}
		
	}

	/* (non-Javadoc)
	 * @see org.kclhi.hands.seeker.Seeker#endOfGame()
	 */
	@Override
	public void endOfGame() {
		
		super.endOfGame();
		
		behaviourPrediction.showGraphs();
		
		// If game had ended, and an instance of this object may be used in further games, clear all learning.
		behaviourPrediction = new BehaviourPrediction();
		
		explorationMechanism.endOfGame();
		
		
	}

	/**
	 * @param startNode
	 */
	public void atStart(StringVertex startNode) {
		
		super.atStart(startNode);
		
		explorationMechanism.atStart(currentNode);
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#atNode()
	 */
	public void atNode() {
		
		super.atNode();
		
		explorationMechanism.atNode();
		
	}
	
	/**
	 * @param nextNode
	 */
	public void atNextNode(StringVertex nextNode) {
		
		super.atNextNode(nextNode);
		
		explorationMechanism.atNextNode(nextNode);
		
	}

}
