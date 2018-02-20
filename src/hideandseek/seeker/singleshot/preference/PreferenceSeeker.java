package hideandseek.seeker.singleshot.preference;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;

import Utility.Utils;
import hideandseek.GraphTraverser;
import hideandseek.OpenTraverserStrategy;
import hideandseek.VariableTraversalStrategy;
import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;
import hideandseek.seeker.SeekingAgent;
import hideandseek.seeker.singleshot.coverage.BacktrackGreedyMechanism;


/**
 * Note: This is an unfortunate duplicate of PreferenceHider, with minor changes.
 * 
 * @author Martin
 *
 */
public abstract class PreferenceSeeker extends SeekingAgent implements VariableTraversalStrategy {
	
	/**
	 * 
	 */
	private LinkedHashSet<StringVertex> targetVertices;
	
	/**
	 * 
	 */
	private double graphPortion;
	
	/**
	 * The mechanism to use when exploring the graph
	 */
	protected OpenTraverserStrategy explorationMechanism;
	

	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public PreferenceSeeker( GraphController<StringVertex, StringEdge> graphController, String name) {
		
		this(graphController, name, 1.0, null);
		
	}

	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public PreferenceSeeker( GraphController<StringVertex, StringEdge> graphController) {
		
		this(graphController, "", 1.0, null);
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public PreferenceSeeker( GraphController<StringVertex, StringEdge> graphController, String name, double graphPortion ) {
		
		this(graphController, name, graphPortion, null);
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public PreferenceSeeker( GraphController<StringVertex, StringEdge> graphController, double graphPortion ) {
		
		this(graphController, "", graphPortion, null);
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public PreferenceSeeker( GraphController<StringVertex, StringEdge> graphController, String name, double graphPortion, GraphTraverser responsibleAgent) {
		
		super(graphController, name, responsibleAgent);

		currentPath = new ArrayList<StringEdge>();
		
		this.graphPortion = graphPortion;
		
		Utils.talk(toString(), "Graph portion: " + graphPortion);
		
		targetVertices = new LinkedHashSet<StringVertex>();
		
		explorationMechanism = getExplorationMechanism(this.responsibleAgent);
		
		targetsGenerated = false;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.VariableTraversalStrategy#getExplorationMechanism(HideAndSeek.GraphTraverser)
	 */
	public OpenTraverserStrategy getExplorationMechanism(GraphTraverser responsibleAgent) {
		
		return new BacktrackGreedyMechanism(graphController, responsibleAgent);
		
	}
	
	/**
	 * ~MDC Doesn't feel neat, but because target nodes are
	 * removed, but may be necessary to avoid unnecessary regeneration
	 * 
	 * @param vertex
	 * @return
	 */
	private boolean targetsGenerated;
	
	/**
	 * @return
	 */
	public abstract LinkedHashSet<StringVertex> computeTargetNodes();
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.HidingAgent#endOfRound()
	 */
	@Override
	public void endOfRound() {
	
		super.endOfRound();
		
		targetVertices.clear();
		
		targetsGenerated = false;
		
		currentPath.clear();
		
		explorationMechanism.endOfRound();
		
	}

	/**
	 * 
	 */
	private List<StringEdge> currentPath;
	
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
		
		// Only attempt this computation with specified knowledge of the graph, and if it has not be done before
		if ( uniquelyVisitedNodes().size() >= ( graphController.vertexSet().size() * graphPortion ) && !targetsGenerated ) {
			
			targetVertices = computeTargetNodes();
			
			Utils.talk(toString(), "Target nodes: " + targetVertices);
			
			targetsGenerated = true;
			
		}
		
		if ( targetVertices.contains(currentNode) ) { 
			
			targetVertices.remove(currentNode); 
			
		}
		
	}
	
	/**
	 * @param nextNode
	 */
	public void atNextNode(StringVertex nextNode) {
		
		super.atNextNode(nextNode);
		
		explorationMechanism.atNextNode(nextNode);
		
	}
	
	/**
	 * @return
	 */
	public String getStatus() {
		
		return super.getStatus() + "\nTarget Nodes: " + targetVertices + "\n" +
									"Current Path: " + currentPath;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public StringVertex nextNode(StringVertex currentNode) {
	
		if ( targetVertices.size() == 0 ) {

			return explorationMechanism.nextNode(currentNode);
			
		} else {
			
			// If we're already on the DSP to a node, continue on it
			if ( currentPath.size() > 0 ) return edgeToTarget(currentPath.remove(0), currentNode);
			
			DijkstraShortestPath<StringVertex, StringEdge> dsp = new DijkstraShortestPath<StringVertex, StringEdge>(localGraph, currentNode, new ArrayList<StringVertex>(targetVertices).get(0));
	    	
			// If no path available, return random connected node
			if ( dsp.getPathEdgeList() == null || dsp.getPathEdgeList().size() == 0 ) { 
			
				return explorationMechanism.nextNode(currentNode);
			
			}
			
			currentPath = new ArrayList<StringEdge>(dsp.getPathEdgeList());
			
			return edgeToTarget(currentPath.remove(0), currentNode);
			
		}
	
	}
    
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	public StringVertex startNode() {
		
		return randomNode();
		
	}

}
