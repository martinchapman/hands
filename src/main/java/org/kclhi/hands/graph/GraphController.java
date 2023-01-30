package org.kclhi.hands.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.VertexFactory;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.generate.CompleteGraphGenerator;
import org.jgrapht.generate.GraphGenerator;
import org.jgrapht.generate.RandomGraphGenerator;
import org.jgrapht.generate.RingGraphGenerator;
import org.jgrapht.generate.ScaleFreeGraphGenerator;
import org.jgrapht.graph.ClassBasedVertexFactory;

import org.kclhi.hands.utility.Metric;
import org.kclhi.hands.utility.Utils;
import org.kclhi.hands.AdaptiveGraphTraversingAgent;
import org.kclhi.hands.GraphTraverser;
import org.kclhi.hands.Main;
import org.kclhi.hands.hider.Hider;
import org.kclhi.hands.seeker.Seeker;

/**
* Encapsulates search graph, controlling the amount of
* information that is released to hiders and searchers.
*
* @author Martin
*
* @param <V>
* @param <E>
*/
public class GraphController<V , E> {
  
  /**
  * 
  */
  private HiddenObjectGraph<StringVertex, StringEdge> graph;
  
  /**
  * 
  */
  private TopologyProperties<StringVertex, StringEdge> topologyProperties;
  
  /**
  * 
  */
  private double fixedOrUpperValue;
  
  /**
  * @return
  */
  public double getFixedOrUpperValue() {
    
    return fixedOrUpperValue;
    
  }
  
  /**
  * 
  */
  private String fixedOrUpperBound;
  
  /**
  * @return
  */
  public String getFixedOrUpperBound() {
    
    return fixedOrUpperBound;
    
  }
  
  /**
  * 
  */
  private int edgeTraversalValue;
  
  /**
  * @return
  */
  public int getEdgeTraversalValue() {
    
    return edgeTraversalValue;
    
  }
  
  /* (non-Javadoc)
  * @see java.lang.Object#toString()
  */
  public String toString() {
    
    return "GraphController";
    
  }
  
  /**
  * @param graph
  */
  public GraphController(String topology, int numberOfVertices, int numberOfHideLocations, String fixedOrUpperBound, double fixedOrUpperValue, int edgeTraversalDecrement) {
    
    this.fixedOrUpperValue = fixedOrUpperValue;
    
    this.fixedOrUpperBound = fixedOrUpperBound;
    
    this.edgeTraversalValue = edgeTraversalDecrement;
    
    this.numberOfHideLocations = numberOfHideLocations;
    
    /**************************
    * 
    * Set up search graph
    * 
    * * * * * * * * * * * * */
    
    graph = new HiddenObjectGraph<StringVertex, StringEdge>(StringEdge.class);
    
    graph.setEdgeTraversalDecrement(edgeTraversalDecrement);
    
    generateGraph(topology, numberOfVertices);
    
  }
  
