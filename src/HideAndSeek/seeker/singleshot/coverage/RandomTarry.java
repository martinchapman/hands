package HideAndSeek.seeker.singleshot.coverage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import HideAndSeek.GraphTraverser;
import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.SeekerLocalGraph;
import Utility.Utils;

/**
 * Original by Alpern and Gal.
 * 
 * @author Martin
 *
 */
public class RandomTarry extends SeekerLocalGraph {

	/**
	 * 
	 */
	Hashtable<StringVertex, StringEdge> entryEdges;
	
	/**
	 * 
	 */
	Hashtable<StringVertex, LinkedHashMap<StringEdge, Boolean>> exitEdges;
	
	/**
	 * 
	 */
	protected boolean automaticMove() {
		
		return false;
		
	}
	
	/**
	 * @param graphController
	 */
	public RandomTarry(GraphController<StringVertex, StringEdge> graphController) {
		
		this(graphController, null);
		
	}

	/**
	 * @param graphController
	 * @param responsibleAgent
	 */
	public RandomTarry(GraphController<StringVertex, StringEdge> graphController, GraphTraverser responsibleAgent) {
		
		super(graphController, responsibleAgent);

		entryEdges = new Hashtable<StringVertex, StringEdge>();

		exitEdges = new Hashtable<StringVertex, LinkedHashMap<StringEdge, Boolean>>();
		
	}

	/**
	 * Record start node as (it seems) this should not be given an exiting edge
	 */
	private StringVertex startNode;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#startNode()
	 */
	@Override
	public StringVertex startNode() {

		startNode = randomNode();
		
		return startNode;
		
	}
	
	private int backtracks = 0;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.SeekerLocalGraph#nextNode(HideAndSeek.graph.StringVertex)
	 */
	public StringVertex nextNode(StringVertex currentNode) {
	
		Utils.talk(toString(), "At (nextNode) " + currentNode);
		
		if ( !this.exitEdges.keySet().contains(currentNode) ) {
			
			Utils.talk(toString(), "First time at this node, adding all edges.");
			
			LinkedHashMap<StringEdge, Boolean> exitEdges = new LinkedHashMap<StringEdge, Boolean>();
			
			for (StringEdge edge : this.getConnectedEdges(currentNode)) {
				
				// Do not add the 'yellow' entry edge
				if ( this.entryEdges.contains(currentNode) && edge == this.entryEdges.get(currentNode) ) { 
					
					Utils.talk(toString(), "Skipping " + edge + " because it is a leaving edge (Check, leaving edge shoudl be: " + this.entryEdges.get(currentNode) + ")");
					
					continue;
				}
				
				Utils.talk(toString(), "Setting " + edge + " as new edge leaving " + currentNode + " marking as not followed.");
				
				exitEdges.put(edge, false);
				
			}
			
			// Applies fixed permutation
			ArrayList<Entry<StringEdge, Boolean>> listOfExitEdges = new ArrayList<Entry<StringEdge, Boolean>>(exitEdges.entrySet());
			
			Collections.shuffle(listOfExitEdges);
			
			exitEdges.clear();
			
			// Messily adds them back to main list
			for ( Entry<StringEdge, Boolean> edgeEntry : listOfExitEdges ) {
				
				exitEdges.put(edgeEntry.getKey(), edgeEntry.getValue());
				
			}
			
			this.exitEdges.put(currentNode, exitEdges);
			
		}
		
		// Go to the first unvisited (shuffled) edge
		for ( Entry<StringEdge, Boolean> edge : this.exitEdges.get(currentNode).entrySet() ) {
			
			if ( edge.getValue() == false ) { 
				
				Utils.talk(toString(), edge + " of " + currentNode + " is not visited. Going to this.");
				
				return edgeToTarget(edge.getKey(), currentNode);
			
			} else {
				
				Utils.talk(toString(), edge + " of " + currentNode + " is visited. Cannot go to this.");
				
			}
			
		}
		
		Utils.talk(toString(), "All edges of " + currentNode + " already traversed");
		
		// If we are here, no 'uncoloured' edges left, check if a yellow edge exists, which has not been coloured red previously
		if ( this.entryEdges.contains(currentNode) && !this.exitEdges.get(currentNode).containsKey(entryEdges.get(currentNode)) ) {
			
			Utils.talk(toString(), "Yellow edge " + this.entryEdges.get(currentNode) + " leaving " + currentNode + " still not re-traversed, taking this.");
			
			backtracks++;
			
			// Add the yellow edge to red
			this.exitEdges.get(currentNode).put(entryEdges.get(currentNode), true);
		
			Utils.talk(toString(), "Marking " + this.entryEdges.get(currentNode) + " as visited (turning it red).");
			
			return edgeToTarget(entryEdges.get(currentNode), currentNode);
			
		}
		
		Utils.talk(toString(), "All edges red. Uhoh.");
		
		throw new UnsupportedOperationException("!" + this.getStatus() + "start node: " + startNode + " backtracks: " + backtracks + " exit edge " + entryEdges.get(currentNode));
		
		//return connectedNode(currentNode);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#atNextNode(HideAndSeek.graph.StringVertex)
	 */
	public void atNextNode(StringVertex nextNode) {
		
		Utils.talk(toString(), "At (atNextNode) " + nextNode + " come from " + currentNode);
		
		LinkedHashMap<StringEdge, Boolean> lastNodeExistEdges = exitEdges.get(currentNode);
		
		Utils.talk(toString(), "Setting the edge " + graphController.getEdge(currentNode, nextNode) + " as visited in " + currentNode);
		
		lastNodeExistEdges.put(graphController.getEdge(currentNode, nextNode), true);
		
		if ( !entryEdges.containsKey(nextNode) ) { //&& !(nextNode == startNode) ) {
			
			Utils.talk(toString(), "First time visiting this node. Exit edge is the one just traversed " + graphController.getEdge(currentNode, nextNode));
			
			entryEdges.put(nextNode, graphController.getEdge(currentNode, nextNode));
			
		} else if ( !(nextNode == startNode) ) {
			
			Utils.talk(toString(), nextNode + " (this) already has an exit edge. Is: " + entryEdges.get(nextNode));
			
		} else {
			
			Utils.talk(toString(), "Back at starting edge");
			
		}
		
		super.atNextNode(nextNode);
		
	}

}
