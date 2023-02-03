package org.kclhi.hands.seeker;

import org.kclhi.hands.utility.Utils;
import org.kclhi.hands.GraphTraverser;
import org.kclhi.hands.TraverserLocalGraph;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
* @author Martin
*
*/
public abstract class SeekingAgent extends TraverserLocalGraph implements Runnable, Seeker {
  
  /**
  * 
  */
  private int estimatedNumberOfHideLocations;
  
  /**
  * @return
  */
  protected int estimatedNumberOfHideLocations() {
    
    return estimatedNumberOfHideLocations;
    
  }
  /**
  * @param graphController
  */
  public SeekingAgent(GraphController <StringVertex, StringEdge> graphController) {
    
    this(graphController, "", null);
    
  }
  
  /**
  * @param graphController
  */
  public SeekingAgent(GraphController <StringVertex, StringEdge> graphController, String name) {
    
    this(graphController, name, null);
    
  }
  
  /**
  * @param graphController
  */
  public SeekingAgent(GraphController <StringVertex, StringEdge> graphController, GraphTraverser responsibleAgent) {
    
    this(graphController, "", responsibleAgent);
    
  }
  
  /**
  * @param graph
  */
  public SeekingAgent(GraphController<StringVertex, StringEdge> graphController, String name, GraphTraverser responsibleAgent) {
    
    super(graphController, name, responsibleAgent);
    
  }
  
  
  /**
  * Default: changing the number of expected locations each time.
  */
  private int hideLocationEstimateInterval = 1;
  
  /** 
  * If a seeker is not *permitted* access to the actual number of hide locations
  * by the controller, it must estimate this, on a specified basis, based
  * upon the number of hide locations most recently recorded.
  */
  protected void updateNumberOfHideLocationsEstimate() {
    
    if ( graphController.numberOfHideLocations(responsibleAgent) == -1 ) {
      
      if ( roundsPassed % hideLocationEstimateInterval == 0 && hideLocations().size() > 0 ) estimatedNumberOfHideLocations = hideLocations().size();
      
    } else {
      
      estimatedNumberOfHideLocations = graphController.numberOfHideLocations();
      
    }
    
  }
  
  /* (non-Javadoc)
  * @see java.lang.Object#toString()
  */
  public String toString() {
    
    return "s" + getName();
    
  }
  
  /* (non-Javadoc)
  * @see java.lang.Runnable#run()
  */
  @Override
  public void run() {
    
    Utils.talk(toString(), "Running " + getID() + " " + getName());
    
    updateNumberOfHideLocationsEstimate();
    
    search();
    
  }
  
  /**
  * What happens when a node is found
  * 
  * @param location
  */
  public void addHideLocation(StringVertex location) {
    
    Utils.talk(toString(), "----------------------------------Found " + location);
    
    hideLocations().add(location); 
    
    if ( allHideLocations().size() >= graphController.vertexSet().size() ) allHideLocations().clear();
    
    allHideLocations().add(location);
    
    if ( uniqueHideLocations().size() >= graphController.vertexSet().size() ) uniqueHideLocations().clear();
    
    uniqueHideLocations().add(location);
    
  }
  
  /**
  * @return
  */
  public boolean searchCriteria() {
    
    return hideLocations().size() != estimatedNumberOfHideLocations;
    
  }
  
  /**
  * @return
  */
  public void search() {
    
    StringVertex startNode = startNode();
    
    if ( currentNode != null && currentNode != startNode ) {
      
      graphController.walkPathFromVertexToVertex(responsibleAgent, currentNode, startNode);
      
    } 
    
    atStart(startNode);
    
    StringVertex nextNode = null;
    
    while ( searchCriteria() ) {
      
      atNode();
      
      if ( graphController.isHideLocation(responsibleAgent, currentNode) && !hideLocations().contains(currentNode) ) { 
        
        addHideLocation(currentNode);
        
        if (hideLocations().size() == estimatedNumberOfHideLocations) break;
        
      }
      
      nextNode = nextNode(currentNode);
      
      if ( !graphController.fromVertexToVertex(responsibleAgent, currentNode, nextNode) ) { 
        
        Utils.talk(toString(), "Error traversing supplied path: " + currentNode + " to " + nextNode + getStatus());
        
      } else {
        
        atNextNode(nextNode);
        
      }
      
    }
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.GraphTraversingAgent#printRoundStats()
  */
  @Override
  public String printRoundStats() {
    
    //System.out.println("Seeker cost: " + graphController.latestRoundCosts(responsibleAgent, false));
    
    return super.printRoundStats() + 
    "Cost," + graphController.latestRoundCosts(responsibleAgent, false) + 
    ", Explored," + exploredNodesSize() + 
    ", Path," + exploredNodesTable().toString().replace(",", "") +
    ", Success, " + graphController.successfulRoundTraversal(responsibleAgent);
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.GraphTraversingAgent#printGameStats()
  */
  @Override
  public String printGameStats() {
    
    return super.printGameStats() +
    "Cost, " + graphController.averageGameCosts(responsibleAgent) +  
    ", PathLength, " + graphController.averagePathLength(responsibleAgent) +
    ", Success, " + graphController.successfulGameTraversal(responsibleAgent);
    
  }
  
  /* (non-Javadoc)
  * @see HideAndSeek.GraphTraverser#endOfRound()
  */
  @Override
  public void endOfRound() {
    
    updateNumberOfHideLocationsEstimate();
    
    super.endOfRound();
    
    exploredNodesTable().clear();
    
    hideLocations().clear();
    
    queuedNodes.clear();
    
    Utils.talk(toString(), "uniqueHideLocations() " + uniqueHideLocations() + " UHL Size: " + uniqueHideLocations().size());
    
  }
  
}