  /**
  * Reset (or create) the graph
  * 
  * @param topology
  * @param numberOfVertices
  */
  public void generateGraph(String topology, int numberOfVertices) {
    
    ConnectivityInspector<StringVertex, StringEdge> con = null;
    
    int edges = numberOfVertices * 3;
    
    int vertices = numberOfVertices >= 2 ? numberOfVertices : 2;
    
    boolean incrementingEdges = false;
    
    do {
      
      removeAllEdges(graph);
      
      removeAllVertices(graph);
      
      // ~MDC 22/1/15 Messy?
      StringVertex.resetNodes();
      
      GraphGenerator<StringVertex, StringEdge, StringVertex> generator = null;
      
      // Select generator
      if (	topology.equals("ring")	  ) {
        
        generator = new RingGraphGenerator<StringVertex, StringEdge>(vertices);
        
      } else if (	   topology.equals("random")    ) {
        
        generator = new RandomGraphGenerator<StringVertex, StringEdge>(vertices, edges);
        
      } else if (    topology.equals("scalefree")    ) {
        
        generator = new ScaleFreeGraphGenerator<StringVertex, StringEdge>(vertices);
        
      } else if (    topology.equals("complete") ) {
        
        generator = new CompleteGraphGenerator<StringVertex, StringEdge>(vertices);
        
      }
      
      // Generate graph
      
      Utils.talk("Graph Controller", "Generating graph.");
      
      VertexFactory<StringVertex> factory = new ClassBasedVertexFactory<StringVertex>(StringVertex.class);
      
      try {
        
        Utils.talk(toString(), "Trying graph generator with " + edges + " edges");
        
        generator.generateGraph(graph, factory, null);
        
        con = new ConnectivityInspector<StringVertex, StringEdge>(graph);
        
        if ( !con.isGraphConnected() && incrementingEdges ) throw new IllegalArgumentException();
        
      } catch (IllegalArgumentException e) {
        
        edges = incrementingEdges == false ? 1 : edges + 1;
        
        if ( edges > 100 ) {
          
          edges = 1;
          
          vertices++;
          
          Utils.talk(toString(), "Incrementing vertices");
          
        }
        
        incrementingEdges = true;
        
        continue;
        
      }
      
    } while ( con == null || !con.isGraphConnected() );
    
    topologyProperties = new TopologyProperties<StringVertex, StringEdge>(topology, graph);
    
    // Assign nodes types
    
    graph.setNodeTypes(new char[]{ 'A', 'B', 'C' });
    
    // Set edge weights
    
    for ( StringEdge edge : graph.edgeSet() ) {
      
      if ( fixedOrUpperBound.equals("fixed") ) {
        
        graph.setEdgeWeight(edge, fixedOrUpperValue);
        
        // Explicit bi-directionality needed for shortest path algorithm
        graph.addEdgeWithWeight(edge.getTarget(), edge.getSource(), fixedOrUpperValue);
        
      } else if (fixedOrUpperBound.equals("random")) {
        
        Double value = Math.random() * fixedOrUpperValue;
        
        graph.setEdgeWeight(edge, value);
        
        graph.addEdgeWithWeight(edge.getTarget(), edge.getSource(), value);
        
      }
      
    }
    
    Utils.talk("Graph Controller", "Graph generated (" + topology + "). Edges: " + graph.edgeSet().size() + "\n" + graph.edgeSet());
    
  }
  
  /**
  * @param graph
  */
  public void removeAllEdges(HiddenObjectGraph<StringVertex, StringEdge> graph) {
    
    LinkedList<StringEdge> copy = new LinkedList<StringEdge>();
    
    for ( StringEdge e : graph.edgeSet() ) {
      
      copy.add(e);
      
    }
    
    graph.removeAllEdges(copy);
    
  }
  
  /**
  * @param graph
  */
  public void removeAllVertices(HiddenObjectGraph<StringVertex, StringEdge> graph) {
    
    LinkedList<StringVertex> copy = new LinkedList<StringVertex>();
    
    for ( StringVertex v : graph.vertexSet() ) {
      
      copy.add(v);
      
    }
    
    graph.removeAllVertices(copy);
    
  }
  
  /**
  * @return Get information on the topology generated for the current game
  */
  public TopologyProperties<StringVertex, StringEdge> getTopologyProperties() {
    
    return topologyProperties;
    
  }
  
  /*
  * Private utility methods only available to Main class
  */
  
  /**
  * @param caller
  */
  public void clearHideLocations(Object caller) {
    
    if (!(caller instanceof Main)) return;
    
    graph.clearHideLocations();
    
  }
  
  /**
  * @param caller
  * @param seeker
  * @return
  */
  public ArrayList<StringVertex> latestRoundPaths(Object caller, Seeker seeker) {
    
    if (!(caller instanceof Main)) return null;
    
    return graph.latestRoundPaths(seeker);
    
  }
  
  /**
  * @param caller
  */
  public void notifyEndOfRound(Object caller) {
    
    if (!(caller instanceof Main)) return;
    
    graph.notifyEndOfRound();
    
  }
  
  /**
  * @param caller
  * @param hider
  * @return
  */
  public double latestHiderRoundPayoffs(Object caller, Hider hider) {
    
    if (!(caller instanceof Main)) return -1;
    
    return graph.latestHiderRoundPayoffs(hider);
    
  }
  
  /**
  * @param caller
  */
  public void resetGameEnvironment(Object caller) {
    
    if (!(caller instanceof Main)) return;
    
    graph.resetGameEnvironment();
    
  }
  
  /**
  * @param caller
  * @return
  */
  public Set<StringEdge> edgeSet(Object caller) {
    
    if (!(caller instanceof Main)) return null;
    
    return graph.edgeSet();
    
  }
  
  /**
  * @param key
  * @return
  */
  public Set<StringEdge> edgeSet(String key) {
    
    if (key.equals(Utils.KEY)) return graph.edgeSet();
    
    return null;
    
  }
  
  /* Hider only methods */
  
  /**
  * @param location
  */
  public void addHideLocation(Object caller, StringVertex location) {
    
    if (!(caller instanceof Hider)) return;
    
    graph.addHideLocation(location);
    
    
  }
  
