package org.kclhi.hands.seeker.singleshot.preference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

import org.kclhi.hands.utility.ComparatorResult;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
* Symmetrically mirrors the least connected strategy
* of a hider, in order to attempt to discern
* the same nodes.
* 
* @author Martin
*
*/
public class LeastConnected extends PreferenceSeeker {
  
  /**
  * @param graphController
  */
  public LeastConnected(GraphController<StringVertex, StringEdge> graphController, double graphPortion) {
    
    this(graphController, "", graphPortion);
    
  }
  
  /**
  * @param graphController
  */
  public LeastConnected(GraphController<StringVertex, StringEdge> graphController, String name, double graphPortion) {
    
    super(graphController, name, graphPortion);
    
  }
  
  /* (non-Javadoc)
  * @see org.kclhi.hands.seeker.singleshot.preference.PreferenceSeeker#computeTargetNodes()
  */
  @Override
  public LinkedHashSet<StringVertex> computeTargetNodes() {
    
    /* ~MDC 27/5 This is just a shorter way of doing what a Hider's least connected strategy does,
    * albeit this selects *all* nodes.
    */
    class NodeConnectivity implements Comparable<NodeConnectivity> {
      
      /**
      * 
      */
      private StringVertex node;
      
      /**
      * @return
      */
      public StringVertex getNode() {
        
        return node;
        
      }
      
      /**
      * @param node
      */
      public NodeConnectivity(StringVertex node) {
        
        this.node = node;
        
      }
      
      /**
      * @return
      */
      public int getNodeConnectivity() {
        
        return localGraph.edgesOf(node).size();
        
      }
      
      /* (non-Javadoc)
      * @see java.lang.Comparable#compareTo(java.lang.Object)
      */
      @Override
      public int compareTo(NodeConnectivity o) {
        
        if ( getNodeConnectivity() > o.getNodeConnectivity() ) {
          
          return ComparatorResult.AFTER;
          
        } else if ( getNodeConnectivity() < o.getNodeConnectivity() ) {
          
          return ComparatorResult.BEFORE;
          
        } else {
          
          return ComparatorResult.EQUAL;
          
        }
        
      }
      
    }
    
    ArrayList<NodeConnectivity> leastConnectedNodes = new ArrayList<NodeConnectivity>();
    
    for ( StringVertex graphVertex : localGraph.vertexSet() ) {
      
      leastConnectedNodes.add(new NodeConnectivity(graphVertex));
      
    }
    
    Collections.sort(leastConnectedNodes);
    
    LinkedHashSet<StringVertex> targetNodes = new LinkedHashSet<StringVertex>();
    
    for ( NodeConnectivity sortedVertex : leastConnectedNodes ) {
      
      targetNodes.add(sortedVertex.getNode());
      
    }
    
    return targetNodes;
    
  }
  
}
