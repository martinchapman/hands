package org.kclhi.hands.seeker.singleshot.preference;

import java.util.List;

import org.kclhi.hands.OpenTraverserStrategy;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
* @deprecated Not viable with graph exploration
* @author Martin
*
*/
public class MostConnectedFirstMechanism extends MostConnectedFirst implements OpenTraverserStrategy {
  
  
  /**
  * @param graphController
  */
  public MostConnectedFirstMechanism( GraphController<StringVertex, StringEdge> graphController ) {
    
    super(graphController);
    
  }
  
  /* (non-Javadoc)
  * @see org.kclhi.hands.seeker.singleshot.LeastConnectedFirst#getConnectedEdges(HideAndSeek.graph.StringVertex)
  */
  @Override
  public List<StringEdge> getConnectedEdges(StringVertex currentNode) {
    
    return super.getConnectedEdges(currentNode);
    
  }
  
  /* (non-Javadoc)
  * @see org.kclhi.hands.seeker.singleshot.preference.LeastConnectedFirst#getConnectedEdge(HideAndSeek.graph.StringVertex, java.util.List)
  */
  @Override
  public StringEdge getConnectedEdge(StringVertex currentNode, List<StringEdge> connectedEdges) {
    
    return super.getConnectedEdge(currentNode, connectedEdges);
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.GraphTraversingAgent#connectedNode(HideAndSeek.graph.StringVertex)
  */
  public StringVertex connectedNode(StringVertex currentNode) {
    
    return super.connectedNode(currentNode);
    
  }
  
  public void atStart(StringVertex startNode) {
    
    super.atStart(startNode);
    
  }
  
  /* (non-Javadoc)
  * @see org.kclhi.hands.seeker.SeekingAgent#atNode()
  */
  public void atNode() {
    
    super.atNode();
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.GraphTraversingAgent#nextNodeAccepted(HideAndSeek.graph.StringVertex)
  */
  public void atNextNode(StringVertex nextNode) {
    
    super.atNextNode(nextNode);
    
  }
  
}
