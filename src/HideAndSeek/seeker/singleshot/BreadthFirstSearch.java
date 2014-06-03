package HideAndSeek.seeker.singleshot;

import java.util.ArrayList;
import java.util.List;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.Seeker;
import Utility.Utils;

/**
 * 
 * Note: There may be more efficient library implementations of these algorithms,
 * but these are tailored to the platform.
 * 
 * @author Martin
 *
 */
public class BreadthFirstSearch extends Seeker {

	/**
	 * @param graph
	 */
	public BreadthFirstSearch(
			GraphController <StringVertex, StringEdge> graphController) {

		super(graphController);
		
		parents = new ArrayList<StringVertex>();
		
		siblingSet = new ArrayList<StringVertex>();

	}

	/**
	 * 
	 */
	protected ArrayList<StringVertex> siblingSet;

	/**
	 * 
	 */
	protected ArrayList<StringVertex> parents;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	protected StringVertex nextNode(StringVertex currentNode) {

		Utils.talk(toString(), "Current node: " + currentNode + " Sibling set: " + siblingSet);
		
		// If we're at a sibling, need to return to parent to visit others
		if (siblingSet.contains(currentNode) && parents.size() > 0 && ( siblingSet.indexOf(currentNode) != siblingSet.size() - 1 )) {
			
			Utils.talk(toString(), "Next node: " + parents.get(parents.size() - 1) + " Sibling set: " + siblingSet);
			
			return parents.get(parents.size() - 1);
			
		}
		
		// Otherwise (at parent), check if we have any unvisited siblings
		for ( StringVertex sibling : siblingSet ) {
			
			if ( !uniquelyVisitedNodes.contains(sibling) ) {
				
				Utils.talk(toString(), "Next node: " + sibling + " Sibling set: " + siblingSet);
				
				//siblingSet.remove(sibling);
				
				return sibling;
				
			}
			
		}
		
		/* If we reach here, all siblings have been visited. Create new sibling set by
		   examining the children of each current sibling. */
		for ( StringVertex sibling : siblingSet ) {
			
			// Get the edges from this sibling
			List<StringEdge> siblingEdges = getConnectedEdges(sibling);
			
			ArrayList<StringVertex> newSiblingSet = new ArrayList<StringVertex>();
			
			for ( StringEdge siblingEdge : siblingEdges ) {
				
				// Get the children of this sibling, from the edges
				StringVertex newSibling = edgeToTarget(siblingEdge, sibling);
				
				// If the child hasn't been visited, it becomes part of the new sibling set
				if ( !uniquelyVisitedNodes.contains(newSibling) ) {
				
					newSiblingSet.add(newSibling);
				
				}
					
			}
			
			// If there are unvisited nodes below this sibling, they become the new sibling set
			if (newSiblingSet.size() > 0) {
				
				Utils.talk(toString(), "Sibling " + sibling);
				
				Utils.talk(toString(), "New Sibling set " + newSiblingSet);
				
				// Add the parent of the new sibling set
				parents.add(sibling);
				
				siblingSet = newSiblingSet;
				
				Utils.talk(toString(), "Next node: " + siblingSet.get(0) + " Sibling set: " + siblingSet);
				
				return siblingSet.get(0);
				
			}
			
		}
		
		Utils.talk(toString(), "Next node: " + parents.get(parents.size() - 1) + " Sibling set: " + siblingSet);
		
		// No where else to go but back up tree
		return parents.remove(parents.size() - 1);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#startNode()
	 */
	@Override
	protected StringVertex startNode() {
		
		StringVertex startNode = randomNode();
		
		siblingSet.add(startNode);
		
		return startNode;
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.Seeker#endOfRound()
	 */
	@Override
	public void endOfRound() {
		// TODO Auto-generated method stub
		super.endOfRound();
		
		parents.clear();
		
	}
	
}