  /**
  * Whether a seeker is permitted to know the number of hide locations
  */
  private static boolean SEEKER_KNOWS_NUMBER_OF_HIDE_LOCATIONS = true;
  
  /**
  * 
  */
  private int numberOfHideLocations;
  
  /**
  * @return
  */
  public int numberOfHideLocations() {
    
    return numberOfHideLocations;
    
  }
  
  /**
  * @return
  */
  public int numberOfHideLocations(Object caller) {
    
    if (!(caller instanceof Hider) && !SEEKER_KNOWS_NUMBER_OF_HIDE_LOCATIONS) return -1;
    
    return graph.numberOfHideLocations();
    
  }
  
  /////////////////////////////////////////////
  
  /**
  * Let players know the edges of a given node
  * 
  * @param currentNode
  * @return
  */
  public Set<? extends StringEdge> edgesOf(GraphTraverser agent, StringVertex vertex) {
    
    // ~MDC 9/6 Second condition is hacky.
    if ( !agent.currentNode().equals(vertex) && !(agent instanceof AdaptiveGraphTraversingAgent) ) { 
      
      throw new UnsupportedOperationException("At " + agent.currentNode() + " asking for edges of " + vertex);
      
      /*Set<? extends StringEdge> blankSet = new HashSet<StringEdge>(graph.edgesOf(vertex));
      
      blankSet.clear();
      
      return blankSet; */
      
    } else {
      
      return graph.edgesOf(vertex);
      
    }
    
  }
  
  /**
  * @param key
  * @param vertex
  * @return
  */
  public Set<? extends StringEdge> edgesOf(String key, StringVertex vertex) {
    
    if ( key.equals(Utils.KEY) ) {
      
      return graph.edgesOf(vertex);
      
    } else {
      
      throw new UnsupportedOperationException(":(");
      
    }
    
  }
  
  /**
  * Let players know size of topology
  * 
  * @return
  */
  public ArrayList<StringVertex> vertexSet() {
    
    return new ArrayList<StringVertex>(graph.vertexSet());
    
  }
  
  /**
  * Let players register themselves
  * 
  * @param graphTraverser
  */
  public void registerTraversingAgent(GraphTraverser graphTraverser) {
    
    graph.registerTraversingAgent(graphTraverser);
    
  }
  
  /**
  * Let players deregister themselves
  * 
  * @param graphTraverser
  */
  public void deregisterTraversingAgent(GraphTraverser graphTraverser) {
    
    graph.deregisterTraversingAgent(graphTraverser);
    
  }
  
  /**
  * Let players request the average performance of all seekers for a given round
  * 
  * @param roundsPassed
  * @return
  * @deprecated
  */
  private double averageSeekersRoundPerformance(int round) {
    
    return graph.averageSeekersRoundPerformance(round);
    
  }
  
  /**
  * Let players request the average performance of all seekers for the LATEST round
  * 
  * @param roundsPassed
  * @return
  * @deprecated
  */
  private double averageSeekersPerformance(Metric metric) {
    
    return graph.averageSeekersPerformance(metric);
    
  }
  
  /**
  * Let players know the weight of edges
  * 
  * @param edge
  * @return
  */
  public double getEdgeWeight(StringEdge edge) {
    
    return graph.getEdgeWeight(edge);
    
  }
  
  /**
  * @return
  */
  public double totalEdgeWeight() {
    
    double totalWeight = 0.0;
    
    for ( StringEdge edge : graph.edgeSet() ) {
      
      totalWeight += edge.getWeight();
      
    }
    
    return totalWeight;
    
  }
  
  /**
  * @param currentNode
  * @return
  */
  public boolean isHideLocation(GraphTraverser caller, StringVertex requestedNode) {
    
    if ( caller instanceof Hider ) { 
      
      return graph.isHideLocation(requestedNode);
      
    } else {
      
      //~MDC Second condition is hacky
      if (caller.currentNode() != requestedNode && !(caller instanceof AdaptiveGraphTraversingAgent) ) throw new UnsupportedOperationException(caller + " trying to check for an object in a node they are not currently at.");
      
      return graph.isHideLocation(requestedNode);
      
    }
    
  }
  
  /**
  * @param seeker
  * @param currentNode
  * @param nextNode
  * @return
  */
  public boolean fromVertexToVertex(GraphTraverser traverser, StringVertex currentNode,
  StringVertex nextNode) {
    
    return graph.fromVertexToVertex(traverser, currentNode, nextNode);
    
  }
  
