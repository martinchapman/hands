package hideandseek.hider.singleshot.preference;

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
import hideandseek.hider.HidingAgent;
import hideandseek.hider.singleshot.random.RandomSetMechanism;
import hideandseek.seeker.singleshot.coverage.BacktrackGreedyMechanism;

/**
 * These hiders specify a preference for certain nodes
 * in the graph, according to given properties. They
 * therefore explore the graph, learning which nodes
 * hold these properties, for a given duration, before
 * heading for those nodes it believes to hold these 
 * properties.
 * 
 * The accuracy of these hiders is therefore dependent
 * upon their knowledge of the graph.
 * 
 * @author Martin
 *
 */
public abstract class PreferenceHider extends HidingAgent implements VariableTraversalStrategy {
	
	/**
	 * 
	 */
	private LinkedHashSet<StringVertex> targetVertices;
	
	/**
	 * @param vertex
	 */
	protected void addTargetVertex(StringVertex vertex) {
		
		targetVertices.add(vertex);
		
	}
	
	/**
	 * For this strategy to operate to best effect, ideally
	 * the agent should have complete knowledge of the graph.
	 * However, this is costly. Therefore, we may have to settle
	 * for only having knowledge of a portion of the graph before
	 * calculating maximum distance. 
	 * 
	 * @param vertex
	 * @return
	 */
	private double graphPortion;
	
	/**
	 * The mechanism to use when exploring the graph
	 */
	protected OpenTraverserStrategy explorationMechanism;
	
	/**
	 * Mechanism likely to be used if preference nodes cannot be computed
	 */
	protected RandomSetMechanism randomSet;
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public PreferenceHider( GraphController<StringVertex, StringEdge> graphController, String name, int numberOfHideLocations) {
		
		this(graphController, name, numberOfHideLocations, 1.0, null);
		
	}

	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public PreferenceHider( GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations) {
		
		this(graphController, "", numberOfHideLocations, 1.0, null);
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public PreferenceHider( GraphController<StringVertex, StringEdge> graphController, String name, int numberOfHideLocations, double graphPortion ) {
		
		this(graphController, name, numberOfHideLocations, graphPortion, null);
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public PreferenceHider( GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations, double graphPortion ) {
		
		this(graphController, "", numberOfHideLocations, graphPortion, null);
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public PreferenceHider( GraphController<StringVertex, StringEdge> graphController, String name, int numberOfHideLocations, double graphPortion, GraphTraverser responsibleAgent) {
		
		super(graphController, name, numberOfHideLocations, responsibleAgent);

		currentPath = new ArrayList<StringEdge>();
		
		this.graphPortion = graphPortion;
		
		Utils.talk(toString(), "Graph portion: " + graphPortion);
		
		targetVertices = new LinkedHashSet<StringVertex>();
		
		explorationMechanism = getExplorationMechanism(this.responsibleAgent);
		
		targetsGenerated = false;
		
		randomSet = new RandomSetMechanism(graphController, numberOfHideLocations, this.responsibleAgent);
		
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
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.HidingAgent#hideHere(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public boolean hideHere(StringVertex vertex) {
		
		// Only attempt this computation with specified knowledge of the graph, and if it has not be done before
		if ( uniquelyVisitedNodes().size() >= ( graphController.vertexSet().size() * graphPortion ) && !targetsGenerated ) {
			
			targetVertices = computeTargetNodes();
			
			targetsGenerated = true;
			
		}
		
		if ( targetVertices.contains(vertex) ) { 
			
			targetVertices.remove(vertex); 
			
			return true;
		
		}
		
		return false;
		
	}
	
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
		
		//targetVertices.clear();
		
		targetsGenerated = false;
		
		currentPath.clear();
		
		explorationMechanism.endOfRound();
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#endOfGame()
	 */
	public void endOfGame() {

		super.endOfGame();
		
		explorationMechanism.endOfGame();
		
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
		
		explorationMechanism.atNode();
		
		super.atNode();
		
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
	
		// Relax to start searching for target nodes earlier
		final int TARGET_VERTICES_SIZE = numberOfHideLocations;
		
		// Number of hide locations ensures that we do not re-explore after targets have been removed
		if ( ( targetVertices.size() + graphController.numberOfHideLocations(responsibleAgent) ) < TARGET_VERTICES_SIZE ) {
			
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
