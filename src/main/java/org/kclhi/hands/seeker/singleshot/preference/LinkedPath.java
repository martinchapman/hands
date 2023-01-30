package org.kclhi.hands.seeker.singleshot.preference;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;

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
public class LinkedPath extends SeekingAgent implements VariableTraversalStrategy {
  
  /**
  * @deprecated
  */
  private int currentNodeGap = 0;
  
  /**
  * @deprecated
  */
  private int specifiedNodeGap;
  
  /**
  * 
  */
  private OpenTraverserStrategy explorationMechanism;
  
  private ArrayList<StringVertex> potentialNodes;
  
  private ArrayList<StringEdge> currentPath;
  
  private ArrayList<StringVertex> visitedNonExplorative;
  
  private ArrayList<StringVertex> hideLocationPath;
  
  private StringVertex firstNode;
  
  private boolean isFound;
  
  /**
  * @param graphController
  * @param specifiedNodeGap
  */
  public LinkedPath(GraphController<StringVertex, StringEdge> graphController) {
    
    this(graphController, 1);
    
  }
  
  /**
  * @param graphController
  */
  public LinkedPath(GraphController<StringVertex, StringEdge> graphController, int specifiedNodeGap) {
    
    super(graphController);
    
    explorationMechanism = getExplorationMechanism(responsibleAgent);
    
    potentialNodes = new ArrayList<StringVertex>();
    
    currentPath = new ArrayList<StringEdge>();
    
    visitedNonExplorative = new ArrayList<StringVertex>();
    
    hideLocationPath = new ArrayList<StringVertex>();
    
    firstNode = null;
    
    isFound = false;
    
    this.specifiedNodeGap = specifiedNodeGap;
    
  }
  
  @Override
  public OpenTraverserStrategy getExplorationMechanism(GraphTraverser responsibleAgent) {
    
    return new BacktrackGreedyMechanism(graphController, responsibleAgent);
    
  }
  
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
  
  /* (non-Javadoc)
  * @see HideAndSeek.GraphTraversingAgent#atNextNode(HideAndSeek.graph.StringVertex)
  */
  public void atNextNode(StringVertex nextNode) {
    
    super.atNextNode(nextNode);
    
    explorationMechanism.atNextNode(nextNode);
    
  }
  
  /* (non-Javadoc)
  * @see org.kclhi.hands.seeker.SeekingAgent#endOfRound()
  */
  public void endOfRound() {
    
    super.endOfRound();
    
    explorationMechanism.endOfRound();
    
    potentialNodes.clear();
    
    currentPath.clear();
    
    visitedNonExplorative.clear();
    
    hideLocationPath.clear();
    
    firstNode = null;
    
    isFound = false;
    
  }
  
