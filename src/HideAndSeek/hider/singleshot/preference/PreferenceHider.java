package HideAndSeek.hider.singleshot.preference;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;

import HideAndSeek.OpenTraverserStrategy;
import HideAndSeek.VariableTraversalStrategy;
import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.HiderLocalGraph;
import HideAndSeek.seeker.singleshot.coverage.NearestNeighbourMechanism;

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
public abstract class PreferenceHider extends HiderLocalGraph implements VariableTraversalStrategy {
	
	/**
	 * 
	 */
	private LinkedHashSet<StringVertex> targetVertices;
	
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
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public PreferenceHider( GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations) {
		
		super(graphController, numberOfHideLocations);

		targetVertices = new LinkedHashSet<StringVertex>();
		
		currentPath = new ArrayList<StringEdge>();
		
		graphPortion = 1.0;
		
		explorationMechanism = getExplorationMechanism();
		
		explorationMechanism.setResponsibleAgent(this);
		
	}
	
	public OpenTraverserStrategy getExplorationMechanism() {
		
		return new NearestNeighbourMechanism(graphController);
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 */
	public PreferenceHider( GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations, double graphPortion ) {
		
		this(graphController, numberOfHideLocations);
		
		this.graphPortion = graphPortion;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.HidingAgent#hideHere(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public boolean hideHere(StringVertex vertex) {
		
		// Only attempt this computation with complete knowledge of the graph, and if it has not be done before
		if ( uniquelyVisitedNodes().size() >= ( graphController.vertexSet().size() * graphPortion ) && targetVertices.size() == 0 ) {
			
			targetVertices = computeTargetNodes();
			
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
		
		targetVertices.clear();
		
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
		
		super.nextNode(currentNode);
		
		if ( targetVertices.size() < numberOfHideLocations ) {

			return explorationMechanism.nextNode(currentNode);
			
		} else {
			
			// If we're already on the DSP to a node, continue on it
			if ( currentPath.size() > 0 ) return edgeToTarget(currentPath.remove(0), currentNode);
			
			DijkstraShortestPath<StringVertex, StringEdge> dsp = new DijkstraShortestPath<StringVertex, StringEdge>(localGraph, currentNode, new ArrayList<StringVertex>(targetVertices).get(0));
	    	
			// If no path available, return random connected node
			if ( dsp.getPathEdgeList() == null || dsp.getPathEdgeList().size() == 0 ) return explorationMechanism.connectedNode(currentNode);
			
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
