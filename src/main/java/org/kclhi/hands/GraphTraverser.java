package org.kclhi.hands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

public interface GraphTraverser extends Comparable<GraphTraverser>, Runnable {
  
  /**
  * @return
  */
  public GraphController<?, ?> getGraphController();
  
  /**
  * @param responsibleAgent
  */
  public abstract void setResponsibleAgent(GraphTraverser responsibleAgent);
  
  /**
  * @return
  */
  public GraphTraverser getResponsibleAgent();
  
  /**
  * @return
  */
  public abstract HashSet<StringVertex> uniquelyVisitedNodes();
  
  /**
  * @return
  */
  public abstract HashSet<StringEdge> uniquelyVisitedEdges();
  
  /**
  * @return
  */
  public ArrayList<StringVertex> allHideLocations();
  
  /**
  * @return
  */
  public HashSet<StringVertex> uniqueHideLocations();
  /**
  * @return
  */
  public abstract boolean strategyOverRounds();
  
  /**
  * @param strategyOverRounds
  */
  public abstract void strategyOverRounds(boolean strategyOverRounds);
  
  /**
  * @return
  */
  public abstract boolean isPlaying();
  
  /**
  * 
  */
  public abstract void startPlaying();
  
  /**
  * @param currentNode
  * @return
  */
  public abstract StringVertex nextNode(StringVertex currentNode);
  
  /**
  * @return
  */
  public abstract StringVertex startNode();
  
  /**
  * 
  */
  public abstract void endOfRound();
  
  /**
  * 
  */
  public abstract void endOfGame();
  
  /**
  * @return
  */
  public abstract String printGameStats();
  
  /**
  * @return
  */
  public abstract String printRoundStats();
  
  /**
  * @return
  */
  public int roundsPassed();
  
  /**
  * @return
  */
  public StringVertex currentNode();
  
  /**
  * @return
  */
  public Hashtable<StringVertex, Integer> exploredNodesTable();
  
  /**
  * @return
  */
  public int exploredNodesSize();
  
  /**
  * @return
  */
  public ArrayList<StringVertex> requestHideLocations(GraphTraverser caller);
  
  /**
  * @param location
  */
  public void addHideLocation(StringVertex location);
  
  /**
  * 
  */
  public void mergeOtherTraverser(GraphTraverser traverser);
  
  /**
  * @return
  */
  public String getName();
  
}
