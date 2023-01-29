package org.kclhi.hands.seeker.repeatgame.preference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

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
public class ApproximateLeastConnectedNodes extends SeekingAgent implements VariableTraversalStrategy {

	/**
	 * The mechanism to use when exploring the graph
	 */
	protected OpenTraverserStrategy explorationMechanism;
	
	/**
	 * 
	 */
	private LinkedHashMap<StringVertex, Integer> incidentEdges;
	
	/**
	 * 
	 */
	LinkedHashMap<StringVertex, Integer> actualIncidentEdges;
	
	/**
	 * 
	 */
	HashSet<StringVertex> minimallyConnectedNodes;
	
	/**
	 * @param graphController
	 */
	public ApproximateLeastConnectedNodes( GraphController<StringVertex, StringEdge> graphController ) {

		super(graphController);
		
		explorationMechanism = getExplorationMechanism(this.responsibleAgent);
		
		incidentEdges = new LinkedHashMap<StringVertex, Integer>();

		actualIncidentEdges = updateIncidentEdges(graphController.edgeSet(Utils.KEY));
		
		actualIncidentEdges = new LinkedHashMap<StringVertex, Integer>(Utils.sortByValue(actualIncidentEdges, true));
		
		minimallyConnectedNodes = new HashSet<StringVertex>();
		
		for ( int i = 0; i < 5; i++ ) {
			
			minimallyConnectedNodes.add(new ArrayList<StringVertex>(actualIncidentEdges.keySet()).get(i));
			
		}

		System.out.println("minimallyConnectedNodes: " + minimallyConnectedNodes);
		
	}
	
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
	private LinkedHashMap<StringVertex, Integer> updateIncidentEdges(Set<StringEdge> edgeSet) {
		
		LinkedHashMap<StringVertex, Integer> connectivityRecord = new LinkedHashMap<StringVertex, Integer>();
		
		for ( StringEdge edge : edgeSet ) {
			
			if ( connectivityRecord.containsKey(edge.getSource()) ) {
				
				connectivityRecord.put(edge.getSource(), connectivityRecord.get(edge.getSource()) + 1);
				
			} else {
				
				connectivityRecord.put(edge.getSource(), 1);
				
			}
			
			if ( connectivityRecord.containsKey(edge.getTarget()) ) {
				
				connectivityRecord.put(edge.getTarget(), connectivityRecord.get(edge.getTarget()) + 1);
				
			} else {
				
				connectivityRecord.put(edge.getTarget(), 1);
				
			}
			
		}
		
		return connectivityRecord;
		
	}
	
	/* (non-Javadoc)
	 * @see org.kclhi.hands.seeker.SeekingAgent#nextNode(HideAndSeek.graph.StringVertex)
	 */
	public StringVertex nextNode(StringVertex currentNode) {
		
		incidentEdges = updateIncidentEdges(localGraph.edgeSet());
		
		double cumulativeAccuracy = 0.0;
		
		int accurate = 0;
		
		int inaccurate = 0;

		System.out.println("=======================");
		
		for ( Entry<StringVertex, Integer> entry : incidentEdges.entrySet() ) {
			
			//System.out.println("Node  Estimated  Real");
			//System.out.println(entry.getKey() + "       " + entry.getValue() + "       " + actualIncidentEdges.get(entry.getKey()) );
			
			if ( entry.getValue() == actualIncidentEdges.get(entry.getKey()) && minimallyConnectedNodes.contains(entry.getKey()) ) accurate++;
			
			if ( entry.getValue() != actualIncidentEdges.get(entry.getKey()) && minimallyConnectedNodes.contains(entry.getKey()) ) inaccurate++;
			
			cumulativeAccuracy += ( ( entry.getValue() / actualIncidentEdges.get(entry.getKey()) ) * 100 );
			
		}
		
		System.out.println("Accurate: " + accurate + " Inaccurate: " + inaccurate + " Cumulative accuracy: " + ( cumulativeAccuracy / incidentEdges.size()));
		
		System.out.println("=======================");
		
		return explorationMechanism.nextNode(currentNode);
			
	}
			
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#startNode()
	 */
	@Override
	public StringVertex startNode() {

		return randomNode();

	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#atStart(HideAndSeek.graph.StringVertex)
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
			
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#atNextNode(HideAndSeek.graph.StringVertex)
	 */
	public void atNextNode(StringVertex nextNode) {
		
		super.atNextNode(nextNode);
		
		explorationMechanism.atNextNode(nextNode);
		
	}
	
}
