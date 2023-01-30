package org.kclhi.hands.seeker.singleshot.random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.seeker.SeekingAgent;

/**
* @author Martin
*
*/
public class SelfAvoidingRandomWalkGreedy extends SeekingAgent {
  
  /**
  * @param graphController
  */
  public SelfAvoidingRandomWalkGreedy(
  GraphController <StringVertex, StringEdge> graphController) {
    
    super(graphController);
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.GraphTraversingAgent#nextNode(HideAndSeek.graph.StringVertex)
  */
  @Override
  public StringVertex nextNode(StringVertex currentNode) {
    
    return connectedNode(currentNode);
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.GraphTraversingAgent#startNode()
  */
  @Override
  public StringVertex startNode() {
    
    return randomNode();
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.GraphTraversingAgent#getConnectedEdges(HideAndSeek.graph.StringVertex)
  */
  public List<StringEdge> getConnectedEdges(StringVertex currentNode) {
    
    ArrayList<StringEdge> edges = new ArrayList<StringEdge>(super.getConnectedEdges(currentNode));
    
    Collections.sort(edges);
    
    return edges;
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.GraphTraversingAgent#getConnectedEdge(HideAndSeek.graph.StringVertex, java.util.List)
  */
  @Override
  public StringEdge getConnectedEdge(StringVertex currentNode, List<StringEdge> connectedEdges) {
    
    for (StringEdge edge : connectedEdges ) {
      
      if ( uniquelyVisitedNodes().contains(edgeToTarget(edge, currentNode)) ) continue;
      
      return edge;
      
    }
    
    return connectedEdges.get((int)(Math.random() * connectedEdges.size()));
    
  }
  
}
