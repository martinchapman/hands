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
import HideAndSeek.seeker.SeekingAgent;
import Utility.Utils;

/**
 * Original by Alpern and Gal.
 * 
 * @author Martin
 *
 */
public class RandomTarry extends SeekingAgent {

	/**
	 * 
	 */
	Hashtable<StringVertex, StringVertex> entryEdges;
	
	/**
	 * 
	 */
	Hashtable<StringVertex, ArrayList<StringVertex>> exitEdges;
	
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

		entryEdges = new Hashtable<StringVertex, StringVertex>();

		exitEdges = new Hashtable<StringVertex, ArrayList<StringVertex>>();
		
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
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.SeekingAgent#printGameStats()
	 */
	public String printGameStats() {
		
		return super.printGameStats() + ",Total Edge Cost," + graphController.totalEdgeWeight();
		
	}
	
	/**
	 * 
	 */
	private int backtracks = 0;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.SeekingAgent#nextNode(HideAndSeek.graph.StringVertex)
	 */
	public StringVertex nextNode(StringVertex currentNode) {
	
		Utils.talk(toString(), "At (nextNode) " + currentNode);
		
		if ( !this.exitEdges.keySet().contains(currentNode) ) {
			
			Utils.talk(toString(), "First time at this node, adding all edges.");
			
			ArrayList<StringVertex> exitEdges = new ArrayList<StringVertex>();
			
			for (StringEdge edge : this.getConnectedEdges(currentNode)) {
				
				// Do not add the 'yellow' entry edge
				if ( entryEdges.containsKey(currentNode) && edgeToTarget(edge,currentNode).equals(entryEdges.get(currentNode)) ) { 
					
					Utils.talk(toString(), "Skipping " + edge + " because it is a leaving edge (Check, leaving edge shoudl be: " + this.entryEdges.get(currentNode) + ")");
					
					continue;
				}
				
				Utils.talk(toString(), "Setting " + edge + " as new edge leaving " + currentNode + " marking as not followed.");
				
				exitEdges.add(edgeToTarget(edge, currentNode));
				
			}
			
			Collections.shuffle(exitEdges);
			
			Utils.talk(toString(), "First time permuation: " + exitEdges);
			
			this.exitEdges.put(currentNode, exitEdges);
			
		}
		
		// Go to the first unvisited (shuffled) edge
		for ( StringVertex vertex : this.exitEdges.get(currentNode) ) {
			
			Utils.talk(toString(), vertex + " of " + currentNode + " is not visited. Going to this.");
			
			return vertex;
			
		}
		
		Utils.talk(toString(), "All edges of " + currentNode + " already traversed");
		
		// If we are here, no 'uncoloured' edges left, check if a yellow edge exists, which has not been coloured red previously
		if ( this.entryEdges.containsKey(currentNode) ) {
			
			Utils.talk(toString(), "Yellow edge " + this.entryEdges.get(currentNode) + " leaving " + currentNode + " still not re-traversed, taking this.");
			
			backtracks++;
			
			Utils.talk(toString(), "Marking " + this.entryEdges.get(currentNode) + " as visited (turning it red).");
			
			StringVertex yellowTarget = entryEdges.remove(currentNode);
			
			return yellowTarget;
			
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
		
		ArrayList<StringVertex> lastNodeExistEdges = exitEdges.get(currentNode);
		
		Utils.talk(toString(), "Setting the edge " + graphController.getEdge(currentNode, nextNode) + " as visited in " + currentNode);
		
		lastNodeExistEdges.remove(nextNode);
		
		if ( !entryEdges.containsKey(nextNode) && !(nextNode == startNode) ) {
			
			Utils.talk(toString(), "First time visiting this node. Exit edge is the one just traversed " + graphController.getEdge(currentNode, nextNode));
			
			entryEdges.put(nextNode, currentNode);
			
		} else if ( !(nextNode == startNode) ) {
			
			Utils.talk(toString(), nextNode + " (this) already has an exit edge. Is: " + entryEdges.get(nextNode));
			
		} else {
			
			Utils.talk(toString(), "Back at starting edge");
			
		}
		
		super.atNextNode(nextNode);
		
	}

}
