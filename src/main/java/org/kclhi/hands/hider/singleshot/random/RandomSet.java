package org.kclhi.hands.hider.singleshot.random;

import java.util.ArrayList;
import java.util.TreeSet;

import org.jgrapht.alg.DijkstraShortestPath;

import org.kclhi.hands.utility.Utils;
import org.kclhi.hands.GraphTraverser;
import org.kclhi.hands.OpenTraverserStrategy;
import org.kclhi.hands.VariableTraversalStrategy;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.hider.HidingAgent;
import org.kclhi.hands.seeker.singleshot.coverage.BacktrackGreedyMechanism;

/**
 * Produces a K size set of random nodes and then
 * heads for those nodes on the graph to hide in them.
 * 
 * NB: Doesn't allow bias to emerge as a product of low
 * weighted edges.
 * 
 * @author Martin
 *
 */
public class RandomSet extends HidingAgent implements VariableTraversalStrategy {
	
	/**
	 * 
	 */
	private OpenTraverserStrategy explorationMechanism;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.VariableTraversalStrategy#getExplorationMechanism(HideAndSeek.GraphTraverser)
	 */
	@Override
	public OpenTraverserStrategy getExplorationMechanism(GraphTraverser responsibleAgent) {
		
		return new BacktrackGreedyMechanism(graphController, responsibleAgent);
		
	}
	
	/**
	 * 
	 */
	private ArrayList<StringVertex> hideSet;

	/**
	 * @param graphController
	 * @param name
	 * @param numberOfHideLocations
	 * @param responsibleAgent
	 */
	public RandomSet(GraphController<StringVertex, StringEdge> graphController, String name, int numberOfHideLocations, GraphTraverser responsibleAgent) {
		
		super(graphController, name, numberOfHideLocations, responsibleAgent);
		
		populateHideSet(createRandomSet(numberOfHideLocations, new TreeSet<StringVertex>()));
		
		currentPath = new ArrayList<StringEdge>();
		
		explorationMechanism = getExplorationMechanism(responsibleAgent);
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public RandomSet(GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations) {
		
		this(graphController, "", numberOfHideLocations, null);
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 * @param responsibleAgent
	 */
	public RandomSet(GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations, GraphTraverser responsibleAgent) {
		
		this(graphController, "", numberOfHideLocations, responsibleAgent);
		
	}
	
	/**
	 * @param graphController
	 * @param name
	 * @param numberOfHideLocations
	 */
	public RandomSet(GraphController<StringVertex, StringEdge> graphController, String name, int numberOfHideLocations) {
		
		this(graphController, name, numberOfHideLocations, null);
		
	}
	
	/**
	 * @param hideSet
	 */
	protected void populateHideSet(ArrayList<StringVertex> hideSet) {
		
		this.hideSet = hideSet;
		
	}
	
	/**
	 * @return
	 */
	protected ArrayList<StringVertex> getHideSet() {
		
		return this.hideSet;
		
	}
	
	/**
	 * @param size
	 * @param ignoreSet
	 * @return
	 */
	protected ArrayList<StringVertex> createRandomSet(int size, TreeSet<StringVertex> ignoreSet) {
		
		ArrayList<StringVertex> hideSet = new ArrayList<StringVertex>();
		
		if (ignoreSet.size() > (graphController.vertexSet().size() - size)) { 
			
			Utils.talk(toString(), "Too many nodes to disregard supplied. Hiding completely randomly.");
			
			ignoreSet = new TreeSet<StringVertex>();
			
		}
		
		StringVertex randomVertex = randomNode();
		
		while ( hideSet.size() != size ) {
			
			if (!hideSet.contains(randomVertex) && !ignoreSet.contains(randomVertex)) {
				
				hideSet.add(randomVertex);
				
			}
			
			randomVertex = randomNode();
			
			// Double check.
			// if ( ignoreSet.size() + hideSet.size() >= graphController.vertexSet().size()) ignoreSet = new TreeSet<StringVertex>();
			
		}
		
		return hideSet;
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#hideHere(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public boolean hideHere(StringVertex vertex) {

		// Utils.talk(toString(), "Current node: " + vertex + " Random set: " + hideSet + " Hide locations: " + hideLocations);
		
		// If we land on a node in our random set, hide here
		if ( hideSet.contains(vertex) ) { 
			
			hideSet.remove(vertex);
			
			return true;
		
		}
		
		return false;
		
	}
  
	/**
	 * 
	 */
	ArrayList<StringEdge> currentPath;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.HidingAgent#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public StringVertex nextNode(StringVertex currentNode) {
		
		// If we're on a path to a node in the random set, follow this first
		if ( currentPath.size() > 0 ) {
			
			return edgeToTarget(currentPath.remove(0), currentNode);
			
		}
		
		// Otherwise, try and calculate a path to the next node in the set
		if ( hideSet.size() > 0 && localGraph.containsVertex( hideSet.get(0) ) ) {
			
			DijkstraShortestPath<StringVertex, StringEdge> dsp = new DijkstraShortestPath<StringVertex, StringEdge>(localGraph, currentNode, hideSet.get(0));
	    	
			// If no path available, return random connected node
			if (dsp.getPathEdgeList() == null || dsp.getPathEdgeList().size() == 0) return connectedNode(currentNode);
			
			currentPath = new ArrayList<StringEdge>(dsp.getPathEdgeList());
			
			return edgeToTarget(currentPath.remove(0), currentNode);
			
		}
		
		// Lastly explore efficiently.
		return connectedNode(currentNode);
		
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

	/**
	 * 
	 */
	public void endOfGame() {
		
		super.endOfGame();
		
		explorationMechanism.endOfGame();
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#endOfRound()
	 */
	@Override
	public void endOfRound() {
		
		super.endOfRound();
		
		populateHideSet(createRandomSet(numberOfHideLocations, new TreeSet<StringVertex>()));
		
		explorationMechanism.endOfRound();
		
		currentPath.clear();
		
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