  /* (non-Javadoc)
  * @see org.kclhi.hands.seeker.SeekingAgent#endOfGame()
  */
  public void endOfGame() {
    
    super.endOfGame();
    
    explorationMechanism.endOfGame();
    
  }
  
  
  /* (non-Javadoc)
  * @see HideAndSeek.GraphTraversingAgent#nextNode(HideAndSeek.graph.StringVertex)
  */
  @Override
  public StringVertex nextNode(StringVertex currentNode) {
    
    // If we are still exploring... 
    if ( super.hideLocations().size() == 0 ) {
      
      return explorationMechanism.nextNode(currentNode);
      
    } else {
      
      // Nodes we have already been to, that weren't visited during the exploration period
      visitedNonExplorative.add(currentNode);
      
      // Record first node found to contain an object
      if ( firstNode == null ) firstNode = hideLocations().get(0);
      
      // If we have reached the node connected to the previous, clear potentials
      // And we've only been here once before, to avoid cycles
      if ( hideLocations().contains(currentNode) && exploredNodesTable().containsKey(currentNode) && exploredNodesTable().get(currentNode) == 1) {
        
        potentialNodes.clear();
        
        currentPath.clear();
        
        isFound = true;
        
        hideLocationPath.add(currentNode);
        
      }
      
      if ( potentialNodes.contains(currentNode) ) potentialNodes.remove(currentNode);
      
      // If we're already on the DSP to a node, continue on it
      if ( currentPath.size() > 0 ) { 
        
        Utils.talk(toString(), "On shortest path.");
        
        return edgeToTarget(currentPath.remove(0), currentNode);
        
      }
      
      if ( potentialNodes.size() == 0 && isFound == true ) {
        
        for ( StringEdge connectedEdge : getConnectedEdges(currentNode) ) {
          
          if ( !visitedNonExplorative.contains(edgeToTarget(connectedEdge, currentNode)) && !hideLocations().contains(edgeToTarget(connectedEdge, currentNode)) ) {
            
            Utils.talk(toString(), currentNode + " --> " + edgeToTarget(connectedEdge, currentNode));
            
            potentialNodes.add(edgeToTarget(connectedEdge, currentNode));
            
          }
          
        }
        
        isFound = false;
        
      }
      
      DijkstraShortestPath<StringVertex, StringEdge> dsp;
      
      if ( potentialNodes.size() > 0 ) {
        
        dsp = new DijkstraShortestPath<StringVertex, StringEdge>(localGraph, currentNode, potentialNodes.remove(0));
        
        // Abstracted to return in the pseudocode
      } else if ( hideLocationPath.size() > 1 ) {
        
        isFound = true;
        
        hideLocationPath.remove(hideLocationPath.size() - 1);
        
        Utils.talk(toString(), "Bactracking to: " + hideLocationPath.get(hideLocationPath.size() - 1));
        
        dsp = new DijkstraShortestPath<StringVertex, StringEdge>(localGraph, currentNode, hideLocationPath.remove(hideLocationPath.size() - 1));
        
      } else {
        
        Utils.talk(toString(), "Exploring because all path links exhausted.");
        
        return explorationMechanism.nextNode(currentNode);
        
      }
      
      // If no path available, then return to exploring
      if ( dsp.getPathEdgeList() == null || dsp.getPathEdgeList().size() == 0 ) { 
        
        return explorationMechanism.nextNode(currentNode);
        
      }
      
      currentPath = new ArrayList<StringEdge>(dsp.getPathEdgeList());
      
      return edgeToTarget(currentPath.remove(0), currentNode);
      
    }
    
  }
  
  /**
  * @deprecated
  * @return
  */
  private StringVertex withGap() {
    
    /* 
    * If we there are not the required number
    * of nodes between the current location, 
    * and the last known hide position, increment
    * the gap by choosing a random unvisited node
    */
    if ( currentNodeGap < specifiedNodeGap ) {
      
      currentNodeGap++;
      
      return connectedNode(currentNode);
      
      /* 
      * If we now have an appropriate gap, look for the next hide location.
      * If found, move to it. (~MDC 24/4 will not work with new constraints).
      */
    } else {
      
      
      
    }
    
    /* 
    * If no hide location is found, decrement the gap, so we can increment it again
    * and try with a new and unvisited node.
    */
    currentNodeGap = currentNodeGap > 0 ? currentNodeGap - 1 : 0;
    
    return null;
    
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
    
    return explorationMechanism.getConnectedEdges(currentNode);
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.GraphTraversingAgent#getConnectedEdge(HideAndSeek.graph.StringVertex, java.util.List)
  */
  public StringEdge getConnectedEdge(StringVertex currentNode, List<StringEdge> connectedEdges) {
    
    return explorationMechanism.getConnectedEdge(currentNode, connectedEdges);
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.GraphTraversingAgent#connectedNode(HideAndSeek.graph.StringVertex)
  */
  public StringVertex connectedNode(StringVertex currentNode) {
    
    return explorationMechanism.connectedNode(currentNode);
    
  }
  
}
