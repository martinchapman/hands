package org.kclhi.hands.seeker.singleshot.coverage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.kclhi.hands.utility.Utils;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.seeker.SeekingAgent;

/**
* 
* Note: There may be more efficient library implementations of these algorithms,
* but these are tailored to the platform.
* 
* @author Martin
*
*/
public class DepthFirstSearch extends SeekingAgent {
  
  /**
  * @param graph
  */
  public DepthFirstSearch( GraphController <StringVertex, StringEdge> graphController ) {
    
    super(graphController);
    
    currentBranch = new ArrayList<StringEdge>();
    
  }
  
  /**
  * 
  */
  protected ArrayList<StringEdge> currentBranch;
  
  /* (non-Javadoc)
  * @see HideAndSeek.GraphTraverser#nextNode(HideAndSeek.graph.StringVertex)
  */
  @Override
  public StringVertex nextNode(StringVertex currentNode) {
    
    List<StringEdge> connectedEdges = getConnectedEdges(currentNode);
    
    StringEdge connectedEdge = null;
    
    StringVertex target = null;
    
    HashSet<StringEdge> selectedInThisSession = new HashSet<StringEdge>();
    
    // For each possible outgoing edge:
    for ( StringEdge edge : connectedEdges ) {
      
      // Check if it leads to a node which has already been visited
      if ( !uniquelyVisitedNodes().contains( edgeToTarget( edge, currentNode ) ) ) {
        
        connectedEdge = edge;
        
        // Edge found, break from loop.
        break;
        
      }
      
    }
    
    if ( connectedEdge == null ) {
      
      if ( currentBranch.size() > 0 ) {
        
        Utils.talk(toString(), "Going back up through branch from " + currentNode + " to " + edgeToTarget( currentBranch.get(currentBranch.size() - 1), currentNode ));
        
        // If all edges have been traversed, move upwards through the current branch (if it is not empty)
        return edgeToTarget( currentBranch.remove(currentBranch.size() - 1), currentNode );
        
      } else {
        
        /* 
        * Because there should be no more to explore once all branches have been exhausted,
        * this should not be called.
        */
        System.err.println("Error: No backtrack branch: " + getStatus());
        
        return connectedNode(currentNode);
        
      }
      
    } else {
      
      target = edgeToTarget(connectedEdge, currentNode);
      
      selectedInThisSession.add(connectedEdge);
      
    }
    
    currentBranch.add(connectedEdge);
    
    Utils.talk(toString(), "Next node in DFS: " + target);
    
    return target;
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.GraphTraversingAgent#getStatus()
  */
  public String getStatus() {
    
    return super.getStatus() + "\nCurrent branch: " + currentBranch;
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.GraphTraverser#startNode()
  */
  @Override
  public StringVertex startNode() {
    
    return randomNode();
    
  }
  
  /* (non-Javadoc)
  * @see org.kclhi.hands.seeker.Seeker#endOfRound()
  */
  @Override
  public void endOfRound() {
    
    super.endOfRound();
    
    currentBranch.clear();
    
  }
  
}