  /**
  * Walks a given traverser over a specific path
  * 
  * @param traverser
  * @param sourceVertex
  * @param targetVertex
  * @return
  */
  public void walkPathFromVertexToVertex(GraphTraverser traverser, StringVertex sourceVertex, StringVertex targetVertex) {
    
    List<StringEdge> path = graph.pathFromVertexToVertex(sourceVertex, targetVertex);
    
    for (StringEdge edge : path) {
      
      Utils.talk(toString(), "Walk: " + edge);
      
      graph.fromVertexToVertex(traverser, edge.getSource(), edge.getTarget());
      
    }
    
  }
  
  /**
  * @param seeker
  * @return
  */
  public ArrayList<StringVertex> latestRoundPaths(GraphTraverser traverser) {
    
    return graph.latestRoundPaths(traverser);
    
  }
  
  /**
  * Allow players to know how many edges there are in the graph
  * @return
  */
  public int edgeSetSize() {
    
    return graph.edgeSet().size();
    
  }
  
  /**
  * @param roundsPassed
  * @param traverser
  * @return
  */
  public double roundCost(int roundsPassed, GraphTraverser traverser) {
    
    return graph.roundCost(roundsPassed, true, traverser);
    
  }
  
  /**
  * @param traverser
  * @param source
  * @param target
  * @return
  */
  public double traverserEdgeCost(GraphTraverser traverser, StringVertex source, StringVertex target) {
    
    return graph.traverserEdgeCost(traverser, source, target);
    
  }
  
  /**
  * @param hider
  * @return
  */
  public double averageHiderPayoff(Hider hider) {
    
    return graph.averageHiderPayoff(hider);
    
  }
  
  /**
  * @param vertex
  * @return
  */
  public int degreeOf(StringVertex vertex) {
    
    return graph.degreeOf(vertex);
    
  }
  
  /**
  * @param edgeToTarget
  * @return
  */
  public char getNodeType(StringVertex vertex) {
    
    return graph.getNodeType(vertex);
    
  }
  
  /**
  * @return
  */
  public int getNumberOfNodeTypes() {
    
    return graph.getNumberOfNodeTypes();
    
  }
  
  /**
  * @param currentNode
  * @param nextNode
  * @return
  */
  public StringEdge getEdge(StringVertex currentNode, StringVertex nextNode) {
    
    return graph.getEdge(currentNode, nextNode);
    
  }
  
  /**
  * @param currentNode
  * @param nextNode
  * @return
  */
  public boolean containsEdge(StringVertex currentNode, StringVertex nextNode) {
    
    return graph.containsEdge(currentNode, nextNode);
    
  }
  
  /**
  * @param seeker
  * @return
  */
  public double latestRoundCosts(GraphTraverser traverser, boolean normalised) {
    
    return graph.latestRoundCosts(traverser, normalised);
    
  }
  
  /**
  * @param traverser
  * @return
  */
  public double averageGameCosts(GraphTraverser traverser) {
    
    return graph.averageGameCosts(traverser);
    
  }
  
  /**
  * @param seeker
  * @return
  */
  public double averageSeekerPayoff(Seeker seeker) {
    
    return graph.averageSeekerPayoff(seeker);
    
  }
  
  /**
  * @param traverser
  * @return
  */
  public double averagePathLength(GraphTraverser traverser) {
    
    return graph.averagePathLength(traverser);
    
  }
  
  /**
  * @return
  */
  public double getEdgeTraversalDecrement() {
    
    return graph.getEdgeTraversalDecrement();
    
  }
  
  /**
  * @param hider
  * @return
  */
  public double latestHiderRoundPayoffs(Hider hider) {
    
    return graph.latestHiderRoundPayoffs(hider);
    
  }
  
  /**
  * @param edge
  * @param weight
  */
  public void setEdgeWeight(StringEdge edge, double weight) {
    
    graph.setEdgeWeight(edge, weight);
    
  }
  
  /**
  * @param traverser
  * @param metric
  * @return
  */
  public double latestTraverserRoundPerformance(GraphTraverser traverser, Metric metric) {
    
    return graph.latestTraverserRoundPerformance(traverser, metric);
    
  }
  
  /**
  * @param traverser
  * @param score
  * @param normalised
  * @return
  */
  public Double latestTraverserRoundPerformance(GraphTraverser traverser, Metric score, boolean normalised) {
    
    return graph.latestTraverserRoundPerformance(traverser, score, normalised);
    
  }
  
}
