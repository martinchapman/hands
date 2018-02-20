package hideandseek.hider.repeatgame.deceptive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

import Utility.Utils;
import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;
import hideandseek.hider.singleshot.preference.LeastConnected;

/**
 * 
 * Attempts to stop a hider 'accidentally' finding new hide locations
 * while on the way to deceptive ones, by grouping the initial deceptive set.
 * 
 * @author Martin
 *
 */
public class GroupedDeceptive extends Deceptive {

	/**
	 * @param graphController
	 * @param name
	 * @param numberOfHideLocations
	 * @param deceptiveNodes
	 * @param deceptionDuration
	 */
	public GroupedDeceptive(
			GraphController <StringVertex, StringEdge> graphController,  String name,
			int numberOfHideLocations, int deceptiveNodes, int deceptionDuration) {
		
		super(graphController, name, numberOfHideLocations, deceptiveNodes, deceptionDuration);
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 * @param deceptiveNodes
	 * @param deceptionDuration
	 */
	public GroupedDeceptive(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations, int deceptiveNodes, int deceptionDuration) {
		
		this(graphController, "", numberOfHideLocations, deceptiveNodes, deceptionDuration);
		
	}
	
	/**
	 * @param graphController
	 * @param name
	 * @param numberOfHideLocations
	 * @param deceptiveNodes
	 * @param deceptionDuration
	 * @param repeatInterval
	 * @param repeatDuration
	 * @param refreshDeceptiveSet
	 */
	public GroupedDeceptive(
			GraphController <StringVertex, StringEdge> graphController, String name,
			int numberOfHideLocations, int deceptiveNodes, int deceptionDuration, int repeatInterval, int repeatDuration, boolean refreshDeceptiveSet) {
		
		super(graphController, numberOfHideLocations, deceptiveNodes, deceptionDuration, repeatInterval, repeatDuration, refreshDeceptiveSet);
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 * @param deceptiveNodes
	 * @param deceptionDuration
	 * @param repeatInterval
	 * @param repeatDuration
	 * @param refreshDeceptiveSet
	 */
	public GroupedDeceptive(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations, int deceptiveNodes, int deceptionDuration, int repeatInterval, int repeatDuration, boolean refreshDeceptiveSet) {
		
		this(graphController, "", numberOfHideLocations, deceptiveNodes, deceptionDuration, repeatInterval, repeatDuration, refreshDeceptiveSet);
		
	}

	/**
	 * @param graphController
	 * @param name
	 * @param numberOfHideLocations
	 * @param deceptiveNodes
	 * @param deceptionDuration
	 * @param repeatInterval
	 * @param repeatDuration
	 * @param deceptiveSets
	 */
	public GroupedDeceptive(
			GraphController <StringVertex, StringEdge> graphController, String name,
			int numberOfHideLocations, int deceptiveNodes, int deceptionDuration, 
			int repeatInterval, int repeatDuration, int deceptiveSets) {
		
		super(graphController, numberOfHideLocations, deceptiveNodes, deceptionDuration, repeatInterval, repeatDuration, deceptiveSets);
		
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 * @param deceptiveNodes
	 * @param deceptionDuration
	 * @param repeatInterval
	 * @param repeatDuration
	 * @param deceptiveSets
	 */
	public GroupedDeceptive(
			GraphController <StringVertex, StringEdge> graphController,
			int numberOfHideLocations, int deceptiveNodes, int deceptionDuration, 
			int repeatInterval, int repeatDuration, int deceptiveSets) {
		
		this(graphController, "", numberOfHideLocations, deceptiveNodes, deceptionDuration, repeatInterval, repeatDuration, deceptiveSets);
		
		
	}

	/**
	 * 
	 */
	private ArrayList<StringVertex> groupedNodesUsed;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.repeatgame.reactive.Deceptive#createDeceptiveSet(int)
	 */
	public void createDeceptiveSet(int deceptiveNodes) {
	
		// ~MDC This isn't neat...
		if ( deceptiveSetList == null ) deceptiveSetList = new ArrayList<ArrayList<StringVertex>>();
		
		if (groupedNodesUsed == null) groupedNodesUsed = new ArrayList<StringVertex>();
		
		deceptiveSetList.clear();
		
		ArrayList<StringEdge> edgesOfCurrentNode = new ArrayList<StringEdge>();
		
		for ( int i = 0; i < deceptiveSets; i++ ) {
			
			ArrayList<StringVertex> deceptiveSet = new ArrayList<StringVertex>();
			
			StringVertex currentNode = randomNode();
			
			if (groupedNodesUsed.size() > (graphController.vertexSet().size() - deceptiveNodes)) groupedNodesUsed = new ArrayList<StringVertex>();
			
			outerloop:
			while ( deceptiveSet.size() < deceptiveNodes ) {
				
				Utils.talk(toString(), "Trying: " + currentNode);
				
				if ( !deceptiveSet.contains(currentNode) && !groupedNodesUsed.contains(currentNode) ) {
					
					Utils.talk(toString(), "Adding: " + currentNode);
					
					deceptiveSet.add(currentNode);
					
				} else {
					
					currentNode = randomNode();
					
					deceptiveSet.clear();
					
					Utils.talk(toString(), currentNode + " already used, or already chosen in this set, exiting.");
					
					continue;
					
				}
				
				edgesOfCurrentNode.clear();
				
				Utils.talk(toString(), "Edges of " + currentNode + " : " + getConnectedEdges(currentNode));
				
				edgesOfCurrentNode.addAll(getConnectedEdges(currentNode));
				
				for ( StringEdge edge : edgesOfCurrentNode ) {
					
					if ( deceptiveSet.size() == deceptiveNodes ) break;
					
					StringVertex child = edge.getSource() == currentNode ? edge.getTarget() : edge.getSource();
					
					Utils.talk(toString(), "Trying child: " + child);
					
					if ( !deceptiveSet.contains(child) && !groupedNodesUsed.contains(child) ) {
					
						Utils.talk(toString(), "Adding child: " + child);
						
						deceptiveSet.add(child);
						
					} else {
						
						currentNode = randomNode();
						
						deceptiveSet.clear();
						
						Utils.talk(toString(), child + " already used, or already chosen in this set, exiting.");
						
						continue outerloop;
						
					}
					
				}
				
				Collections.shuffle(edgesOfCurrentNode);
				
				StringEdge randomEdge = edgesOfCurrentNode.get(0);
				
				if (randomEdge.getSource() == currentNode) {
					
					currentNode = randomEdge.getTarget();
				
				} else {
				
					currentNode = randomEdge.getSource();
				
				}
				
				if ( deceptiveSet.size() != deceptiveNodes ) Utils.talk(toString(), "Now going to: " + currentNode);
				
			}
			
			Utils.talk(toString(), "Final set: " + deceptiveSet);
	
			groupedNodesUsed.addAll(deceptiveSet);
			
			deceptiveSetList.add(deceptiveSet);
		
			populateDeceptiveSet(deceptiveSetList.get(i));
			
		}
		
		setDeceptiveSet(deceptiveSetList.get(0));
		
	}
	
	public void endOfGame() {
		
		groupedNodesUsed = new ArrayList<StringVertex>();
		
		super.endOfGame();
		
	}
	
}
